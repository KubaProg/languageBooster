
# Dokument wymagań produktu (PRD) - LanguageBooster

## 1. Przegląd produktu

LanguageBooster to webowa aplikacja (MVP), która pozwala użytkownikom tworzyć spersonalizowane zestawy fiszek językowych na podstawie ich własnych materiałów: wklejonego tekstu lub pojedynczego pliku PDF (w tym skanów/obrazów). Aplikacja automatycznie rozpoznaje język materiału wejściowego, użytkownik wybiera język bazowy (na froncie fiszki) oraz język docelowy. Generowanie realizowane jest przez model AI, a nauka opiera się na prostym stanie „Umiem/Nie umiem”.

Zakres języków w MVP: polski, angielski, hiszpański, niemiecki – w dowolnych kombinacjach kierunków.

## 2. Problem użytkownika

Tworzenie własnych, dopasowanych do treści użytkownika materiałów do nauki języka (np. z rozdziału książki, artykułu, notatek) jest czasochłonne i rozproszone. Brakuje prostego narzędzia, które w kilku krokach przekształci prywatne materiały w kompletne, krótkie fiszki z kontekstem zdaniowym i umożliwi natychmiastową naukę.

## 3. Wymagania funkcjonalne

FR-01 Autodetekcja języka materiału wejściowego  
• System automatycznie wykrywa język wklejonego tekstu lub wgranego PDF.  
• Użytkownik wybiera język bazowy (front fiszki) oraz język docelowy (back).

FR-02 Wejścia danych: tekst i PDF  
• Wsparcie dla wklejenia czystego tekstu.  
• Wsparcie dla pojedynczego pliku PDF (w tym skany/obrazy) do 5 MB.  
• Brak dodatkowych komunikatów/ostrzeżeń jakości wejścia w MVP.

FR-03 Generowanie fiszek przez AI  
• Użytkownik określa liczbę fiszek do wygenerowania (1–100).  
• Każda fiszka jest pełnym zdaniem o długości maks. 8 słów (może być mniej), zawierającym wyrazy w naturalnym kontekście.  
• Fiszki nie zawierają nazw własnych.  
• Front fiszki zawsze prezentowany w języku bazowym; back w języku docelowym.  
• Po zakończeniu generowania użytkownik jest przenoszony do listy kolekcji.

FR-04 Zarządzanie kolekcjami  
• Lista kolekcji po zalogowaniu (nazwa, para językowa, data).  
• Tworzenie nowej kolekcji z poziomu listy (tekst/PDF, wybór języków, ilość fiszek).  
• Edycja kolekcji: wejście przez ikonę edycji; edytowalne pola fiszek; zapis po potwierdzeniu.  
• Usuwanie pojedynczej fiszki (ikona X) powoduje przejście do kolejnej.  
• Brak duplikacji/wersjonowania kolekcji w MVP.

FR-05 Nauka i powtórki (prosty model)  
• W widoku nauki pierwsza fiszka jest gotowa do pracy po wejściu do kolekcji.  
• Dwa stany odpowiedzi: „Umiem” (ukryj na zawsze) i „Nie umiem” (pozostaw w puli).  
• Brak resetu postępu w MVP (planowane później).

FR-06 Obsługa błędów generacji  
• W razie niepowodzenia generowania system zadaje pytanie o ponowienie operacji.  
• Brak automatycznych limitów prób poza pojedynczym ponowieniem z poziomu UI.

FR-07 Uwierzytelnianie i dostęp  
• Logowanie i rejestracja przez email+hasło (Supabase).  
• Każdy użytkownik widzi wyłącznie swoje kolekcje i postęp.

FR-08 Ograniczenia operacyjne  
• Maksymalnie jeden plik PDF na generowanie.  
• Brak formalnych limitów dziennych/abonamentowych w MVP; jedyny limit to 100 fiszek na żądanie.  
• Brak walidacji treści po stronie systemu w MVP.

FR-09 Nawigacja i UI  
• Główny widok: lista kolekcji z akcjami „Nowa kolekcja”,  ikona "Usuń zestaw fiszek”.  
• Ekran wewnątrz zestawu fiszek: Widzimy przód, po kliknięciu w obszar fiszki tył, przyciski umiem nie umiem, a rowniez ikona do usuniecia fiszki lub edycji (włącza tryb edycji) , rownież ikona do zatwierdzenia zmian jeśli w trybie edycji.
• Ekran generowania: wybór wejścia (tekst/PDF), język bazowy, język docelowy, liczba fiszek, przycisk „Generuj”.  
• Ekran ładowania z prostą animacją podczas generacji.

