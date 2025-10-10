#### `GET /api/v1/collections`

-   **Description**: Retrieves a list of all collections for the authenticated user.
-   **Response Payload (Success)**: `200 OK`
    ```json
    [
      {
        "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
        "name": "My First Collection",
        "baseLang": "en",
        "targetLang": "pl",
        "createdAt": "2025-10-09T17:31:17Z",
        "cardCount": 50
      }
    ]
    ```
-   **Success Codes**:
    -   `200 OK`: Successfully retrieved the list of collections.