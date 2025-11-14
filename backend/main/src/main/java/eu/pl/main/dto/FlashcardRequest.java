package eu.pl.main.dto;

public record FlashcardRequest(String name, String text, String sourceLang, String targetLang, int cardsToGenerate) {
}
