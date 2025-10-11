# UI Architecture for LanguageBooster

## 1. UI Structure Overview

The LanguageBooster UI is designed as a responsive, single-page application (SPA) built on Angular. The architecture is centered around a clear separation of public (authentication) and protected (application core) views.

After authentication, the user interacts with a main application layout that features a persistent top navigation bar for global actions like navigating to collections, creating new content, and logging out. The core user experience is divided into two main workflows: **Collection Generation** and **Collection Study**.

The architecture prioritizes clear, linear user flows, immediate feedback through notifications, and a simple state management strategy suitable for an MVP. It is designed to be desktop-first but includes responsive considerations for mobile use, ensuring key interactions are accessible on smaller screens.

## 2. View List

### Public Views

---

- **View name**: Login View
- **View path**: `/login`
- **Main purpose**: To allow existing users to sign in.
- **Key information to display**: Email and password fields, a "Login" button, and a link to the Register View.
- **Key view components**:
    - `AuthForm` (shared with Register View)
    - `Button`
- **UX, accessibility, and security considerations**:
    - **UX**: Clear error messages on failed login (e.g., "Invalid credentials"). Autofocus on the email field.
    - **Accessibility**: Proper form labels (`<label for="...">`), keyboard navigation, and input validation feedback.
    - **Security**: All authentication is handled via Supabase. The form should not expose detailed reasons for login failure.

---

- **View name**: Register View
- **View path**: `/register`
- **Main purpose**: To allow new users to create an account.
- **Key information to display**: Email and password fields, a "Register" button, and a link back to the Login View.
- **Key view components**:
    - `AuthForm`
    - `Button`
- **UX, accessibility, and security considerations**:
    - **UX**: On successful registration, the user is automatically redirected to the Login View. Password strength indicators can be a future improvement.
    - **Accessibility**: Proper form labels and validation for email format and password length.
    - **Security**: Communication with Supabase for account creation.

### Protected Views

---

- **View name**: Dashboard View
- **View path**: `/app/dashboard`
- **Main purpose**: To act as a central landing page and navigation hub after login.
- **Key information to display**: Welcome message, and two primary action cards/buttons.
- **Key view components**:
    - `ActionCard` ("View My Collections")
    - `ActionCard` ("Create a New Collection")
    - `MainLayout` (Wrapper with top navigation)
- **UX, accessibility, and security considerations**:
    - **UX**: Provides a clear, simple starting point for the user's main tasks.
    - **Accessibility**: Cards should be focusable and navigable with a keyboard.
    - **Security**: This route is protected by an authentication guard.

---

- **View name**: Collection List View
- **View path**: `/app/collections`
- **Main purpose**: To display all the user's flashcard collections and allow management.
- **Key information to display**: A list or grid of collections. An "empty state" message if no collections exist.
- **Key view components**:
    - `MainLayout`
    - `CollectionCard` (displays name, languages, card count, and a delete icon)
    - `Button` ("Create New Collection")
    - `ConfirmationModal` (for delete action)
- **UX, accessibility, and security considerations**:
    - **UX**: Collections are fetched on every visit to ensure data is fresh. A confirmation modal prevents accidental deletion. On mobile, this view switches to a single-column list.
    - **Accessibility**: Each collection card is focusable. The delete icon should have an accessible label (e.g., "Delete collection 'My First Collection'").
    - **Security**: Fetches data using `GET /api/v1/collections`, which is secured by the user's auth token.

---

- **View name**: Generation Form View
- **View path**: `/app/collections/new`
- **Main purpose**: To provide a form for creating a new collection from a source.
- **Key information to display**: Form fields for collection name, base/target languages, number of cards, and the source (text or PDF).
- **Key view components**:
    - `MainLayout`
    - `TextInput`, `SelectInput`, `NumberInput`
    - `TabComponent` (to switch between "Paste Text" and "Upload PDF")
    - `FileUploadComponent`
    - `Button` ("Generate")
