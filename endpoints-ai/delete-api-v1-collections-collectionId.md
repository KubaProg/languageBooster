#### `DELETE /api/v1/collections/{collectionId}`

-   **Description**: Deletes a specific collection and all of its associated cards.
-   **Response Payload (Success)**: `204 No Content`
-   **Success Codes**:
    -   `204 No Content`: The collection was successfully deleted.
-   **Error Codes**:
    -   `404 Not Found`: No collection found with the given ID.