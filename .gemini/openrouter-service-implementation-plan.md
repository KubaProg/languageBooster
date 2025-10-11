# OpenRouter Service Implementation Guide

## 1. Service Description

The OpenRouter Service will act as a dedicated client for interacting with the OpenRouter API. Its primary responsibility is to abstract the complexities of API communication, providing a simple, high-level interface for performing chat completions. It will handle request construction, authentication, structured response formatting (via JSON schema), and robust error handling, adhering to the project's established backend rules and technology stack.

## 2. Constructor Description

The main service, `OpenRouterService`, will be a Spring `@Service` component. It will use constructor injection to receive its dependencies.

```java
@Service
public class OpenRouterService {

    private final OpenRouterClient openRouterClient;
    private final String defaultModel;

    @Autowired
    public OpenRouterService(OpenRouterClient openRouterClient, @Value("${openrouter.default-model}") String defaultModel) {
        this.openRouterClient = openRouterClient;
        this.defaultModel = defaultModel;
    }

    // ... methods
}
```

- **`OpenRouterClient`**: A dedicated client component responsible for the low-level HTTP communication with the OpenRouter API.
- **`@Value("${openrouter.default-model}")`**: Injects the default model name (e.g., `openai/gpt-4o`) from the `application.properties` file, allowing for easy configuration.

## 3. Public Methods and Fields

### `OpenRouterService`

- **`public CompletableFuture<String> getSimpleChatCompletion(String userPrompt)`**:
  - **Description**: Performs a basic chat completion for a given user prompt using the default model and settings.
  - **Parameters**:
    - `userPrompt`: The user's message.
  - **Returns**: A `CompletableFuture<String>` containing the AI's text response.

- **`public <T> CompletableFuture<T> getStructuredChatCompletion(String userPrompt, String systemPrompt, Class<T> responseClass, Map<String, Object> schema)`**:
  - **Description**: Performs a chat completion that returns a structured JSON object matching the provided schema.
  - **Parameters**:
    - `userPrompt`: The user's message.
    - `systemPrompt`: The system message to guide the model's behavior.
    - `responseClass`: The class type (`.class`) that the JSON response should be deserialized into.
    - `schema`: A `Map<String, Object>` representing the JSON schema for the desired output.
  - **Returns**: A `CompletableFuture<T>` containing the deserialized object of type `T`.

## 4. Private Methods and Fields

### `OpenRouterClient`

This component will handle the actual HTTP request logic.

- **`private final WebClient webClient`**: The reactive Spring `WebClient` used for making non-blocking HTTP requests. It will be configured as a bean with the base URL and authentication headers.
- **`private Mono<OpenRouterChatResponse> sendChatRequest(OpenRouterChatRequest request)`**:
  - **Description**: Sends the prepared request DTO to the OpenRouter `/chat/completions` endpoint.
  - **Parameters**:
    - `request`: The fully constructed `OpenRouterChatRequest` object.
  - **Returns**: A `Mono<OpenRouterChatResponse>` with the API's response.

### `OpenRouterService`

- **`private ResponseFormat buildResponseFormat(Map<String, Object> schema)`**:
  - **Description**: A helper method to construct the `ResponseFormat` object required by the OpenRouter API for structured JSON output.
  - **Parameters**:
    - `schema`: The JSON schema definition.
  - **Returns**: A `ResponseFormat` DTO.

## 5. Error Handling

A centralized exception handler using `@RestControllerAdvice` will be implemented to ensure consistent error responses.

- **`@ExceptionHandler(WebClientResponseException.class)`**:
  - **Scenario**: Catches errors returned from the OpenRouter API (e.g., 401 Unauthorized, 400 Bad Request, 5xx Server Error).
  - **Action**: Parses the error details from the API response body. Returns a `502 Bad Gateway` or `400 Bad Request` with a structured `ErrorDto`. A `401` from the API should trigger a critical log and a generic `500` response to the client to avoid leaking configuration issue details.