FR-10 Bezpieczeństwo i prywatność (minimalne MVP)  
• Dane kont i sesji zarządzane przez Supabase.  
• Brak polityki retencji/eksportu danych w MVP (do doprecyzowania po MVP).

Wymagania niefunkcjonalne (NFR – minimalny zakres MVP)  
• Web only (desktop-first responsywnie), bez aplikacji mobilnej.  
• Czas odpowiedzi interfejsu powinien umożliwić płynne przechodzenie między ekranami; brak gwarancji czasów generowania.  
• Język interfejsu: co najmniej polski (rozszerzenia po MVP).

## 4. Granice produktu

Poza zakresem MVP (eksplicytnie):  
• Zaawansowane algorytmy SRS i ustawienia powtórek.  
• Import innych formatów niż PDF i czysty tekst; import wielu plików PDF jednocześnie.  
• Współdzielenie kolekcji między użytkownikami.  
• Integracje z zewnętrznymi platformami edukacyjnymi.  
• Aplikacje mobilne (Android/iOS).  
• Inne formy nauki niż fiszki (słuchanie, uzupełnienia, ćwiczenia gramatyczne).  
• System punktów, rankingi i elementy społecznościowe.  
• Zaawansowana analityka/metryki jakości (poza oceną manualną).  
• Reset postępu nauki (planowane po MVP).  
• Walidacje jakości treści, OCR i filtrowanie nazw własnych poza regułami generatora (MVP ogranicza się do promptu dla AI).

## 5. Historyjki użytkowników

US-001 Rejestracja konta  
Opis: Jako nowy użytkownik chcę utworzyć konto przy użyciu emaila i hasła, aby móc tworzyć i przechowywać moje kolekcje.  
Kryteria akceptacji:  
• Formularz rejestracji przyjmuje email oraz hasło i tworzy konto w Supabase.  
• Po rejestracji użytkownik jest przekierowany do logowania.  
• Walidacja podstawowa pól (format email, minimalna długość hasła).

US-002 Logowanie i wylogowanie  
Opis: Jako użytkownik chcę zalogować się i wylogować, aby bezpiecznie uzyskiwać dostęp do moich kolekcji.  
Kryteria akceptacji:  
• Logowanie przez Supabase (email+hasło).  
• Po zalogowaniu widoczna jest lista kolekcji użytkownika.  
• Wylogowanie kończy sesję i ukrywa dane użytkownika.

US-003 Przegląd listy kolekcji  
Opis: Jako użytkownik chcę zobaczyć listę moich kolekcji po zalogowaniu, aby szybko rozpocząć naukę lub edycję.  
Kryteria akceptacji:  
• Lista pokazuje nazwę, parę językową i datę utworzenia.  
• Akcje: „Nowa kolekcja”, „Edycja” (wejście do edycji zestawu).  
• Widoczne są wyłącznie kolekcje zalogowanego użytkownika.

US-004 Utworzenie kolekcji z tekstu  
Opis: Jako użytkownik chcę wkleić tekst, wskazać język bazowy i docelowy oraz liczbę fiszek, aby wygenerować nową kolekcję.  
Kryteria akceptacji:  
• Pole wklejenia tekstu działa dla maksymalnie 10 000 znaków.  
• Autodetekcja języka tekstu działa i prezentuje wykryty język (może być edytowalny lub tylko informacyjny).  
• Użytkownik ustala liczbę fiszek 1–100.  
• Po kliknięciu „Generuj” pojawia się ekran ładowania, a po sukcesie kolekcja jest widoczna na liście.

US-005 Utworzenie kolekcji z PDF  
Opis: Jako użytkownik chcę wgrać pojedynczy plik PDF do 5 MB i wygenerować kolekcję fiszek.  
Kryteria akceptacji:  
• UI akceptuje wyłącznie jeden plik PDF ≤ 5 MB.  
• Autodetekcja języka materiału wejściowego działa dla PDF.  
• Po generacji nowa kolekcja pojawia się na liście.
• Maksymalna ilość znaków wyciągniętych z PDF to 10 tysięcy.

