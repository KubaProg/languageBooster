#### `PUT /api/v1/cards/{cardId}`

-   **Description**: Updates the content (`front` and `back` text) of a single card.
-   **Request Payload**: `application/json`
    ```json
    {
      "front": "Updated front text",
      "back": "Updated back text"
    }
    ```
-   **Response Payload (Success)**: `200 OK`
    ```json
    {
      "id": "c1d2e3f4-a5b6-c7d8-e9f0-a1b2c3d4e5f6",
      "front": "Updated front text",
      "back": "Updated back text",
      "known": false,
      "updatedAt": "2025-10-09T18:00:00Z"
    }
    ```
-   **Success Codes**:
    -   `200 OK`: The card was successfully updated.
-   **Error Codes**:
    -   `400 Bad Request`: Invalid request body (e.g., empty text).
    -   `404 Not Found`: No card found with the given ID.