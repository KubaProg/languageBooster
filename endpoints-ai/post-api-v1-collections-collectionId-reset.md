#### `POST /api/v1/collections/{collectionId}/reset`

-   **Description**: Resets the learning progress for a collection by marking all its cards as "unknown".
-   **Response Payload (Success)**: `204 No Content`
-   **Success Codes**:
    -   `204 No Content`: The collection's progress was successfully reset.
-   **Error Codes**:
    -   `404 Not Found`: No collection found with the given ID.
