# LanguageBooster – PostgreSQL Schema (MVP) – wariant z `card.known`

## 1. Lista tabel z kolumnami, typami danych i ograniczeniami

### 1.1 `collection`

- `id` UUID **PK** `NOT NULL` `DEFAULT gen_random_uuid()`
- `owner_id` UUID **NOT NULL** — (Supabase Auth user id; FK logiczna po stronie aplikacji)
- `name` VARCHAR(120) **NOT NULL**
- `base_lang` VARCHAR(5) **NOT NULL**
- `target_lang` VARCHAR(5) **NOT NULL**
- `created_at` TIMESTAMPTZ **NOT NULL** `DEFAULT now()`

**Ograniczenia**

- `UNIQUE (owner_id, name)`
- `CHECK (base_lang <> target_lang)`
- `CHECK (base_lang IN ('pl','en','es','de') AND target_lang IN ('pl','en','es','de'))`

---

### 1.2 `card`

- `id` UUID **PK** `NOT NULL` `DEFAULT gen_random_uuid()`
- `collection_id` UUID **NOT NULL** **FK** → `collection(id)` `ON DELETE CASCADE`
- `front` TEXT **NOT NULL**
- `back` TEXT **NOT NULL**
- `known` BOOLEAN **NOT NULL** `DEFAULT false` — (status „Umiem” właściciela kolekcji)
- `updated_at` TIMESTAMPTZ **NOT NULL** `DEFAULT now()`
- `created_at` TIMESTAMPTZ **NOT NULL** `DEFAULT now()`

**Ograniczenia**

- brak dodatkowych (kolejność nie jest przechowywana)

---

## 2. Relacje między tabelami

- **Użytkownik (Supabase Auth)** 1 — N **collection**  
  (egzekwowane w backendzie: `collection.owner_id = auth.user.id`)
- **collection** 1 — N **card**  
  (`card.collection_id` → `collection.id`, `ON DELETE CASCADE`)

Kardynalności:

- `collection` : `card` = 1:N
- `user` : `collection` = 1:N

> Uwaga: Status nauki `known` jest per karta i **per właściciel** (bo karty należą do prywatnych kolekcji użytkownika). Brak współdzielenia w MVP.

---

## 3. Indeksy

> MVP: poza PK/UNIQUE brak dodatkowej indeksacji. Opcjonalnie można dodać później.

- **Z ograniczeń**:

  - `collection_pkey` na `collection(id)`
  - `card_pkey` na `card(id)`
  - `collection_owner_name_key` na `(owner_id, name)`

- **Opcjonalne (później)**:
  - `card_collection_id_idx` na `card(collection_id)`
  - `card_known_idx` na `card(collection_id, known)` — szybsza filtracja puli nauki

---

## 4. Zasady PostgreSQL (RLS)

- **MVP:** brak RLS; izolacja po stronie backendu (filtrowanie po `owner_id`).
- **Na przyszłość (opcjonalnie, jeśli klient miałby bezpośredni dostęp):**
  - `collection`: `USING (owner_id = auth.uid())`
  - `card`: `USING (collection_id IN (SELECT id FROM collection WHERE owner_id = auth.uid()))`

---

## 5. Dodatkowe uwagi / decyzje projektowe

- Brak utrwalania źródeł wejściowych (tekst/PDF); brak tabel `input_text`/`file_upload`.
- Kolejność fiszek nie jest przechowywana (brak `position`).
- Liczba fiszek w kolekcji liczona dynamicznie (`COUNT(*)`).
- UUID (v4/v7) jako PK; `VARCHAR(5)` dla kodów językowych — łatwa rozbudowa poza MVP.
- Kaskady: usunięcie kolekcji usuwa karty; status `known` jest częścią rekordu karty.

---
