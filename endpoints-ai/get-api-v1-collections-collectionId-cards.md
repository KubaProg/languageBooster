#### `GET /api/v1/collections/{collectionId}/cards`

-   **Description**: Retrieves a list of cards within a specific collection. Primarily used for the learning view.
-   **Query Parameters**:
    -   `known` (boolean, optional): Filter cards by their `known` status (e.g., `known=false` for a learning session). If omitted, returns all cards.
-   **Response Payload (Success)**: `200 OK`
    ```json
    [
      {
        "id": "c1d2e3f4-a5b6-c7d8-e9f0-a1b2c3d4e5f6",
        "front": "Hello",
        "back": "Cześć",
        "known": false,
        "updatedAt": "2025-10-09T17:31:17Z"
      }
    ]
    ```
-   **Success Codes**:
    -   `200 OK`: Successfully retrieved the list of cards.
-   **Error Codes**:
    -   `404 Not Found`: No collection found with the given ID.