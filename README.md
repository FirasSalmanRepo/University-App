# University App - MVI Clean Architecture

An Android application that displays universities in the United Arab Emirates, built with **MVI (Model-View-Intent) Clean Architecture** and a **multi-module** structure.

## Architecture

The project follows **MVI Clean Architecture** with clear separation of concerns across three layers:

- **Presentation Layer** — UI components (Compose / XML), ViewModels, MVI contracts (State, Intent, Effect)
- **Domain Layer** — Use cases and repository interfaces (pure Kotlin, no Android dependencies)
- **Data Layer** — Repository implementations, Retrofit API service, Room database, DTOs, entities, and mappers

## Module Structure

| Module | Description |
|---|---|
| **`:app`** | Application entry point. Hosts `MainActivity`, `UniversityApplication` (Hilt), navigation between screens, and Compose theme. |
| **`:core`** | Shared module containing the Domain layer (models, repository interface, use cases) and Data layer (API, Room DB, repository implementation, DI modules). |
| **`:feature_listing`** | **Module A** — Listing screen built with **Jetpack Compose**. Displays all UAE universities in a scrollable list. |
| **`:feature_details`** | **Module B** — Details screen built with **XML + ViewBinding**. Shows full details of a selected university. |

## Data Flow

1. User lands on the **Listing Screen**
2. The screen fetches data from the [Hipolabs Universities API](http://universities.hipolabs.com/search?country=United%20Arab%20Emirates) and caches it in Room Database
3. On API failure:
   - Loads cached data from Room if available
   - Otherwise displays an error state with a Retry button
4. Tapping a university navigates to the **Details Screen** (no additional API call — data is passed via Intent)
5. The Details Screen contains a **Refresh** button that:
   - Closes the Details Screen
   - Returns to the Listing Screen
   - Triggers a fresh API request and updates the cache

## Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI (Listing) | Jetpack Compose + Material 3 |
| UI (Details) | XML + ViewBinding + Material Components |
| Architecture | MVI (Model-View-Intent) |
| DI | Dagger Hilt |
| Networking | Retrofit + OkHttp + Gson |
| Local DB | Room |
| Async | Kotlin Coroutines + Flow |
| Testing | JUnit 4 + MockK + Coroutines Test |

## Key Design Decisions

- **MVI Pattern**: Each screen has a clear Contract (State, Intent, Effect) with unidirectional data flow
- **StateFlow** for UI state, **Channel** for one-shot side effects (navigation)
- **Repository pattern** with offline-first fallback: API → Cache on success; Cache → Error on failure
- **No additional API call** for Details — the selected `University` (Parcelable) is passed via Intent extras and retrieved through `SavedStateHandle`
- **Activity Result API** for the refresh flow — `DetailsActivity` sets `RESULT_OK` and `MainActivity`'s launcher triggers a reload

## Testing

Unit tests cover:
- `UniversityRepositoryImplTest` — API success/failure and cache fallback logic
- `GetUniversitiesUseCaseTest` — Use case delegation and error propagation
- `ListingViewModelTest` — MVI state transitions and effect emissions
- `DetailsViewModelTest` — SavedStateHandle initialization and refresh effect

## Building

Open the project in Android Studio and sync Gradle. The project requires:
- Android Studio Ladybug or newer
- JDK 21
- Android SDK 36
