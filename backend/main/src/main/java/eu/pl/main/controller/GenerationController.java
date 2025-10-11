package eu.pl.main.controller;

import eu.pl.main.dto.openrouter.FlashcardCollection;
import eu.pl.main.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/generate")
@RequiredArgsConstructor
public class GenerationController {

    private final FlashcardService flashcardService;

    // Inner class to define the request body structure for this endpoint
    public record FlashcardRequest(String text, String sourceLang, String targetLang) {}

    @PostMapping("/flashcards")
    public CompletableFuture<ResponseEntity<FlashcardCollection>> generateFlashcards(@RequestBody FlashcardRequest request) {
        return flashcardService.generateFlashCardsCollection(request.text(), request.sourceLang(), request.targetLang())
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> ResponseEntity.status(500).build()); // Basic error handling, to be refined by GlobalExceptionHandler
    }
}
