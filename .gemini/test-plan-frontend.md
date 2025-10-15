Plan Testów - LanguageBooster Frontend
1. Wprowadzenie i Cele Testowania
   1.1. Wprowadzenie

Ten dokument opisuje plan testów dla aplikacji frontendowej projektu LanguageBooster. Aplikacja została zbudowana przy użyciu frameworka Angular w wersji 17, z wykorzystaniem TypeScript oraz TailwindCSS do stylizacji. Frontend komunikuje się z backendem opartym na Spring Boot poprzez API REST. Celem planu jest systematyczne podejście do weryfikacji jakości interfejsu użytkownika, jego funkcjonalności, użyteczności oraz kompatybilności.
1.2. Cele Testowania

Główne cele testowania frontendu to:

    Weryfikacja funkcjonalna: Zapewnienie, że wszystkie elementy interfejsu użytkownika (formularze, przyciski, nawigacja) działają zgodnie z wymaganiami.

    Testowanie integracji z API: Sprawdzenie, czy frontend poprawnie wysyła żądania do backendu i obsługuje otrzymywane odpowiedzi (zarówno sukcesy, jak i błędy).

    Walidacja UI/UX: Upewnienie się, że aplikacja jest intuicyjna, responsywna i zgodna z projektem wizualnym.

    Testowanie kompatybilności: Sprawdzenie, czy aplikacja działa poprawnie na najpopularniejszych przeglądarkach internetowych.

    Zapewnienie wydajności: Ocena czasu ładowania aplikacji i responsywności interfejsu na akcje użytkownika.

2. Zakres Testów
   2.1. Funkcjonalności w Zakresie Testów

   Routing i nawigacja:

        Poprawne działanie wszystkich zdefiniowanych ścieżek (/app/collections, /app/collections/new).

        Przekierowania dla ścieżek domyślnych i nieistniejących.

        Ochrona ścieżek (AuthGuard) zostanie przetestowana po implementacji.

   Formularz generowania kolekcji:

        Walidacja pól formularza (pola wymagane, walidatory niestandardowe, np. languageMatchValidator).

        Interakcje z komponentami niestandardowymi (LanguageSelectorComponent, SourceInputComponent).

        Obsługa stanu ładowania (isLoading) podczas wysyłania formularza.

        Wyświetlanie komunikatów o błędach walidacji i błędach z API.

   Lista kolekcji:

        Uwaga: Testy zostaną przeprowadzone po zaimplementowaniu widoku, który obecnie jest placeholderem.

        Poprawne wyświetlanie listy kolekcji pobranych z API.

        Obsługa stanu, gdy użytkownik nie ma żadnych kolekcji.

   Komponenty współdzielone:

        Poprawne działanie i wygląd komponentu MainLayoutComponent.

        Działanie serwisu NotificationService.

2.2. Funkcjonalności Poza Zakresem Testów (MVP)

    Testowanie zaawansowanych funkcji ułatwień dostępu (WCAG AA).

    Testowanie na szerokiej gamie urządzeń mobilnych (testy ograniczone do trybu responsywnego w przeglądarkach desktopowych).

    Testowanie motywu ciemnego (darkMode w tailwind.config.js).

    Testowanie logowania i rejestracji (zostanie dodane po implementacji widoków).

3. Typy Testów

Testy frontendu będą obejmować różne poziomy w celu zapewnienia kompleksowej jakości.[1][2][3][4][5]
Typ Testu	Opis	Narzędzia	Odpowiedzialność
Testy Jednostkowe	Testowanie pojedynczych komponentów, serwisów i potoków (pipes) w izolacji.[1][5][6]	Jest	Deweloper
Testy Komponentów	Testowanie interakcji w obrębie komponentu, renderowania szablonu i komunikacji z jego zależnościami (np. serwisami).	Angular TestBed, Jasmine	Deweloper / Inżynier QA
Testy End-to-End (E2E)	Symulacja pełnych scenariuszy użytkownika w rzeczywistej przeglądarce, weryfikująca przepływ danych przez całą aplikację.[2][7][8]	Playwright (sugerowane)	Inżynier QA
Testy UI/UX	Manualna weryfikacja zgodności interfejsu z makietami, responsywności, intuicyjności i ogólnego doświadczenia użytkownika.	Przeglądarki (Chrome, Firefox)	Inżynier QA / UX Designer
Testy Kompatybilności	Sprawdzenie poprawnego działania i wyświetlania aplikacji na różnych przeglądarkach.	Chrome, Firefox, Safari (najnowsze wersje)	Inżynier QA
4. Scenariusze Testowe dla Kluczowych Funkcjonalności
   4.1. Tworzenie Nowej Kolekcji

   TC-F-GEN-01 (Pozytywny): Użytkownik wypełnia wszystkie pola formularza poprawnymi danymi, przesyła formularz i jest przekierowywany na listę kolekcji z komunikatem o sukcesie.

   TC-F-GEN-02 (Negatywny - Walidacja): Próba wysłania formularza z pustymi polami wymaganymi skutkuje wyświetleniem odpowiednich komunikatów o błędach pod polami.

   TC-F-GEN-03 (Negatywny - Walidacja Języków): Wybranie tego samego języka jako bazowy i docelowy powoduje wyświetlenie błędu walidacji.

   TC-F-GEN-04 (Interakcja - Stan Ładowania): Po kliknięciu przycisku "Generate", przycisk staje się nieaktywny, a na ekranie pojawia się wskaźnik ładowania.

   TC-F-GEN-05 (Interakcja - Przełączanie Źródła): Użytkownik może swobodnie przełączać się między zakładkami "Paste Text" i "Upload PDF", a wpisane dane są poprawnie obsługiwane.

   TC-F-GEN-06 (Obsługa Błędu API): Gdy backend zwróci błąd podczas generowania, stan ładowania jest wyłączany, a użytkownikowi wyświetlany jest komunikat o błędzie (np. "A collection with the same name already exists").