- **`@ExceptionHandler(JsonProcessingException.class)`**:
  - **Scenario**: Catches errors during the serialization of the request or deserialization of the response.
  - **Action**: Logs the error and the problematic JSON. Returns a `500 Internal Server Error` with a generic message.
- **`@ExceptionHandler(TimeoutException.class)`**:
  - **Scenario**: Catches timeouts when waiting for a response from the OpenRouter API.
  - **Action**: Logs the timeout event. Returns a `504 Gateway Timeout`.

## 6. Security Considerations

1.  **API Key Management**: The `openrouter.api.key` must not be hardcoded or committed to version control. It should be loaded from environment variables or a secure secrets management system.
2.  **Input Sanitization**: While the primary interaction is with a trusted API, any user-provided input that is reflected in logs or internal systems should be treated as untrusted data.
3.  **Denial of Service**: Implement rate limiting on the API endpoints that trigger the OpenRouter service to prevent abuse and control costs.
4.  **Logging**: Ensure that sensitive information, such as the full API key or raw user data, is not logged in production environments.

## 7. Step-by-Step Implementation Plan

### Step 1: Configure Project Dependencies and Properties

1.  **Add `spring-boot-starter-webflux`**: Ensure this dependency is in `pom.xml` to use `WebClient`.
2.  **Update `application.properties`**: Add the necessary configuration properties.

    ```properties
    # OpenRouter Configuration
    openrouter.api.url=https://openrouter.ai/api/v1
    openrouter.api.key=${OPENROUTER_API_KEY} # Load from environment variable
    openrouter.default-model=openai/gpt-4o
    ```

### Step 2: Create DTOs as Java Records

Create immutable DTOs to represent the API contract.

```java
// In package eu.pl.main.dto.openrouter

// For the request
public record Message(String role, String content) {}

public record JsonSchema(String name, boolean strict, Map<String, Object> schema) {}

public record ResponseFormat(String type, JsonSchema json_schema) {}

public record OpenRouterChatRequest(
    String model,
    List<Message> messages,
    ResponseFormat response_format,
    Double temperature,
    Integer max_tokens
) {}

// For the response
public record OpenRouterUsage(int prompt_tokens, int completion_tokens, int total_tokens) {}

public record OpenRouterChoice(Message message, String finish_reason) {}

public record OpenRouterChatResponse(String id, List<OpenRouterChoice> choices, OpenRouterUsage usage) {}
```

### Step 3: Implement the `OpenRouterClient`

Create a Spring `@Component` to encapsulate `WebClient` logic.

```java
@Component
public class OpenRouterClient {

    private final WebClient webClient;

    public OpenRouterClient(WebClient.Builder webClientBuilder,
                            @Value("${openrouter.api.url}") String baseUrl,
                            @Value("${openrouter.api.key}") String apiKey) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public Mono<OpenRouterChatResponse> sendChatRequest(OpenRouterChatRequest request) {
        return webClient.post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OpenRouterChatResponse.class)
            .timeout(Duration.ofSeconds(30)); // Set a reasonable timeout
    }
}
```

### Step 4: Implement the `OpenRouterService`

Create the main service that orchestrates the logic.