US-006 Konfiguracja pary językowej i orientacji fiszki  
Opis: Jako użytkownik chcę wybrać język bazowy (front) i docelowy (back), aby dopasować naukę do moich potrzeb.  
Kryteria akceptacji:  
• Front fiszki zawsze prezentowany w języku bazowym.  
• Back w języku docelowym.  
• Zapisane ustawienia obowiązują dla całej kolekcji.

US-007 Generowanie fiszek z regułami zdaniowymi  
Opis: Jako użytkownik chcę otrzymać fiszki będące krótkimi zdaniami (≤8 słów), bez nazw własnych, aby mieć kontekst do nauki.  
Kryteria akceptacji:  
• Każda fiszka spełnia regułę długości (≤8 słów).  
• Fiszki nie zawierają nazw własnych.  
• Fiszki tworzą naturalne zdania w języku docelowym lub bazowym zgodnie z projektem kolekcji.

US-008 Widok nauki – start sesji  
Opis: Jako użytkownik chcę po wejściu do kolekcji od razu zobaczyć pierwszą fiszkę i rozpocząć naukę.  
Kryteria akceptacji:  
• Po kliknięciu kolekcji ładowana jest pierwsza fiszka.  
• Widoczne są akcje „Umiem” i „Nie umiem” oraz ikona do edycji, rowniez usunięcia aktualnego elementu z zestawu jeśli klikneliśmy w tryb edycji.

US-009 Oznaczenie „Umiem”  
Opis: Jako użytkownik chcę oznaczyć fiszkę jako „Umiem”, aby zniknęła z przyszłych sesji w tej kolekcji.  
Kryteria akceptacji:  
• Oznaczona fiszka przestaje pojawiać się w sesjach tej kolekcji.  
• Status jest zapisywany i utrzymany po odświeżeniu strony.

US-010 Oznaczenie „Nie umiem”  
Opis: Jako użytkownik chcę oznaczyć fiszkę jako „Nie umiem”, aby pozostała w puli do dalszej nauki.  
Kryteria akceptacji:  
• Fiszka pozostaje w puli i może pojawiać się w kolejnych sesjach.  
• Status jest zapisywany i utrzymany po odświeżeniu.

US-011 Edycja fiszki  
Opis: Jako użytkownik chcę edytować treść fiszki i zapisać zmiany po potwierdzeniu.  
Kryteria akceptacji:  
• Ikona edycji na poziomie danego elementu zestawu przenosi do trybu edycji danej fiszki.  
• Zmiana treści fiszki wymaga potwierdzenia do zapisu.  
• Po zapisie widok pozostaje w kolekcji; zmiana jest natychmiast widoczna.

US-012 Usunięcie fiszki  
Opis: Jako użytkownik chcę usunąć konkretną fiszkę i automatycznie przejść do następnej.  
Kryteria akceptacji:  
• Ikona X jest dostępna tylko w trybie edycji.
• Ikona X usuwa fiszkę z kolekcji.  
• Po usunięciu aplikacja wyświetla następną fiszkę (jeśli istnieje).

US-013 Ponowienie generacji po błędzie  
Opis: Jako użytkownik chcę, aby po nieudanym generowaniu aplikacja zapytała mnie, czy ponowić próbę.  
Kryteria akceptacji:  
• W razie błędu pojawia się komunikat z opcją „Ponów”.  
• Po „Ponów” aplikacja ponownie wywołuje generowanie z identyczną konfiguracją.

US-014 Ochrona dostępu do danych  
Opis: Jako użytkownik chcę, aby nikt poza mną nie widział moich kolekcji i postępu.  
Kryteria akceptacji:  
• API i UI respektują kontekst zalogowanego użytkownika.  
• Próba dostępu do cudzej kolekcji zwraca błąd autoryzacji/odmowę.

US-015 Odrzucenie pliku ponad limit  
Opis: Jako użytkownik chcę, aby plik PDF większy niż 5 MB lub taki z którego wyciągnięty tekst będzi przekraczał 10 tysięcy znakow nie został przyjęty i abym dostał jasny komunikat.  
Kryteria akceptacji:  
• Wgrywanie pliku >5 MB jest blokowane w UI lub odrzucane po stronie serwera.  
• Użytkownik otrzymuje informację o limicie 5 MB.

