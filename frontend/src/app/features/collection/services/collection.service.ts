import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateCollectionMetadata, CreateCollectionResponse, GenerationFormViewModel } from '../../generation-form/types/generation-form.types';

@Injectable({
  providedIn: 'root'
})
export class CollectionService {
  private http = inject(HttpClient);
  private collectionsApiUrl = '/api/v1/collections';
  private generationApiUrl = '/api/v1/generate';

  public generateFromText(name: string, text: string, sourceLang: string, targetLang: string): Observable<CreateCollectionResponse> {
    const payload = { name, text, sourceLang, targetLang };
    return this.http.post<CreateCollectionResponse>(`${this.generationApiUrl}/flashcards`, payload);
  }

  public create(formValue: GenerationFormViewModel): Observable<CreateCollectionResponse> {
    const formData = new FormData();

    const metadata: CreateCollectionMetadata = {
      name: formValue.name,
      baseLang: formValue.languages.baseLang,
      targetLang: formValue.languages.targetLang,
      cardsToGenerate: formValue.cardsToGenerate
    };

    formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));

    if (formValue.source) {
      if (formValue.source.type === 'text') {
        formData.append('textSource', formValue.source.value as string);
      } else if (formValue.source.type === 'file' && formValue.source.value instanceof File) {
        formData.append('fileSource', formValue.source.value, formValue.source.value.name);
      }
    }

    return this.http.post<CreateCollectionResponse>(`${this.collectionsApiUrl}/from-source`, formData);
  }
}