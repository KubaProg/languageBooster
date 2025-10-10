#### `PUT /api/v1/collections/{collectionId}`

-   **Description**: Updates the name of a specific collection.
-   **Request Payload**: `application/json`
    ```json
    {
      "name": "Updated Collection Name"
    }
    ```
-   **Response Payload (Success)**: `200 OK`
    ```json
    {
      "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
      "name": "Updated Collection Name",
      "baseLang": "en",
      "targetLang": "pl",
      "createdAt": "2025-10-09T17:31:17Z"
    }
    ```
-   **Success Codes**:
    -   `200 OK`: The collection was successfully updated.
-   **Error Codes**:
    -   `400 Bad Request`: The request body is invalid (e.g., name is empty).
    -   `404 Not Found`: No collection found with the given ID.
    -   `409 Conflict`: A collection with the same name already exists for this user.