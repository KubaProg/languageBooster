export interface CreateCollectionMetadata {
  name: string;
  baseLang: string;
  targetLang: string;
  cardsToGenerate: number;
}

export interface CreateCollectionResponse {
  id: string; // UUID
  name: string;
  baseLang: string;
  targetLang: string;
  createdAt: string; // ISO 8601 date string
  cardCount: number;
}

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
