import { components } from './api.types';

export type CollectionResponseDto = components['schemas']['CollectionResponseDto'];
export type FlashcardRequest = components['schemas']['FlashcardRequest'];

export interface CardDto {
  id: string; // UUID in Java maps to string in TS
  front: string;
  back: string;
  known: boolean;
  createdAt: string; // OffsetDateTime in Java maps to string in TS (ISO 8601)
  updatedAt: string; // OffsetDateTime in Java maps to string in TS (ISO 8601)
}

export interface CollectionDetailsDto {
  id: string; // UUID in Java maps to string in TS
  name: string;
  baseLang: string;
  targetLang: string;
  createdAt: string; // OffsetDateTime in Java maps to string in TS (ISO 8601)
  cards: CardDto[];
}
