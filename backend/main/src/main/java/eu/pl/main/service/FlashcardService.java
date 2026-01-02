package eu.pl.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.dto.openrouter.*;
import eu.pl.main.entity.Card;
import eu.pl.main.entity.Collection;
import eu.pl.main.exception.flashcard.*;
import eu.pl.main.repository.CardRepository;
import eu.pl.main.repository.CollectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlashcardService {

    private final OpenRouterClient openRouterClient;
    private final CollectionRepository collectionRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Value("${openrouter.default-model}")
    private String defaultModel;

    @Transactional
    public CollectionResponseDto generateFlashCardsCollectionFromFile(String name,
                                                                      MultipartFile file,
                                                                      String sourceLang,
                                                                      String targetLang) throws IOException {
        String text = extractTextFromFile(file);
        return generateFlashCardsCollection(name, text, sourceLang, targetLang);
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if ("pdf".equalsIgnoreCase(fileExtension)) {
            return extractTextFromPdf(file);
        }
        // Add support for other file types here
        throw new UnsupportedFileTypeException(fileExtension);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(file.getInputStream()))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * Wersja prosta/synchroniczna: wywołanie klienta, parsowanie JSON, zapis do DB, zwrot DTO.
     */
    @Transactional
    public CollectionResponseDto generateFlashCardsCollection(String name,
                                                              String sourceText,
                                                              String sourceLang,
                                                              String targetLang) {
        // 1) Autoryzacja – must have użytkownik
        UUID userId = authService.getAuthenticatedUserId();

        // 2) Prompty + schema
        String systemPrompt = buildSystemPrompt(sourceLang, targetLang);
        String userPrompt = sourceText;
        Map<String, Object> schema = buildFlashcardSchema();

        List<Message> messages = List.of(
                new Message("system", systemPrompt),
                new Message("user", userPrompt)
        );
        ResponseFormat responseFormat = buildResponseFormat(schema);

        OpenRouterChatRequest request = new OpenRouterChatRequest(
                defaultModel,
                messages,
                responseFormat,
                0.7,
                2048
        );

        // 3) Proste synchroniczne wywołanie (bez CompletableFuture)
        // Jeżeli Twój klient jest reaktywny (Mono), użyj .block(); jeśli jest już synchroniczny, po prostu wywołaj metodę.
        OpenRouterChatResponse response;
        try {
            response = openRouterClient.sendChatRequest(request)  // jeśli zwraca Mono -> .block()
                    .block(); // usuń .block() jeżeli klient ma metodę synchroniczną
        } catch (Exception e) {
            throw new OpenRouterApiException(e);
        }
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new OpenRouterEmptyResponseException();
        }

        String jsonContent = response.choices().get(0).message().content();
        if (jsonContent == null || jsonContent.isBlank()) {
            throw new OpenRouterEmptyContentException();
        }

        // 4) Parsowanie JSON → FlashcardCollection (prosto, bez kombinacji)
        FlashcardCollection flashcardCollection;
        try {
            flashcardCollection = objectMapper.readValue(jsonContent, FlashcardCollection.class);
        } catch (Exception e) {
            log.debug("Nieudane parsowanie JSON:\n{}", jsonContent);
            throw new OpenRouterResponseParsingException(e);
        }
        if (flashcardCollection.flashcards() == null || flashcardCollection.flashcards().isEmpty()) {
            throw new OpenRouterNoFlashcardsException();
        }

        // 5) Zapis kolekcji
        Collection collection = Collection.builder()
                .name(name)
                .baseLang(sourceLang)
                .targetLang(targetLang)
                .ownerId(userId)
                .createdAt(OffsetDateTime.now())
                .build();
        Collection savedCollection = collectionRepository.save(collection);

        // 6) Zapis kart (prosta pętla)
        List<Card> cards = new ArrayList<>();
        for (LanguageFlashcard f : flashcardCollection.flashcards()) {
            Card c = Card.builder()
                    .front(f.base())
                    .back(f.translation())
                    .known(false)
                    .collection(savedCollection)
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build();
            cards.add(c);
        }
        cardRepository.saveAll(cards);

        // 7) Zwrot prostego DTO
        return new CollectionResponseDto(
                savedCollection.getId(),
                savedCollection.getName(),
                savedCollection.getBaseLang(),
                savedCollection.getTargetLang(),
                savedCollection.getCreatedAt(),
                (long) cards.size()
        );
    }

    // === Pomoce (zostawione prosto, jak było) ===

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
