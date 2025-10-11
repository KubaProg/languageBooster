import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs';

import { LanguageSelectorComponent } from '../../shared/components/language-selector/language-selector.component';
import { SourceInputComponent } from '../../shared/components/source-input/source-input.component';
import { languageMatchValidator } from './validators/custom-validators';
import { CollectionService } from '../collection/services/collection.service';
import { NotificationService } from '../../shared/services/notification.service';

@Component({
  selector: 'app-generation-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    LanguageSelectorComponent,
    SourceInputComponent
  ],
  templateUrl: './generation-form.component.html',
  styleUrls: ['./generation-form.component.scss']
})
export class GenerationFormComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private collectionService = inject(CollectionService);
  private notificationService = inject(NotificationService);

  public isLoading = signal<boolean>(false);
  public generationForm: FormGroup;

  constructor() {
    this.generationForm = this.fb.group({
      name: ['', [Validators.required]],
      languages: [null, [Validators.required, languageMatchValidator]],
      cardsToGenerate: [10, [Validators.required, Validators.min(1), Validators.max(100)]],
      source: [null, [Validators.required]]
    });
  }

  public onSubmit(): void {
    if (this.generationForm.invalid) {
      this.generationForm.markAllAsTouched(); // Mark all fields as touched to show validation errors
      return;
    }

    this.isLoading.set(true);

    this.collectionService.create(this.generationForm.getRawValue())
      .pipe(
        finalize(() => this.isLoading.set(false))
      )
      .subscribe({
        next: (response) => {
          this.notificationService.show('success', 'Collection created successfully!');
          this.router.navigate(['/app/collections']);
        },
        error: (err: HttpErrorResponse) => {
          const errorMessage = err.error?.message || 'An unexpected error occurred.';
          this.notificationService.show('error', errorMessage);
        }
      });
  }
}

