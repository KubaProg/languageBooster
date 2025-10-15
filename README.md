# LanguageBooster

LanguageBooster is a web application that transforms your personal study materials into customized language flashcards using AI. Simply paste text or upload a PDF, and let the app generate a new learning deck for you in seconds.

## Project Description

This project aims to solve a common problem for language learners: the time-consuming process of creating personalized study materials. LanguageBooster streamlines this by allowing users to generate contextual flashcards directly from their own content, like a book chapter, article, or personal notes.

### Key Features

- **AI-Powered Generation**: Automatically creates flashcards from user-provided text or PDF files.
- **Language Autodetection**: Intelligently detects the source language of your material.
- **Customizable Decks**: Choose the base and target languages (supports Polish, English, Spanish, and German for the MVP).
- **Simple Learning Interface**: Study your flashcards with a straightforward "I know / I don't know" system.
- **Secure and Personal**: User accounts are managed through Supabase & Spring Security, ensuring that your collections are private.

## Tech Stack

| Category           | Technology                                                   |
| ------------------ | ------------------------------------------------------------ |
| **Backend**        | Java (Spring Boot)                                           |
| **Frontend**       | Angular (`^17.3.0`), TypeScript (`~5.4.2`), TailwindCSS      |
| **Database**       | PostgreSQL                                                   |
| **Authentication** | Supabase (with Spring Security)                              |
| **Containerization**| Docker, Docker Compose                                     |
| **Backend Testing**| JUnit 5, Mockito, Spring Test, Testcontainers, REST Assured  |
| **Frontend Testing**| Jest, Playwright, ESLint                  |
| **CI/CD**          | GitHub Actions                                               |

## Testing

The project follows a comprehensive testing strategy to ensure quality, stability, and security. Testing is performed at multiple levels for both the backend and frontend, as outlined in the detailed test plans.

### Backend Testing Strategy

- **Unit Tests**: Using JUnit 5 and Mockito to verify individual classes and methods in isolation.
- **Integration Tests**: With Spring Test, MockMvc, and Testcontainers to test the collaboration of components within the Spring context, including database interactions.
- **API (E2E) Tests**: Using REST Assured to test the public API endpoints from a client's perspective.
- **Security Tests**: Planned to verify authentication, authorization, and data isolation between users.

### Frontend Testing Strategy

- **Unit & Component Tests**: Using Jest and Angular's TestBed to test components, services, and pipes in isolation.
- **End-to-End (E2E) Tests**: Using Playwright to simulate full user scenarios in a real browser.
- **Manual Tests**: Including UI/UX validation against mockups, and cross-browser compatibility checks (Chrome, Firefox, Safari).

## Getting Started Locally

Follow these instructions to set up and run the project on your local machine.

### Prerequisites

- **Java (JDK 17 or newer)**
- **Node.js** (Check the `.nvmrc` file for the specific version, or use a recent LTS release)
- **Docker** and **Docker Compose**
- **Maven** (The project includes a Maven wrapper)

### Setup and Execution

1.  **Clone the repository:**
    ```sh
    git clone <repository-url>
    cd language-booster
    ```

2. **Run the Backend:**
    Navigate to the backend directory and use the Maven wrapper to start the Spring Boot application.
    ```sh
    cd backend/main
    ./mvnw spring-boot:run
    ```
    The backend server should now be running.

3. **Run the Frontend:**
    In a new terminal, navigate to the frontend directory, install dependencies, and start the Angular development server.
    ```sh
    cd frontend
    npm install
    npm start
    ```
    The application will be available at `http://localhost:4200/`. The `proxy.conf.json` file handles proxying API requests to the backend.

## Available Scripts

The following scripts are available for the frontend application and can be run with `npm run <script-name>`.

| Script      | Description                                             |
| ----------- | ------------------------------------------------------- |
| `start`     | Runs the application in development mode (`ng serve`).    |
| `build`     | Builds the application for production.                  |
| `test`      | Runs unit tests with Jest and generates a coverage report.                              |
| `lint`      | Lints the codebase using ESLint.                        |
| `watch`     | Builds the app and watches for file changes.            |

## Project Scope

### In Scope (MVP Features)

- User registration and login (email/password).
- Flashcard collection creation from pasted text or a single PDF (up to 5MB).
- Management of flashcard collections (view, edit, delete).
- A simple learning mode to review flashcards.
- Support for Polish, English, Spanish, and German.

### Out of Scope

- Advanced Spaced Repetition System (SRS) algorithms.
- Importing multiple files or other formats (e.g., DOCX, EPUB).
- Sharing collections between users.
- Mobile applications (the MVP is web-only).
- Gamification features like points or leaderboards.

## Project Status

**MVP (Minimum Viable Product)**

This project is currently in the MVP stage. The core functionalities are implemented and usable, but it is not yet a full-featured product. Future development will focus on expanding features, improving the learning algorithm, and enhancing the user experience.

## License

This project is licensed under the MIT License - see the `LICENSE.md` file for details.
