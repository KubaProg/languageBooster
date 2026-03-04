# PrimeNG Migration & Learning Action Plan

This plan outlines the steps to replace your custom components with PrimeNG equivalents. This is designed to help you get hands-on experience with the library before joining your new project.

---

## Phase 1: Installation & Infrastructure

PrimeNG requires the library itself plus `primeicons` for its UI elements.

1.  **Install Dependencies:**
    ```bash
    npm install primeng@17.18.12 primeicons @angular/cdk@17.3.0
    ```

2.  **Configure Styles:**
    Add the PrimeNG theme and core styles to your `angular.json` (under `styles` array):
    ```json
    "styles": [
      "node_modules/primeng/resources/themes/lara-light-blue/theme.css",
      "node_modules/primeng/resources/primeng.min.css",
      "node_modules/primeicons/primeicons.css",
      "src/styles.scss"
    ]
    ```

3.  **Tailwind Integration:**
    PrimeNG components often have their own padding/margins. To ensure Tailwind doesn't conflict or to use them together, ensure your `tailwind.config.js` is aware of PrimeNG if you plan to use [PrimeNG's Tailwind-based presets](https://primeng.org/tailwind). For now, standard usage is fine.

---

## Phase 2: Global Services Setup

PrimeNG excels at service-driven UI components like Toasts and Dialogs.

1.  **Update `app.config.ts`:**
    Provide `MessageService` and `ConfirmationService` globally.
    ```typescript
    import { MessageService, ConfirmationService } from 'primeng/api';
    // ... in providers:
    providers: [MessageService, ConfirmationService, ...]
    ```

2.  **Update `app.component.ts`:**
    Import the required PrimeNG modules in the `imports` array of your standalone component.
    ```typescript
    import { ToastModule } from 'primeng/toast';
    import { ConfirmDialogModule } from 'primeng/confirmdialog';

    @Component({
      // ...
      standalone: true,
      imports: [RouterOutlet, ToastModule, ConfirmDialogModule],
      // ...
    })
    ```

3.  **Update `app.component.html`:**
    Add the global containers for notifications and confirmation dialogs at the root level.
    ```html
    <p-toast></p-toast>
    <p-confirmDialog></p-confirmDialog>
    <router-outlet></router-outlet>
    ```

---

## Phase 3: Component Migration (Step-by-Step)

### Task 1: Replace `ConfirmationModalComponent`
This is your "Quick Win". PrimeNG handles this via a service, so you can delete your custom component entirely.

*   **Current:** `src/app/shared/components/confirmation-modal/`
*   **PrimeNG Replacement:** `ConfirmationService` + `p-confirmDialog`.
*   **Action:** 
    1.  Inject `ConfirmationService` into components that currently use your modal.
    2.  Replace the `isVisible = true` logic with `this.confirmationService.confirm({ message: '...', accept: () => { ... } })`.
    3.  Delete your custom modal folder.

### Task 2: Refactor `LanguageSelector`
*   **Current:** Custom dropdown/select logic.
*   **PrimeNG Replacement:** `p-dropdown`.
*   **Action:**
    1.  Import `DropdownModule`.
    2.  Use `<p-dropdown [options]="languages" [(ngModel)]="selectedLang" optionLabel="name"></p-dropdown>`.
    3.  Note how PrimeNG handles the "overlay" positioning automatically.

### Task 3: Refactor `CollectionCard`
*   **Current:** Manual Tailwind classes for shadows and hover effects.
*   **PrimeNG Replacement:** `p-card`.
*   **Action:**
    1.  Import `CardModule`.
    2.  Wrap your content in `<p-card [header]="collection.name">`.
    3.  Use the `header` and `footer` templates for better structure.

### Task 4: Refactor `GenerationForm`
*   **Current:** Standard HTML inputs.
*   **PrimeNG Replacement:** `p-inputText`, `p-inputNumber`, `p-button`.
*   **Action:**
    1.  Replace `<button>` with `<p-button label="Generate" icon="pi pi-check"></p-button>`.
    2.  Add `pInputText` directive to your text inputs.
    3.  Observe how `ng-invalid` classes from Angular Forms automatically trigger PrimeNG's error styling.

---

## Phase 4: Exploration & Advanced Features

Once the basics are done, try these to level up:

1.  **Data Loading:** Replace your collection list with `p-dataView` or `p-table`. This will teach you about templating (`pTemplate`).
2.  **Theming:** Try changing the theme in `angular.json` to `lara-dark-blue` or `soho-light` to see how the whole UI adapts.
3.  **Icons:** Replace all your custom SVGs (like the delete trash can) with PrimeIcons: `<i class="pi pi-trash"></i>`.

---

## Useful Resources
- [PrimeNG Documentation](https://primeng.org/setup)
- [PrimeIcons List](https://primeng.org/icons)
- [PrimeFlex (Optional)](https://primeflex.org/) - Prime's alternative to Tailwind (good to know it exists, though you are using Tailwind).
