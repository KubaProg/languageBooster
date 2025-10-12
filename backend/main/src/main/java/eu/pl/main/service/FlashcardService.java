package eu.pl.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.dto.openrouter.*;
import eu.pl.main.entity.Card;
import eu.pl.main.entity.Collection;
import eu.pl.main.repository.CardRepository;
import eu.pl.main.repository.CollectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    private final OpenRouterClient openRouterClient;
    private final CollectionRepository collectionRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;
    private final String defaultModel;

    public FlashcardService(OpenRouterClient openRouterClient,
                            CollectionRepository collectionRepository,
                            CardRepository cardRepository,
                            AuthService authService,
                            @Value("${openrouter.default-model}") String defaultModel) {
        this.openRouterClient = openRouterClient;
        this.collectionRepository = collectionRepository;
        this.cardRepository = cardRepository;
        this.authService = authService;
        this.defaultModel = defaultModel;
    }

    @Transactional
    public CompletableFuture<CollectionResponseDto> generateFlashCardsCollection(String name, String sourceText, String sourceLang, String targetLang) {
        String systemPrompt = buildSystemPrompt(sourceLang, targetLang);
        String userPrompt = sourceText;
        Map<String, Object> schema = buildFlashcardSchema();

        var messages = List.of(new Message("system", systemPrompt), new Message("user", userPrompt));
        var responseFormat = buildResponseFormat(schema);

        var request = new OpenRouterChatRequest(defaultModel, messages, responseFormat, 0.7, 2048);

        return openRouterClient.sendChatRequest(request)
                .map(response -> {
                    try {
                        String jsonContent = response.choices().get(0).message().content();
                        ObjectMapper mapper = new ObjectMapper();
                        FlashcardCollection flashcardCollection = mapper.readValue(jsonContent, FlashcardCollection.class);

                        Collection collection = Collection.builder()
                                .name(name)
                                .baseLang(sourceLang)
                                .targetLang(targetLang)
                                .ownerId(UUID.fromString("f885b81a-6541-47e1-ab5e-b8bf5d6a00a7")) // Hardcoded user ID
                                .createdAt(OffsetDateTime.now())
                                .build();
                        Collection savedCollection = collectionRepository.save(collection);

                        List<Card> cards = flashcardCollection.flashcards().stream()
                                .map(flashcard -> Card.builder()
                                        .front(flashcard.base())
                                        .back(flashcard.translation())
                                        .known(false)
                                        .collectionId(savedCollection.getId())
                                        .createdAt(OffsetDateTime.now())
                                        .updatedAt(OffsetDateTime.now())
                                        .build())
                                .collect(Collectors.toList());
                        cardRepository.saveAll(cards);

                        return new CollectionResponseDto(
                                savedCollection.getId(),
                                savedCollection.getName(),
                                savedCollection.getBaseLang(),
                                savedCollection.getTargetLang(),
                                savedCollection.getCreatedAt(),
                                (long) cards.size()
                        );

                    } catch (JsonProcessingException e) {
                        throw new CompletionException("Failed to deserialize structured response from OpenRouter API", e);
                    }
                })
                .toFuture();
    }

    private String buildSystemPrompt(String sourceLang, String targetLang) {
        return String.format(
                "You are an expert language tutor. Your task is to create a list of flashcards from the user's text. " +
                "The user provides text in %s, and you must provide translations in %s. " +
                "Each flashcard should be a JSON object with a 'base' field for the original word/phrase " +
                "and a 'translation' field for its translation. Your response must be a single JSON object " +
                "containing a list of these flashcards under the 'flashcards' key.",
                sourceLang, targetLang
        );
    }

    private ResponseFormat buildResponseFormat(Map<String, Object> schema) {
        var jsonSchema = new JsonSchema("flashcard_collection_response", true, schema);
        return new ResponseFormat("json_schema", jsonSchema);
    }

    private Map<String, Object> buildFlashcardSchema() {
        Map<String, Object> flashcardSchema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "base", Map.of("type", "string", "description", "The word or phrase in the base language."),
                        "translation", Map.of("type", "string", "description", "The translated word or phrase.")
                ),
                "required", List.of("base", "translation")
        );

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "flashcards", Map.of("type", "array", "items", flashcardSchema)
                ),
                "required", List.of("flashcards")
        );
    }
}
