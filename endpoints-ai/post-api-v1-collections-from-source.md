#### `POST /api/v1/collections/from-source`

-   **Description**: Creates a new collection and generates flashcards from a source (text or PDF file). This is a single, atomic operation from the user's perspective. The request must be of type `multipart/form-data`.
-   **Request**: `multipart/form-data` containing:
    -   `metadata` (part): A JSON object with the collection details.
        ```json
        {
          "name": "My First Collection",
          "baseLang": "en",
          "targetLang": "pl",
          "cardsToGenerate": 50
        }
        ```
    -   `textSource` (part, optional): Raw text content.
    -   `fileSource` (part, optional): A PDF file (max 5MB).
-   **Response Payload (Success)**: `201 Created`
    ```json
    {
      "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
      "name": "My First Collection",
      "baseLang": "en",
      "targetLang": "pl",
      "createdAt": "2025-10-09T17:31:17Z",
      "cardCount": 50
    }
    ```
-   **Success Codes**:
    -   `201 Created`: The collection and cards were successfully created.
-   **Error Codes**:
    -   `400 Bad Request`: Invalid input (e.g., `baseLang` equals `targetLang`, `cardsToGenerate` out of 1-100 range, missing source).
    -   `409 Conflict`: A collection with the same name already exists for this user.
    -   `413 Payload Too Large`: The uploaded PDF file exceeds the 5MB limit.
    -   `500 Internal Server Error`: AI generation failed or another server error occurred.