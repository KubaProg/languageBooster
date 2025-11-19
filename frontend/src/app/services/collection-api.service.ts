import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CollectionDetailsDto } from '../types/aliases';

@Injectable({
  providedIn: 'root'
})
export class CollectionApiService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080';

  getCollectionDetails(collectionId: string): Observable<CollectionDetailsDto> {
    return this.http.get<CollectionDetailsDto>(`${this.apiUrl}/api/v1/collections/${collectionId}`);
  }

  updateCardKnownStatus(cardId: string, known: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/api/v1/cards/${cardId}`, { known });
  }

  updateCardContent(cardId: string, front: string, back: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/api/v1/cards/${cardId}`, { front, back });
  }

  deleteCard(cardId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/api/v1/cards/${cardId}`);
  }

  resetCollection(collectionId: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/api/v1/collections/${collectionId}/reset`, {});
  }
}
