Plan Testów - LanguageBooster Backend
1. Wprowadzenie i Cele Testowania
   1.1. Wprowadzenie

Niniejszy dokument stanowi kompleksowy plan testów dla komponentu backendowego aplikacji LanguageBooster. Projekt jest aplikacją webową opartą na architekturze mikroserwisów, z backendem napisanym w języku Java z wykorzystaniem frameworka Spring Boot. Celem aplikacji jest generowanie spersonalizowanych fiszek językowych przy użyciu zewnętrznego API (OpenRouter). Plan ten określa strategię, zakres, zasoby i harmonogram działań testowych mających na celu zapewnienie jakości, stabilności i bezpieczeństwa aplikacji.
1.2. Cele Testowania

Główne cele procesu testowego dla backendu to:

    Weryfikacja funkcjonalności: Upewnienie się, że wszystkie kluczowe funkcje, w szczególności generowanie kolekcji fiszek, działają zgodnie ze specyfikacją.

    Zapewnienie niezawodności: Sprawdzenie stabilności aplikacji pod obciążeniem oraz jej zdolności do poprawnego obsługiwania błędów, w tym błędów pochodzących z usług zewnętrznych.

    Weryfikacja integracji: Potwierdzenie, że integracja z bazą danych PostgreSQL oraz zewnętrznym API OpenRouter działa poprawnie.

    Zapewnienie bezpieczeństwa: Identyfikacja i eliminacja potencjalnych luk bezpieczeństwa, zwłaszcza w kontekście autoryzacji i dostępu do danych użytkowników.

    Walidacja logiki biznesowej: Sprawdzenie poprawności logiki związanej z tworzeniem i zarządzaniem kolekcjami oraz fiszkami.

2. Zakres Testów
   2.1. Funkcjonalności w Zakresie Testów

   API generowania fiszek:

        Generowanie kolekcji na podstawie wklejonego tekstu.

        Poprawność zapisu wygenerowanych kolekcji i fiszek w bazie danych.

        Asynchroniczna obsługa procesu generowania.

   API zarządzania kolekcjami:

        Pobieranie listy kolekcji dla uwierzytelnionego użytkownika.

        Uwaga: Pozostałe operacje CRUD (Update, Delete) zostaną objęte testami po ich implementacji.

   Obsługa błędów:

        Obsługa błędów komunikacji z API OpenRouter (np. timeout, statusy 4xx/5xx).

        Obsługa błędów związanych z przetwarzaniem odpowiedzi z API (np. nieprawidłowy format JSON).

        Obsługa błędów bazy danych (np. naruszenie ograniczeń UNIQUE).

   Bezpieczeństwo:

        Uwaga: Testy bezpieczeństwa zostaną w pełni zaimplementowane po ukończeniu integracji Spring Security z Supabase. Obecny stan (permitAll()) zostanie oznaczony jako krytyczne ryzyko.

        Weryfikacja izolacji danych pomiędzy różnymi użytkownikami.

2.2. Funkcjonalności Poza Zakresem Testów (MVP)

    Zaawansowane testy wydajnościowe i obciążeniowe.

    Testy importu plików PDF (zostaną dodane po implementacji odpowiedniego endpointu).

    Testy funkcjonalności, które są obecnie zamockowane lub niezaimplementowane (np. pełny CRUD na kolekcjach).

    Testowanie samego API OpenRouter – zakładamy jego poprawność, skupiając się na integracji.

3. Typy Testów

