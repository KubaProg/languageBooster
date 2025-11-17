### Action Plan for E2E Tests

1.  **Test: Create a new collection from text and verify it appears on the list.**
    *   **Arrange:**
        *   Log in to the application.
        *   Navigate to the collections list page.
    *   **Act:**
        *   Click the "New Collection" button.
        *   Fill in the generation form (paste text, select languages, set card count, name the collection).
        *   Submit the form to generate flashcards.
    *   **Assert:**
        *   Wait for redirection to the collections list.
        *   Verify the newly created collection is visible on the page.

2.  **Test: Study a collection and use "Known"/"Unknown" actions.**
    *   **Arrange:**
        *   Log in and ensure at least one collection exists.
        *   Navigate to the collections list.
    *   **Act:**
        *   Click on a collection to start a study session.
        *   Flip the current flashcard.
        *   Click the "Umiem" (Known) button.
    *   **Assert:**
        *   Verify the card is removed from the review pile.
    *   **Act:**
        *   Click the "Nie umiem" (Unknown) button on the next card.
    *   **Assert:**
        *   Verify the next card in the sequence is displayed.

3.  **Test: Edit and delete a card within a collection.**
    *   **Arrange:**
        *   Log in and navigate to a collection's study page.
    *   **Act:**
        *   Enable "Edit" mode.
        *   Modify the text on the front and back of the card.
        *   Save the changes.
    *   **Assert:**
        *   Verify the card's content has been updated.
    *   **Act:**
        *   Re-enable "Edit" mode.
        *   Click the "Delete" button and confirm the action.
    *   **Assert:**
        *   Verify the card is removed and the next card is displayed.
