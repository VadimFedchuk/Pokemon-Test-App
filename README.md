# Pokémon App

A small Android app that lists Pokémon from the [PokéAPI](https://pokeapi.co/),
lets the user mark favourites, and persists them locally.

## Features

- 15 Pokémon fetched alphabetically from the PokéAPI
- Two tabs (Pokédex / Wishlist) with a badge for the wishlist count
- Local search by name, type, or ability
- Persistent wishlist (Room) — survives app restarts
- Pull-to-refresh on Pokédex
- Partial-failure handling: if some detail calls fail, the list still shows
  what loaded plus a banner with the failure count
- Reactive UI: tapping the heart updates both tabs simultaneously

## Tech Stack

- **Language / build:** Kotlin 2.0.21, AGP 8.7.3, KSP 2.0.21
- **UI:** Jetpack Compose, Material 3, Navigation Compose
- **Architecture:** MVI per screen, Clean Architecture across layers
- **DI:** Koin 4
- **Networking:** Retrofit, Moshi (reflection), OkHttp logging
- **Local storage:** Room 2.7 (TypeConverters, no `@Relation`)
- **Concurrency:** Coroutines, Flow, structured concurrency via `coroutineScope`
- **Images:** Coil 2
- **Testing:** JUnit, MockK, Turbine, coroutines-test

## Setup

No API key required — PokéAPI is open. Open the project in Android Studio
(Ladybug or newer recommended) and run on an emulator or device with API 26+.

## Architecture

Three layers — `data`, `domain`, `presentation` — with strict dependency direction
inward toward `domain`. Repositories live in `data`, expose interfaces from `domain`,
and are consumed via use cases.

**Single source of truth.** Room is the only thing the UI reads from. The network
layer's only job is to refresh the cache; Room then emits new values to the UI via
Flow. This makes the wishlist toggle automatically propagate to both tabs without
any manual sync.

**MVI per screen.** Each ViewModel exposes `state: StateFlow`, `effect: Channel`,
and a single `onIntent(Intent)` entry point. State carries data + computed booleans
(`showFullScreenLoading`, `showEmptySearchResult`, etc.) — the UI just renders.

## Testing

Unit tests cover ViewModels (Pokédex, Wishlist), the repository's batch refresh
logic including partial failures, and the wishlist–pokemons combine use case.
`AppGraphTest` — fast feedback for any DI misconfiguration.