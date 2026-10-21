# Smart Pantry Manager

A Java Android application for tracking pantry ingredients and suggesting recipes that can be made from ingredients already available at home.

## Technology
- Java and Android Studio
- SQLite via `SQLiteOpenHelper`
- `ListView` with custom `ArrayAdapter` implementations
- Android Intents for screen navigation

## Features
- Create, read, update and delete pantry ingredients
- Local data persistence
- Twenty preloaded recipes
- Strict recipe matching: a recipe is suggested only when all listed ingredient names are present with quantity at least 1
- Recipe details and preparation steps
- Saved expiry-alert preference
- Input validation and an empty-state message

## Setup and run
1. Install Android Studio and a compatible Android SDK.
2. Open this folder in Android Studio using **Open**.
3. Allow Gradle sync to finish.
4. Create/start an Android emulator or connect an Android device with USB debugging enabled.
5. Press **Run** and choose the app configuration.

## Database choice
SQLite was selected because the app is designed for offline, on-device pantry tracking. It avoids a separate server and keeps the implementation easy to demonstrate.

## Important limitation
The starter matching logic checks ingredient presence and requires at least 1 stored unit for each ingredient. It does not yet convert between measurement units or model recipe-specific quantities. Improve and test this if your lecturer expects quantity-aware matching beyond ingredient presence.
