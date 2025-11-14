package eu.pl.main.controller;

import eu.pl.main.dto.CollectionResponseDto;
import eu.pl.main.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/generate")
@RequiredArgsConstructor
public class GenerationController {

    private final FlashcardService flashcardService;

    // Inner class to define the request body structure for this endpoint
    public record FlashcardRequest(String name, String text, String sourceLang, String targetLang) {}

    @PostMapping("/flashcards")
    public ResponseEntity<CollectionResponseDto> generateFlashcards(@RequestBody FlashcardRequest request) {
        try {
            CollectionResponseDto response = flashcardService.generateFlashCardsCollection(
                    request.name(),
                    request.text(),
                    request.sourceLang(),
                    request.targetLang()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Możesz tu logować lub rzucać dalej do @ControllerAdvice
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/flashcards-from-file")
    public ResponseEntity<CollectionResponseDto> generateFlashcardsFromFile(@RequestParam("file") MultipartFile file,
                                                                            @RequestParam("name") String name,
                                                                            @RequestParam("sourceLang") String sourceLang,
                                                                            @RequestParam("targetLang") String targetLang) {
        try {
            CollectionResponseDto response = flashcardService.generateFlashCardsCollectionFromFile(
                    name,
                    file,
                    sourceLang,
                    targetLang
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
