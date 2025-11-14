import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CollectionResponseDto, FlashcardRequest } from '../../../types/aliases';

@Injectable({
  providedIn: 'root'
})
export class CollectionService {
  private apiUrl = '/api/v1/collections';
  private generateTextUrl = '/api/v1/generate/flashcards';
  private generateFileUrl = '/api/v1/generate/flashcards-from-file';

  constructor(private http: HttpClient) { }

  getCollections(): Observable<CollectionResponseDto[]> {
    return this.http.get<CollectionResponseDto[]>(this.apiUrl);
  }

  deleteCollection(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  generateFromText(request: FlashcardRequest): Observable<CollectionResponseDto> {
    return this.http.post<CollectionResponseDto>(this.generateTextUrl, request);
  }

  generateFromFile(formData: FormData): Observable<CollectionResponseDto> {
    return this.http.post<CollectionResponseDto>(this.generateFileUrl, formData);
  }
}