Proces testowy zostanie zrealizowany na kilku poziomach, zgodnie z piramidą testów:
Typ Testu	Opis	Narzędzia	Odpowiedzialność
Testy Jednostkowe	Weryfikacja pojedynczych klas i metod w izolacji (np. logika serwisów, mappery DTO). Zależności (repozytoria, klienci API) będą mockowane.	JUnit 5, Mockito	Deweloper
Testy Integracyjne	Testowanie współpracy komponentów w ramach kontekstu Springa. Obejmuje to testy warstwy webowej (@WebMvcTest) oraz warstwy dostępu do danych (@DataJpaTest).	Spring Test, MockMvc, H2/Testcontainers	Deweloper / Inżynier QA
Testy API (E2E)	Testowanie publicznego API z perspektywy klienta. Weryfikacja pełnych przepływów biznesowych, od żądania HTTP do zapisu w bazie danych.	Postman, Newman, REST Assured	Inżynier QA
Testy Bezpieczeństwa	Weryfikacja mechanizmów uwierzytelniania i autoryzacji, testowanie podatności na podstawowe ataki (np. próba dostępu do cudzych zasobów).	Postman (z użyciem JWT), OWASP ZAP (opcjonalnie)	Inżynier QA
4. Scenariusze Testowe dla Kluczowych Funkcjonalności
   4.1. Generowanie Kolekcji Fiszki

   TC-GEN-01 (Pozytywny): Pomyślne wygenerowanie kolekcji fiszek na podstawie poprawnego tekstu i zwrócenie statusu 200 OK wraz z danymi nowej kolekcji.

   TC-GEN-02 (Negatywny - Błąd API): Symulacja błędu 500 z API OpenRouter i weryfikacja, czy backend zwraca odpowiedni status błędu (np. 502 Bad Gateway).

   TC-GEN-03 (Negatywny - Timeout): Symulacja przekroczenia czasu oczekiwania na odpowiedź z API OpenRouter i weryfikacja, czy backend zwraca błąd 504 Gateway Timeout.

   TC-GEN-04 (Negatywny - Nieprawidłowy JSON): Symulacja odpowiedzi z API OpenRouter z nieprawidłowym formatem JSON i weryfikacja, czy błąd jest poprawnie obsługiwany i logowany.

   TC-GEN-05 (Walidacja Danych): Sprawdzenie, czy dane zapisane w tabelach collection i card są zgodne z odpowiedzią otrzymaną z API.

4.2. Zarządzanie Kolekcjami

    TC-COLL-01 (Pozytywny): Pomyślne pobranie listy kolekcji dla uwierzytelnionego użytkownika (po implementacji autentykacji).

    TC-COLL-02 (Pozytywny - Pusta Lista): Weryfikacja, czy dla nowego użytkownika bez kolekcji API zwraca pustą listę i status 200 OK.

    TC-COLL-03 (Bezpieczeństwo): Próba pobrania kolekcji należących do innego użytkownika powinna zakończyć się niepowodzeniem (np. zwróceniem pustej listy lub błędem 404).

4.3. Bezpieczeństwo

    TC-SEC-01 (Dostęp Nieautoryzowany): Próba dostępu do chronionych endpointów bez tokenu JWT powinna zwrócić błąd 401 Unauthorized (po implementacji zabezpieczeń).

    TC-SEC-02 (Dostęp z Nieprawidłowym Tokenem): Próba dostępu z nieprawidłowym lub wygasłym tokenem JWT powinna zwrócić błąd 401 Unauthorized.

    TC-SEC-03 (Izolacja Danych): Użytkownik A nie może uzyskać dostępu do zasobów (kolekcji, fiszek) użytkownika B poprzez bezpośrednie wywołanie API z poprawnym ID zasobu.

5. Środowisko Testowe

   Lokalne: Maszyny deweloperskie z wykorzystaniem Docker Compose do uruchomienia bazy danych PostgreSQL oraz H2 do szybkich testów integracyjnych.

   CI/CD (Staging): Dedykowane środowisko uruchamiane w ramach pipeline'u CI/CD (np. GitHub Actions), na którym będą automatycznie uruchamiane testy jednostkowe, integracyjne i API. Baza danych będzie czyszczona przed każdym przebiegiem testów.

   Produkcyjne: Środowisko produkcyjne, na którym po wdrożeniu zostaną przeprowadzone testy dymne (smoke tests).

6. Narzędzia do Testowania
   Kategoria	Narzędzie	Zastosowanie
   Framework Testowy	JUnit 5	Podstawowy framework do pisania testów w Javie.
   Mockowanie	Mockito	Tworzenie mocków i zaślepek dla zależności w testach jednostkowych.
   Testy Spring	Spring Test, MockMvc	Testowanie integracyjne i warstwy webowej aplikacji Spring Boot.
   Baza Danych w Testach	H2, Testcontainers	Lekka baza danych w pamięci do testów integracyjnych oraz konteneryzacja bazy PostgreSQL.
   Testy API	Postman, Newman	Manualne i zautomatyzowane testowanie API REST.
   CI/CD	GitHub Actions (lub inne)	Automatyzacja budowania, testowania i wdrażania aplikacji.
   Zarządzanie Błędami	Jira (lub inne)	Rejestrowanie, śledzenie i zarządzanie zgłoszonymi błędami.
