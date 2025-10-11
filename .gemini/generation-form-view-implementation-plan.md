# View Implementation Plan: Generation Form View

## 1. Overview
This document outlines the implementation plan for the **Generation Form View**. This view provides a user interface for creating a new flashcard collection. Users can specify a collection name, select base and target languages, define the number of cards to generate, and provide the source material either as pasted text or an uploaded PDF file. The view is responsible for collecting and validating user input, handling the API request for collection creation, and managing loading and error states.

## 2. View Routing
- **Path**: `/app/collections/new`
- **Access**: This view is protected and requires an authenticated user session. It should be handled by an `AuthGuard`.

## 3. Component Structure
The view will be composed of a smart container component that manages state and several presentational child components for the form inputs.

```
GenerationFormComponent (Smart Component, View Route)
|
|-- MainLayoutComponent (Wrapper providing nav bar)
|
|-- <form> (Angular Reactive Form)
|   |
|   |-- TextInputComponent (for collection name)
|   |
|   |-- LanguageSelectorComponent (for base and target languages)
|   |
|   |-- NumberInputComponent (for number of cards)
|   |
|   |-- SourceInputComponent (for text/PDF source)
|   |
|   |-- ButtonComponent (Submit button)
|
|-- LoadingOverlayComponent (Displayed during API call)
```

## 4. Component Details

### `GenerationFormComponent`
- **Component description**: This is the main container for the view. It initializes and manages the reactive form (`FormGroup`), orchestrates validation, handles the form submission, interacts with the `CollectionService` to make the API call, and manages the view's state (e.g., loading, errors).
- **Main elements**: It will use a `<form>` tag with the `[formGroup]` directive. It will contain all the child form components and the main submit button. It will also conditionally render the `LoadingOverlayComponent`.
- **Handled interactions**:
  - `ngSubmit`: Triggered when the user clicks the "Generate" button. This method will orchestrate the API call.
- **Handled validation**:
  - It holds the root `FormGroup` and is responsible for checking the overall `form.valid` status to enable/disable the submit button.
- **Types**: `GenerationFormViewModel`, `CreateCollectionMetadata`, `CreateCollectionResponse`.
- **Props**: None. This is a routed view component.

### `LanguageSelectorComponent`
- **Component description**: A self-contained component with two dropdowns for selecting the base and target languages. It ensures that the selected languages are not the same.
- **Main elements**: Two `<select>` elements, each with associated `<label>`.
- **Handled interactions**:
  - `(change)`: When a user selects a language, it updates its internal state and emits the new language pair.
- **Handled validation**:
  - A custom validator will be attached to its `FormGroup` to ensure `baseLang !== targetLang`. An error message is shown if they are the same.
- **Types**: `Language = { code: string; name: string; }`. Emits `{ baseLang: string; targetLang: string; }`.
- **Props**: None. It will be implemented as a custom form control using `ControlValueAccessor`.

### `SourceInputComponent`
- **Component description**: A component with two tabs: "Paste Text" and "Upload PDF". It allows the user to provide the source material in one of the two formats.
- **Main elements**: Tab buttons, a `<textarea>` for text input, and an `<input type="file">` for the PDF upload.
- **Handled interactions**:
  - Tab switching: Toggles the visibility of the `textarea` and file input.
  - Text input: Updates the form value on input.
  - File selection: Updates the form value when a file is chosen.
- **Handled validation**:
  - Text input: `Validators.maxLength(10000)`.
  - File input: Checks if the file type is `application/pdf` and if the size is less than 5MB.
- **Types**: Emits `source: { type: 'text', value: string } | { type: 'file', value: File }`.
- **Props**: None. It will be implemented as a custom form control using `ControlValueAccessor`.

## 5. Types

### `CreateCollectionMetadata` (Request DTO)
This interface defines the shape of the `metadata` part for the `multipart/form-data` request.
```typescript
export interface CreateCollectionMetadata {
  name: string;
  baseLang: string;
  targetLang: string;
  cardsToGenerate: number;
}
```

### `CreateCollectionResponse` (Response DTO)
This interface defines the shape of the successful API response.
```typescript
export interface CreateCollectionResponse {
  id: string; // UUID
  name: string;
  baseLang: string;
  targetLang: string;
  createdAt: string; // ISO 8601 date string
  cardCount: number;
}
```

