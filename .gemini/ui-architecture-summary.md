<conversation_summary>
<decisions>
1. Editing a collection's name will not be implemented in the MVP.
2. After a user learns all cards in a collection, a dialog will ask if they want to reset progress. If yes, a `POST /api/v1/collections/{collectionId}/reset` call is made, and the user is redirected to the collections list. In both cases (yes/no), the user is redirected.
3. The backend is responsible for deleting a collection if its last card is deleted. The `DELETE /api/v1/cards/{cardId}` endpoint logic is expected to handle this.
4. API error messages will be displayed in a red, non-interactive pop-up (toast) that appears for 2 seconds. The message content will be taken directly from the API response body.
5. The client-side will not cache the main list of collections. It will be fetched every time the user visits the collections view.
6. Highlighting a newly created collection in the list is not required for the MVP.
7. A global "edit mode" is enabled for the learning view via a toggle. In this mode, card text becomes editable on click, a single "Save" button appears on content change, and a delete 'X' icon is visible for each card.
8. Navigation away from a card (next/previous) is disabled while in edit mode. Attempting to navigate away from the view with unsaved changes will trigger a blue informational pop-up stating "Please save the changes first," blocking the navigation.
9. Authentication state (JWT) will be managed via `localStorage` and an `HttpInterceptor` for attaching auth headers.
10. On mobile, the collections list will be a single column, and learning view buttons will be large touch targets.
11. A blue pop-up will be used for informational messages, and a green one for success messages (e.g., "Collection created successfully!"), both disappearing after 2 seconds.
12. If AI generation fails, the user will be returned to the generation form with all previously entered data preserved, allowing them to retry.
</decisions>
<matched_recommendations>
1. **Authentication Strategy**: The user agreed with the recommendation to store the JWT in `localStorage`, use an `HttpInterceptor` to attach the `Authorization` header to API requests, and use route guards to protect authenticated routes.
2. **Responsive Design**: The user agreed with the recommendation for a single-column layout for the collections list on mobile and large, touch-friendly buttons in the learning view.
3. **Error & Status Notifications**: The user expanded on the recommendation for notifications, specifying a color-coded system: red for errors, green for success, and blue for information. The content for error messages will come from the API response.
4. **Failed Generation Flow**: The user agreed with the recommendation to return to the generation form with all data preserved upon a generation failure, allowing the user to review and retry.
5. **Card Editing UI**: The user clarified the recommendation for an edit mode, specifying a view-level toggle, a single "Save" button that appears on change, and making text editable on click.
6. **End-of-Session Flow**: The user modified the recommendation, requesting a confirmation dialog to reset progress, which requires a new API endpoint (`POST /api/v1/collections/{collectionId}/reset`).
</matched_recommendations>
<ui_architecture_planning_summary>
### 1. Main UI Architecture Requirements
The project is a responsive, desktop-first web application built with Angular and TypeScript, styled with TailwindCSS. It will serve as the frontend for the LanguageBooster MVP. The UI will communicate with a Java/Spring Boot backend. Authentication is handled via Supabase.

### 2. Key Views, Screens, and User Flows
The application is structured around the following views and user flows:
- **Authentication Flow**: A simple initial screen offers `Login` or `Register`. The `Register View` leads to the `Login View`. Successful login directs the user to the main dashboard.
- **Main Dashboard (`Collections/Generating View`)**: A central hub post-login that allows the user to navigate to either their existing collections or the generation form.
- **Generation Flow**:
    - `Generating Form View`: The user can create a new collection by providing a name, language pair, number of cards, and a source (pasted text or an uploaded PDF).
    - `Generating Loading Screen`: A simple loading animation is displayed while the backend processes the source and creates cards.
    - `Collections View`: Upon success, the user is redirected here, where a green success pop-up confirms the creation. Upon failure, the user is returned to the form with all data preserved.
- **Learning Flow**:
    - `Collections View`: Displays a list of the user's collections, showing name, language pair, and card count.
    - `Inside of Collection View` (Learning/Flashcard View): This view has two primary modes:
        - **Learning Mode**: The user sees a card's front, clicks to reveal the back, and chooses "Umiem" (I know) or "Nie umiem" (I don't know). "Umiem" marks the card as known (`PATCH /api/v1/cards/{cardId}`).
        - **Edit Mode**: Toggled by an icon. Navigation is disabled. Card text becomes editable on click. A "Save" button appears on change to update the card (`PUT /api/v1/cards/{cardId}`). A delete 'X' icon is also visible to remove the card (`DELETE /api/v1/cards/{cardId}`).

### 3. API Integration and State Management
- **State Management**: The application will not use a client-side cache for the collections list, fetching it fresh on each visit. During a learning session, the list of unknown cards is fetched once at the beginning and managed entirely on the client-side until the session ends.
- **API Integration**:
    - An `HttpInterceptor` will attach the Supabase JWT from `localStorage` to all backend requests.
    - `GET /api/v1/collections`: To display the collections list.
    - `POST /api/v1/collections/from-source`: To create a new collection.
    - `GET /api/v1/collections/{collectionId}/cards`: To fetch cards for a learning session.
    - `PATCH /api/v1/cards/{cardId}`: To mark a card as "known".
    - `PUT /api/v1/cards/{cardId}`: To save edits to a card's text.
    - `DELETE /api/v1/cards/{cardId}`: To delete a card. The backend is expected to delete the collection if it becomes empty.
    - `POST /api/v1/collections/{collectionId}/reset`: To reset progress for a collection.
- **Notifications**: A consistent, timed (2-second) pop-up/toast system will be used for user feedback, color-coded for intent (red/error, green/success, blue/info).

### 4. Responsiveness, Accessibility, and Security
- **Responsiveness**: The app will be desktop-first but responsive. Key adaptations include a single-column collections list on mobile and large, ergonomic buttons in the learning view.
- **Accessibility**: The color-coded notification system will rely on clear text messages, not just color, to convey information.
- **Security**: Route guards will prevent access to authenticated views. All API communication will be secured via JWT Bearer tokens.

</ui_architecture_planning_summary>
<unresolved_issues>
- **Backend Dependency**: A key user requirement is that the backend should automatically delete a collection when its last card is deleted. This logic was added to the API documentation (`delete-api-v1-cards-cardId.md`) per the user's request, but it represents a required change in the backend's behavior that the frontend will depend on. The current backend implementation may not support this, creating a potential integration issue.
</unresolved_issues>
</conversation_summary>