US-016 Wylogowanie  
Opis: Jako użytkownik chcę się wylogować, aby zakończyć sesję i chronić dostęp do moich danych.  
Kryteria akceptacji:  
• Kliknięcie „Wyloguj” zamyka sesję i przekierowuje do ekranu logowania.  
• Dane użytkownika nie są dostępne po wylogowaniu.

US-017 Orientacja fiszki: front/back  
Opis: Jako użytkownik chcę mieć pewność, że front fiszki jest w języku bazowym, a back w docelowym.  
Kryteria akceptacji:  
• Front zawsze w języku bazowym w całej aplikacji.  
• Back zawsze w języku docelowym w całej aplikacji.

US-018 Widok ładowania  
Opis: Jako użytkownik chcę widzieć prostą animację ładowania podczas generacji.  
Kryteria akceptacji:  
• Po kliknięciu „Generuj” pojawia się animacja do czasu zakończenia procesu.  
• Po zakończeniu generacji użytkownik trafia na listę kolekcji, gdzie widoczna jest nowa pozycja.

US-019 Ograniczenie liczby fiszek na operację  
Opis: Jako użytkownik chcę, aby system nie pozwalał mi ustawić więcej niż 100 fiszek na jedną generację.  
Kryteria akceptacji:  
• Pole liczby fiszek ograniczone do zakresu 1–100.  
• Próba wpisania wartości >100 blokowana lub korygowana z komunikatem.

US-020 Widok pojedynczej kolekcji – nawigacja  
Opis: Jako użytkownik chcę wygodnie przechodzić między fiszkami w kolekcji.  
Kryteria akceptacji:  
• Przycisk następna fiszka to po prostu umiem/nie umiem, nie da sie wrócić.
• Kontekst kolekcji nie gubi się przy odświeżeniu strony.

US-021 Bezpieczny dostęp – uwierzytelnianie  
Opis: Jako użytkownik chcę bezpiecznie logować się do aplikacji.  
Kryteria akceptacji:  
• Uwierzytelnianie przez Supabase z prawidłowym przepływem sesji.  
• Dostęp do widoków aplikacji jest niedostępny bez zalogowania.

US-022 Błędy formularza i walidacje UI (minimalne)  
Opis: Jako użytkownik chcę otrzymywać podstawowe komunikaty w UI dla oczywistych błędów (np. brak tekstu, brak pliku).  
Kryteria akceptacji:  
• Nie można rozpocząć generowania bez wprowadzenia tekstu lub wybrania PDF.  
• Nie można rozpocząć generowania bez wskazania języka bazowego i liczby fiszek.

## 6. Metryki sukcesu

MS-01 Ocena manualna jakości  
• Manualny tester ocenia jakość wygenerowanych fiszek jako zadowalającą (pozytywna ocena po sesji testowej na rzeczywistych materiałach).

MS-02 Kompletność funkcjonalna  
• Wszystkie wymagania FR-01…FR-10 oraz historyjki US-001…US-022 działają bez krytycznych błędów.

MS-03 Stabilność podstawowych przepływów  
• Utworzenie kolekcji z tekstu i z PDF kończy się sukcesem w typowych scenariuszach (pojedynczy plik ≤5 MB, do 100 fiszek).  
• W razie niepowodzenia generacji użytkownik może ponowić próbę z UI.
ń
MS-04 Użyteczność interfejsu  
• Użytkownik po zalogowaniu jest w stanie w ≤3 krokach rozpocząć naukę istniejącej kolekcji (wejście na listę, klik w kolekcję, pojawia się pierwsza fiszka).  
• Tworzenie nowej kolekcji w ≤4 krokach (wejście „Nowa kolekcja”, wklejenie tekstu/wybór PDF, wybór języków + liczba fiszek, „Generuj”).

MS-05 Bezpieczeństwo dostępu  
• Użytkownik nie ma dostępu do kolekcji innych użytkowników; próby są poprawnie odrzucane przez backend i UI.

MS-06 Ograniczenia operacyjne  
• System egzekwuje limit 5 MB dla PDF oraz 100 fiszek na generację we wszystkich ścieżkach.