```java
@Service
@RequiredArgsConstructor // Lombok for constructor injection
public class OpenRouterService {

    private final OpenRouterClient openRouterClient;
    private final String defaultModel;

    @Autowired
    public OpenRouterService(OpenRouterClient openRouterClient, @Value("${openrouter.default-model}") String defaultModel) {
        this.openRouterClient = openRouterClient;
        this.defaultModel = defaultModel;
    }

    public CompletableFuture<String> getSimpleChatCompletion(String userPrompt) {
        var systemMessage = new Message("system", "You are a helpful assistant.");
        var userMessage = new Message("user", userPrompt);
        var messages = List.of(systemMessage, userMessage);

        var request = new OpenRouterChatRequest(defaultModel, messages, null, 0.7, 1024);

        return openRouterClient.sendChatRequest(request)
            .map(response -> response.choices().get(0).message().content())
            .toFuture();
    }

    public <T> CompletableFuture<T> getStructuredChatCompletion(String userPrompt, String systemPrompt, Class<T> responseClass, Map<String, Object> schema) {
        var messages = List.of(new Message("system", systemPrompt), new Message("user", userPrompt));
        var responseFormat = buildResponseFormat(schema);

        var request = new OpenRouterChatRequest(defaultModel, messages, responseFormat, 0.7, 2048);

        return openRouterClient.sendChatRequest(request)
            .map(response -> {
                try {
                    String jsonContent = response.choices().get(0).message().content();
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(jsonContent, responseClass);
                } catch (JsonProcessingException e) {
                    throw new CompletionException("Failed to deserialize structured response", e);
                }
            })
            .toFuture();
    }

    private ResponseFormat buildResponseFormat(Map<String, Object> schema) {
        var jsonSchema = new JsonSchema("structured_response", true, schema);
        return new ResponseFormat("json_schema", jsonSchema);
    }
}
```

### Step 5: Expose via a Controller and Handle Errors

Create a REST controller to expose the service and an exception handler for robustness.

```java
// Controller
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final OpenRouterService openRouterService;

    // ... endpoints that call the service
}

// Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorDto> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Error from external API: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        // Return a generic error to the client
        return new ResponseEntity<>(new ErrorDto("Service communication error"), HttpStatus.BAD_GATEWAY);
    }
    
    // ... other handlers from section 5
}
```

### Step 6: Example of `response_format` Usage for Language Flashcards

To get a structured list of language flashcards, you define your schema and call the service as follows. This example shows how to generate Spanish translations from English words.

```java
// In a client service or controller
public void generateLanguageFlashcards() {
    String userPrompt = "Create flashcards for learning Spanish from the following English words: hello, goodbye, thank you, please.";
    String systemPrompt = "You are an expert language tutor. Your task is to create a list of flashcards from the user's text. Each flashcard should be a JSON object with a 'base' field for the original word/phrase and a 'translation' field for its translation. Your response must be a single JSON object containing a list of these flashcards.";

    // 1. Define the schema for a single language flashcard
    Map<String, Object> flashcardSchema = Map.of(
        "type", "object",
        "properties", Map.of(
            "base", Map.of("type", "string", "description", "The word or phrase in the base language."),
            "translation", Map.of("type", "string", "description", "The translated word or phrase.")
        ),
        "required", List.of("base", "translation")
    );

    // 2. Define the top-level schema for the collection of flashcards
    Map<String, Object> flashcardCollectionSchema = Map.of(
        "type", "object",
        "properties", Map.of(
            "flashcards", Map.of("type", "array", "items", flashcardSchema)
        ),
        "required", List.of("flashcards")
    );

    // 3. Define DTO classes to hold the deserialized result
    // public record LanguageFlashcard(String base, String translation) {}
    // public record FlashcardCollection(java.util.List<LanguageFlashcard> flashcards) {}

    // 4. Call the service with the prompts, schema, and target class
    CompletableFuture<FlashcardCollection> future = openRouterService.getStructuredChatCompletion(
        userPrompt,
        systemPrompt,
        FlashcardCollection.class,
        flashcardCollectionSchema
    );

    // 5. Process the result asynchronously
    future.thenAccept(collection -> {
        System.out.println("Successfully generated flashcards!");
        collection.flashcards().forEach(card -> {
            System.out.println("Base: " + card.base() + " -> Translation: " + card.translation());
        });
        // Here, you can process the 'collection' and save it to your database
    }).exceptionally(ex -> {
        System.err.println("Failed to generate flashcards: " + ex.getMessage());
        return null;
    });
}
```
