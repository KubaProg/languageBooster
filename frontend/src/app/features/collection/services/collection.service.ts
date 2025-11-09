import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CollectionResponseDto, FlashcardRequest } from '../../../types/aliases';

@Injectable({
  providedIn: 'root'
})
export class CollectionService {
  private apiUrl = '/api/v1/collections';
  private generateUrl = '/api/v1/generate/flashcards';

  constructor(private http: HttpClient) { }

  getCollections(): Observable<CollectionResponseDto[]> {
    return this.http.get<CollectionResponseDto[]>(this.apiUrl);
  }

  deleteCollection(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  generateFromText(request: FlashcardRequest): Observable<CollectionResponseDto> {
    return this.http.post<CollectionResponseDto>(this.generateUrl, request);
  }

  create(formValue: any): Observable<CollectionResponseDto> {
    // Assuming 'create' is a POST request to the collections endpoint
    return this.http.post<CollectionResponseDto>(this.apiUrl, formValue);
  }
}