- **UX, accessibility, and security considerations**:
    - **UX**: Client-side validation provides immediate feedback (e.g., PDF too large, card count out of 1-100 range). If generation fails, the user is returned here with all form data preserved.
    - **Accessibility**: All form fields have labels. Validation errors are clearly associated with their respective inputs.
    - **Security**: The file upload component should validate file types and size on the client side as a first line of defense.

---

- **View name**: Collection Study View
- **View path**: `/app/collections/:id/study`
- **Main purpose**: To allow the user to study and edit cards in a collection.
- **Key information to display**: The current flashcard, study controls, and mode-specific UI.
- **Key view components**:
    - `MainLayout`
    - `Flashcard` (manages front/back display and edit state)
    - `Button` ("Umiem", "Nie umiem")
    - `IconButton` (for toggling edit mode)
    - `ConfirmationModal` (for resetting progress at the end of a session)
- **UX, accessibility, and security considerations**:
    - **UX**: The view has two distinct modes: **Learning** and **Editing**. In edit mode, navigation is disabled to prevent data loss, and a blue toast appears if the user tries to leave the view. On mobile, control buttons are large and placed at the bottom.
    - **Accessibility**: The flashcard should be keyboard-navigable (e.g., spacebar to flip). All interactive elements need accessible labels.
    - **Security**: This route is protected. All API calls (`GET`, `PATCH`, `PUT`, `DELETE`) are authenticated.

## 3. User Journey Map

The user journey is designed to be straightforward and task-oriented.

1.  **Authentication**: A new user follows the `Register -> Login` path. An existing user goes directly to `Login`.
2.  **Post-Login**: The user lands on the **Dashboard**, which presents two clear choices: "View My Collections" or "Create a New Collection".
3.  **Creation Path**:
    - From the Dashboard, the user navigates to the **Generation Form**.
    - After submitting the form, a **Loading Overlay** is shown.
    - On completion, the user is redirected to the **Collection List**, where the new collection is visible and a success toast is shown.
4.  **Study Path**:
    - From the Dashboard or Collection List, the user selects a collection and enters the **Collection Study View**.
    - The user cycles through cards using the "Umiem"/"Nie umiem" buttons.
    - At the end of the session, a modal asks to reset progress. The user is then returned to the **Collection List**.
5.  **Editing Path**:
    - Within the Study View, the user can toggle **Edit Mode**.
    - This locks navigation and allows direct editing of the card's text. Changes are saved via a "Save" button.

## 4. Layout and Navigation Structure

The application uses a simple and consistent layout for all authenticated views.

-   **Main Layout**: A primary `MainLayout` component wraps all protected views. It contains a persistent top navigation bar and a content area where the active view is rendered.
-   **Top Navigation Bar**:
    -   **Logo**: Acts as a home button, linking to the `/app/dashboard`.
    -   **Links**: "My Collections" (`/app/collections`) and "Create New" (`/app/collections/new`).
    -   **User Menu**: A dropdown on the far right containing a "Logout" button.
-   **Routing**: The Angular Router manages navigation between views. Routes are protected by an `AuthGuard` to prevent access without a valid session. The URL structure is predictable (e.g., `/app/collections/:id/study`).

## 5. Key Components

These are reusable components that form the building blocks of the UI.

-   **`NotificationToast`**: A global component for displaying timed, color-coded (red/error, green/success, blue/info) messages to the user.
-   **`ConfirmationModal`**: A dialog used for critical actions like deleting a collection or resetting progress, requiring explicit user confirmation.
-   **`LoadingOverlay`**: A full-screen overlay with a spinner, used to block UI interaction during long-running async operations like AI generation.
-   **`CollectionCard`**: A component used in the `CollectionListView` to represent a single collection, displaying its metadata and providing an entry point for study and management actions.
-   **`Flashcard`**: The core interactive component of the `CollectionStudyView`. It handles its own internal state (flipped/unflipped) and has different visual states for learning and editing modes.