7. Harmonogram Testów

Proces testowy będzie prowadzony równolegle z procesem deweloperskim w podejściu zwinnym.

    Sprint 1-N (Development):

        Deweloperzy piszą testy jednostkowe i integracyjne dla każdej nowej funkcjonaljonalności.

        Inżynier QA przygotowuje scenariusze i automatyzuje testy API dla ukończonych endpointów.

    Koniec Sprintu (Code Freeze):

        Uruchomienie pełnej regresji zautomatyzowanych testów API na środowisku Staging.

        Przeprowadzenie testów eksploracyjnych i testów bezpieczeństwa.

    Wdrożenie na Produkcję:

        Po pomyślnym zakończeniu testów na Stagingu.

        Wykonanie testów dymnych (smoke tests) na środowisku produkcyjnym.

8. Kryteria Akceptacji Testów
   8.1. Kryteria Wejścia (Rozpoczęcia Testów)

   Kod źródłowy został pomyślnie skompilowany i wdrożony na środowisku testowym.

   Wszystkie testy jednostkowe napisane przez deweloperów przechodzą pomyślnie.

   Dostępna jest dokumentacja API (np. Swagger) dla testowanych endpointów.

8.2. Kryteria Wyjścia (Zakończenia Testów)

    100% zdefiniowanych scenariuszy testowych zostało wykonanych.

    95% testów automatycznych kończy się sukcesem.

    Brak otwartych błędów krytycznych lub blokujących.

    Wszystkie błędy o wysokim priorytecie zostały naprawione i retestowane.

    Pokrycie kodu testami jednostkowymi i integracyjnymi dla nowej logiki biznesowej wynosi co najmniej 70%.

9. Role i Odpowiedzialności
   Rola	Odpowiedzialność
   Deweloper	- Tworzenie i utrzymanie testów jednostkowych i integracyjnych.<br>- Naprawa błędów zgłoszonych przez zespół QA.<br>- Uczestnictwo w analizie przyczyn źródłowych błędów.
   Inżynier QA	- Projektowanie, tworzenie i utrzymanie planu testów.<br>- Tworzenie i automatyzacja testów API (E2E).<br>- Wykonywanie testów manualnych, eksploracyjnych i regresji.<br>- Raportowanie i zarządzanie cyklem życia błędów.
   Product Owner	- Definiowanie kryteriów akceptacji dla funkcjonalności.<br>- Uczestnictwo w testach akceptacyjnych użytkownika (UAT).<br>- Priorytetyzacja naprawy błędów.
10. Procedury Raportowania Błędów

Wszystkie wykryte błędy będą raportowane w systemie Jira zgodnie z poniższym szablonem:

    Tytuł: Zwięzły i jednoznaczny opis problemu.

    Projekt/Komponent: LanguageBooster / Backend.

    Środowisko: Wersja aplikacji, przeglądarka, system operacyjny (jeśli dotyczy).

    Kroki do odtworzenia: Szczegółowa, numerowana lista kroków prowadzących do wystąpienia błędu.

    Oczekiwany rezultat: Opis, jak aplikacja powinna się zachować.

    Rzeczywisty rezultat: Opis, co faktycznie się stało.

    Priorytet/Waga:

        Krytyczny (Critical): Blokuje działanie kluczowych funkcji, brak obejścia.

        Wysoki (High): Poważnie ogranicza funkcjonalność, ale istnieje obejście.

        Średni (Medium): Powoduje nieprawidłowe działanie, ale nie jest to kluczowa funkcja.

        Niski (Low): Drobny błąd kosmetyczny lub literówka.

    Załączniki: Zrzuty ekranu, logi, pliki HAR, przykładowe dane wejściowe (payload).