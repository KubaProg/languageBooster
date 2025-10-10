#### `PATCH /api/v1/cards/{cardId}`

-   **Description**: Partially updates a card. Used to mark a card as "known".
-   **Request Payload**: `application/json`
    ```json
    {
      "known": true
    }
    ```
-   **Response Payload (Success)**: `200 OK`
    ```json
    {
      "id": "c1d2e3f4-a5b6-c7d8-e9f0-a1b2c3d4e5f6",
      "front": "Hello",
      "back": "Cześć",
      "known": true,
      "updatedAt": "2025-10-09T18:05:00Z"
    }
    ```
-   **Success Codes**:
    -   `200 OK`: The card was successfully updated.
-   **Error Codes**:
    -   `400 Bad Request`: Invalid request body.
    -   `404 Not Found`: No card found with the given ID.