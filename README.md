# Tablet Grid

A tablet-oriented Android application built with Kotlin and Jetpack Compose.

The application allows the user to specify the number of rows and columns, generate a table filled with random text, select multiple cells, and edit their contents.

## Features

- Table size configuration:
  - From 1 to 1,000 rows
  - From 1 to 6 columns
- Random text generation in the data layer
- Lazy table rendering using `LazyVerticalGrid`
- Multiple cell selection
- Immediate visual selection feedback
- Cell editing by double-tapping
- Portrait and landscape tablet layouts
- Light and dark themes
- State preservation for table settings
- Accessibility semantics for selection and editing

## Architecture

The project follows a modular architecture:

- `app` — application entry point, navigation, and dependency injection
- `ui` — Compose screens, ViewModels, UI state, and UI models
- `domain` — business models, repository contracts, validation, and use cases
- `data` — repository implementations and random data generation

Dependencies are provided using Koin.

## Tablet Support

The application is designed for tablets with a minimum screen width of 600 dp.

In portrait orientation, the cell editor is displayed below the table.  
In landscape orientation, it is displayed next to the table.

## Technology Stack

- Kotlin
- Jetpack Compose
- Material 3
- Compose Navigation
- Kotlin Coroutines and Flow
- Koin
- Kotlinx Immutable Collections
- JUnit