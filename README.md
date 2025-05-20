
# SOS-App – Aplikacja ratunkowa dla podróżników

## Spis treści

1. **Opis projektu**
2. **Funkcjonalności**
3. **Architektura i struktura projektu**
4. **Wymagania**
5. **Opis działania poszczególnych modułów**
6. **Bezpieczeństwo i uprawnienia**
7. **Możliwe rozszerzenia**
8. **Autor**

---

## 1. Opis projektu

**SOS-App** to aplikacja mobilna na system Android, zaprojektowana z myślą o osobach aktywnie spędzających czas w terenie, np. podczas wycieczek górskich. Jej głównym celem jest umożliwienie szybkiego wezwania pomocy w sytuacjach zagrożenia życia lub zdrowia, poprzez wykorzystanie możliwości smartfona: latarki, GPS oraz SMS.

---

## 2. Funkcjonalności

- **Automatyczne generowanie sygnału SOS** – aplikacja wykorzystuje latarkę urządzenia do nadawania sygnału świetlnego SOS w kodzie Morse’a.
- **Lokalizacja użytkownika** – pobieranie aktualnej pozycji GPS oraz prezentacja jej na mapie Google w aplikacji.
- **Wysyłanie SMS z lokalizacją** – możliwość wysłania wiadomości SMS z prośbą o pomoc oraz linkiem do lokalizacji na mapie Google.
- **Intuicyjna nawigacja** – nowoczesny interfejs oparty o Material 3 i Jetpack Compose, z menu bocznym (drawer) i przejrzystym podziałem na ekrany.
- **Obsługa uprawnień w czasie rzeczywistym** – aplikacja dynamicznie prosi o wymagane uprawnienia i informuje użytkownika o ich znaczeniu.

---

## 3. Architektura i struktura projektu

Projekt oparty jest o architekturę modularną, z podziałem na logiczne komponenty:

- `MainActivity.kt` – główna aktywność, zarządza nawigacją i szufladą menu.
- `SosScreen.kt` – ekran główny z przyciskami do uruchomienia sygnału SOS i wysłania SMS.
- `SosFlasher.kt` – logika generowania sygnału SOS latarką (kod Morse’a).
- `MapScreen.kt` – ekran z mapą Google i markerem pozycji użytkownika.
- `RequestLocationPermission.kt` – obsługa uprawnień do lokalizacji, wraz z wyjaśnieniami i przekierowaniem do ustawień.
- `DrawerContent.kt` – zawartość menu bocznego.
- `Theme.kt` – definicje motywów kolorystycznych (jasny/ciemny).
- `AuthorScreen.kt` – ekran informacyjny o autorze.

---

## 4. Wymagania

- **System operacyjny:** Android 7.1 (API 25) lub nowszy
- **Uprawnienia:** Dostęp do lokalizacji (ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), wysyłanie SMS (SEND_SMS), dostęp do latarki
- **Dodatkowe biblioteki:** Jetpack Compose, Google Maps Compose, Google Play Services Location

---

## 5. Opis działania poszczególnych modułów

### 5.1. Sygnał SOS (latarka)

- Moduł `SosFlasher.kt` cyklicznie włącza i wyłącza latarkę, generując sygnał SOS w kodzie Morse’a (trzy krótkie, trzy długie, trzy krótkie błyski).
- Obsługa odbywa się w osobnym wątku (Kotlin Coroutines), z możliwością zatrzymania sygnału w dowolnym momencie.

### 5.2. Lokalizacja i mapa

- Moduł `MapScreen.kt` pobiera aktualną lokalizację użytkownika (z zachowaniem bezpieczeństwa i obsługą braku uprawnień).
- Po uzyskaniu pozycji, wyświetla ją na mapie Google z markerem.
- W przypadku braku uprawnień lub błędów, użytkownik otrzymuje czytelny komunikat.

### 5.3. Wysyłanie SMS

- Moduł `SosScreen.kt` umożliwia wysłanie SMS na zdefiniowany numer (np. służby ratunkowe).
- Wiadomość zawiera prośbę o pomoc oraz link do lokalizacji użytkownika w Google Maps.
- Obsługa uprawnień do SMS jest dynamiczna – użytkownik jest proszony o zgodę w momencie próby wysłania wiadomości.

### 5.4. Uprawnienia

- Moduł `RequestLocationPermission.kt` zarządza żądaniami uprawnień do lokalizacji, wyświetla uzasadnienia i umożliwia szybkie przejście do ustawień systemowych w razie odmowy.

### 5.5. Interfejs użytkownika

- Całość oparta o Jetpack Compose i Material 3 – nowoczesny, responsywny i czytelny interfejs.
- Menu boczne umożliwia szybkie przełączanie się między ekranami: SOS, Mapa, Autor.

---

## 6. Bezpieczeństwo i uprawnienia

Aplikacja wymaga dostępu do wrażliwych funkcji urządzenia (lokalizacja, SMS, latarka). Wszystkie uprawnienia są żądane w sposób zgodny z wytycznymi Google, a użytkownik jest informowany o ich celu. W przypadku odmowy, aplikacja wyświetla odpowiednie komunikaty i umożliwia zmianę decyzji w ustawieniach systemowych.

---

## 7. Możliwe rozszerzenia

- Integracja z numerami alarmowymi odpowiednimi dla lokalizacji użytkownika.
- Automatyczne wykrywanie upadku lub braku ruchu i wysyłanie SOS.
- Historia lokalizacji i wysłanych zgłoszeń.
- Wsparcie dla innych form komunikacji (np. e-mail, powiadomienia push).
- Tryb offline – zapisanie ostatniej znanej lokalizacji.

---

## 8. Autor

**Imię i nazwisko:** Adrian Goral  
**Uczelnia:** Politechnika Wrocławska  
**Kierunek:** Informatyczne Systemy Automatyki, Specjalność: IPS  
**Semestr:** VI, Maj 2025, Studia Inżynierskie
