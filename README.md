University App

A modular Android application that displays universities in the United Arab Emirates, built with MVI Clean Architecture.

Architecture

The project follows MVI (Model-View-Intent) pattern with Clean Architecture layers:





Presentation Layer -- ViewModels, UI State, Intents, Effects



Domain Layer -- Use Cases, Repository interfaces, Domain models



Data Layer -- Repository implementations, Retrofit API, Room Database

Module Structure

UniversityApp/
├── app/                  # Main application module (entry point, theme, navigation)
├── core/                 # Shared module (data layer, domain layer, DI)
├── feature_listing/      # Listing Screen (Jetpack Compose)
└── feature_details/      # Details Screen (XML + ViewBinding)

Features





Listing Screen (Jetpack Compose) -- Displays all UAE universities with search functionality, filter chips, and cache status indicator



Details Screen (XML + ViewBinding) -- Shows university details with hero image, location info, website links, and a refresh action



Offline Support -- Data is cached in Room DB; on API failure, cached data is served with a visual indicator



Refresh Mechanism -- Details screen refresh button returns to listing and triggers a fresh API call

Tech Stack







Category



Technology





Language



Kotlin





UI (Listing)



Jetpack Compose + Material 3





UI (Details)



XML + ViewBinding + Material Components





Architecture



MVI Clean Architecture





Networking



Retrofit + OkHttp + Gson





Local Storage



Room Database





Dependency Injection



Dagger Hilt





Async



Kotlin Coroutines + Flow





Testing



JUnit + MockK + Coroutines Test





Build System



Gradle Kotlin DSL (AGP 9.1.1)





Font



Inter (Variable TTF)

API

Data is fetched from the Hipolabs Universities API:

GET http://universities.hipolabs.com/search?country=United%20Arab%20Emirates

Data Flow

API Request
    ├── Success → Cache in Room DB → Display list (isFromCache = false)
    └── Failure → Load from Room DB
                    ├── Cache available → Display list (isFromCache = true, banner shown)
                    └── Cache empty → Show error with retry

Building

# Debug build
./gradlew assembleDebug

# Run unit tests
./gradlew test

Requirements


Testing

Unit tests cover:





UniversityRepositoryImplTest -- API success/failure, cache fallback, error propagation



GetUniversitiesUseCaseTest -- Delegation to repository, exception propagation



ListingViewModelTest -- State loading, error handling, cache flag, navigation effects



DetailsViewModelTest -- University state loading, refresh effect emission