4.2. Nawigacja

    TC-F-NAV-01 (Pozytywny): Kliknięcie linków w MainLayoutComponent ("My Collections", "Create New") poprawnie przenosi użytkownika do odpowiednich widoków.

    TC-F-NAV-02 (Przekierowanie): Wejście na główny adres aplikacji (/) powinno przekierować użytkownika do /app, a następnie do /app/collections/new.

5. Środowisko Testowe

   Lokalne: Maszyny deweloperskie z uruchomionym serwerem ng serve. Testy jednostkowe uruchamiane za pomocą npm test. Backend mockowany lub uruchamiany lokalnie.

   CI/CD (Staging): Środowisko, na którym aplikacja frontendowa jest budowana i serwowana. Na tym środowisku uruchamiane będą testy E2E, które będą komunikować się z backendem wdrożonym w tym samym środowisku.

   Produkcyjne: Środowisko dostępne dla użytkowników końcowych. Po wdrożeniu przeprowadzane będą na nim manualne testy dymne (smoke tests).

6. Narzędzia do Testowania
   Kategoria	Narzędzie	Zastosowanie
   Framework Testowy	Jest	Uruchamianie i pisanie testów jednostkowych dla Angulara.[9]
   Testy Komponentów	Angular TestBed	Izolowanie i testowanie komponentów w kontrolowanym środowisku.
   Mockowanie HTTP	HttpClientTestingModule	Przechwytywanie i mockowanie żądań HTTP w testach.[1]
   Testy E2E	 Playwright	Automatyzacja scenariuszy użytkownika w przeglądarce.[1][7][10]
   Linting	ESLint	Statyczna analiza kodu w celu zapewnienia jego jakości i spójności.
   CI/CD	GitHub Actions (lub inne)	Automatyzacja budowania, testowania i wdrażania aplikacji.
   Zarządzanie Błędami	Jira (lub inne)	Centralne miejsce do raportowania i śledzenia błędów.
7. Harmonogram Testów

Harmonogram testów frontendu jest zsynchronizowany z cyklem rozwoju backendu i pracami w metodyce zwinnej.

    Podczas Sprintu:

        Deweloperzy implementują testy jednostkowe i komponentowe dla tworzonych elementów.

        Inżynier QA tworzy skrypty E2E dla gotowych i stabilnych przepływów użytkownika.

    Faza Integracji (przed końcem sprintu):

        Manualne testy eksploracyjne na środowisku Staging w celu znalezienia nieoczywistych błędów.

        Uruchomienie pełnej regresji testów E2E.

    Przed Wdrożeniem:

        Testy kompatybilności na kluczowych przeglądarkach.

        Finalna weryfikacja przez Product Ownera (UAT).

8. Kryteria Akceptacji Testów
   8.1. Kryteria Wejścia

   Aplikacja została pomyślnie zbudowana i wdrożona na środowisku testowym.

   Wszystkie testy jednostkowe i komponentowe przechodzą.

   Backend jest dostępny na środowisku testowym i jego API jest stabilne.

8.2. Kryteria Wyjścia

    100% krytycznych ścieżek użytkownika (np. tworzenie kolekcji) zostało przetestowanych (manualnie i/lub automatycznie) i działa poprawnie.

    Brak otwartych błędów krytycznych lub blokujących w interfejsie użytkownika.

    Aplikacja jest w pełni responsywna i poprawnie wyświetla się na docelowych rozdzielczościach (desktop, tablet, mobile).

    Aplikacja działa poprawnie na najnowszych wersjach przeglądarek Chrome i Firefox.

9. Role i Odpowiedzialności
   Rola	Odpowiedzialność
   Deweloper Frontend	- Tworzenie i utrzymanie testów jednostkowych i komponentowych.<br>- Dbanie o jakość kodu i zgodność ze standardami (linting).<br>- Naprawa błędów zgłoszonych przez QA.
   Inżynier QA	- Projektowanie scenariuszy testowych (manualnych i E2E).<br>- Implementacja i utrzymanie testów E2E.<br>- Przeprowadzanie testów manualnych, regresji, kompatybilności.<br>- Raportowanie błędów.
   UX/UI Designer	- Weryfikacja zgodności wizualnej aplikacji z projektem.<br>- Ocena użyteczności i intuicyjności interfejsu.
   Product Owner	- Zatwierdzanie funkcjonalności w ramach testów akceptacyjnych (UAT).<br>- Priorytetyzacja zadań i błędów w backlogu.
10. Procedury Raportowania Błędów

Procedura raportowania błędów jest spójna z tą dla backendu. Wszystkie błędy będą zgłaszane w Jirze z następującymi informacjami:

    Tytuł: Zwięzły opis błędu, np. "Formularz generowania: Błąd walidacji dla identycznych języków nie jest wyświetlany".

    Projekt/Komponent: LanguageBooster / Frontend.

    Środowisko: Wersja aplikacji, przeglądarka (nazwa i wersja), system operacyjny, rozdzielczość ekranu.

    Kroki do odtworzenia: Dokładny opis czynności prowadzących do błędu.

    Oczekiwany rezultat: Co powinno się stać.

    Rzeczywisty rezultat: Co się stało.

    Priorytet/Waga: (Krytyczny, Wysoki, Średni, Niski).

    Załączniki: Zrzuty ekranu, nagrania wideo, komunikaty z konsoli przeglądarki.