### `GenerationFormViewModel` (Form Group Structure)
This interface defines the structure of the Angular `FormGroup` used to manage the form's state and values.
```typescript
export interface GenerationFormViewModel {
  name: string;
  languages: {
    baseLang: string;
    targetLang: string;
  };
  cardsToGenerate: number;
  source: {
    type: 'text' | 'file';
    value: string | File;
  } | null;
}
```

## 6. State Management
State will be managed locally within the `GenerationFormComponent` using Angular Signals and Reactive Forms.

- **`generationForm: FormGroup`**: An instance of `FormGroup` will hold the entire form's state, including values and validation status for each field.
- **`isLoading = signal<boolean>(false)`**: A signal to control the visibility of the loading overlay during the API call.
- **`supportedLanguages`**: A constant array or signal holding the available languages (`{ code: 'en', name: 'English' }`, etc.) to populate the language selector dropdowns.

No external state management library or complex custom hooks are required for this view.

## 7. API Integration
Integration will be handled by a dedicated `CollectionService`.

- **Endpoint**: `POST /api/v1/collections/from-source`
- **Request Type**: `multipart/form-data`
- **Frontend Service Method**: `CollectionService.create(formValue: GenerationFormViewModel): Observable<CreateCollectionResponse>`
  - This method will construct a `FormData` object.
  - It will create the `CreateCollectionMetadata` from the `formValue`.
  - It will append the metadata: `formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }))`.
  - It will conditionally append the source: `formData.append('textSource', source.value)` or `formData.append('fileSource', source.value)`.
  - It will use Angular's `HttpClient` to post the `FormData`.

## 8. User Interactions
- **Filling the form**: As the user fills in the fields, reactive form validators provide immediate feedback on any invalid input.
- **Switching Source Type**: Clicking the tabs in the `SourceInputComponent` will toggle which input field (`textarea` or file input) is visible.
- **Submitting the Form**:
  - The "Generate" button is disabled until all validation conditions are met.
  - On click, the `onSubmit` method is triggered.
  - The `isLoading` signal is set to `true`, and the loading overlay is displayed.
  - The `CollectionService` is called.
  - On success, the user is redirected to `/app/collections` and a success toast is shown.
  - On error, the loading overlay is hidden, and an error toast is shown with the message from the API. The form data is preserved.

## 9. Conditions and Validation
- **Collection Name**: `Validators.required`. The field cannot be empty.
- **Languages**: A custom form group validator on `languages` will ensure `baseLang.value !== targetLang.value`.
- **Number of Cards**: `Validators.required`, `Validators.min(1)`, `Validators.max(100)`.
- **Source**: A custom validator on the root form group will check that the `source` control is not null (i.e., the user has provided either text or a file).
- **Text Source**: `Validators.maxLength(10000)`.
- **File Source**: The `SourceInputComponent` will internally validate that the selected file has a `.pdf` extension and its size is `<= 5 * 1024 * 1024` bytes.

## 10. Error Handling
- **Client-Side Validation Errors**: Messages will be displayed directly below the invalid form fields (e.g., "Name is required", "Number of cards must be between 1 and 100").
- **API Errors (`4xx`, `5xx`)**: The component's error handler for the API call will trigger a red `NotificationToast` component. The message displayed will be the error message from the API response body. The loading state will be reset to `false`, and the user can attempt to submit the form again.
- **Network Errors**: A generic "Network error, please try again" message will be shown in a red toast if the request fails to reach the server.

## 11. Implementation Steps
1.  Create the `GenerationFormComponent` as a standalone, routed component at `/app/collections/new`.
2.  Implement the `MainLayoutComponent` if it doesn't already exist, to provide the consistent navigation bar.
3.  Create the presentational `LanguageSelectorComponent` and `SourceInputComponent` as custom form controls implementing `ControlValueAccessor`.
4.  In `GenerationFormComponent`, initialize the `FormGroup` with all the fields defined in `GenerationFormViewModel` and attach all synchronous and asynchronous validators.
5.  Implement the HTML template for the form, binding the controls to the `FormGroup` instance. Use `@if` to conditionally show the loading overlay based on the `isLoading` signal.
6.  Create the `CollectionService` and implement the `create()` method, ensuring it correctly constructs the `multipart/form-data` payload.
7.  Implement the `onSubmit()` method in the component. It should call the service, subscribe to the result, and handle the success (redirect, success toast) and error (error toast) cases.
8.  Add the necessary types (`CreateCollectionMetadata`, etc.) to a shared types file.
9.  Write unit tests for the component, mocking the `CollectionService` and `Router` to verify that `onSubmit` behaves correctly in success and error scenarios.


