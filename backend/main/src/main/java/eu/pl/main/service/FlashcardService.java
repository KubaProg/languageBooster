package eu.pl.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.pl.main.dto.openrouter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class FlashcardService {

    private final OpenRouterClient openRouterClient;
    private final String defaultModel;

    @Autowired
    public FlashcardService(OpenRouterClient openRouterClient, @Value("${openrouter.default-model}") String defaultModel) {
        this.openRouterClient = openRouterClient;
        this.defaultModel = defaultModel;
    }

    public CompletableFuture<FlashcardCollection> generateFlashCardsCollection(String sourceText, String sourceLang, String targetLang) {
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
                    return mapper.readValue(jsonContent, FlashcardCollection.class);
                } catch (JsonProcessingException e) {
                    // This exception will be handled by a global exception handler
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
