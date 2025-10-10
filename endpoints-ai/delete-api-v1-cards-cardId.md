#### `DELETE /api/v1/cards/{cardId}`

-   **Description**: Deletes a single card from a collection.
-   **Response Payload (Success)**: `204 No Content`
-   **Success Codes**:
    -   `204 No Content`: The card was successfully deleted.
-   **Error Codes**:
    -   `404 Not Found`: No card found with the given ID.