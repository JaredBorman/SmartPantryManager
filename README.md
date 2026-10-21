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

## Database Design

The application uses SQLite for persistent local storage. The database contains three tables:

- **pantry:** Stores ingredient IDs, names, quantities, units, and optional expiry dates.
- **recipes:** Stores recipe IDs, unique recipe names, required ingredients, and preparation steps.
- **preferences:** Stores application settings as key-value pairs.

The database is managed by `DatabaseHelper.java`, which creates the tables and inserts the initial recipe data. Primary keys identify records, while constraints such as `NOT NULL` and `UNIQUE` help maintain data integrity.

## Data Persistence

Pantry ingredients are stored in the local SQLite database so that saved information remains available when the application is closed and reopened. Manual testing confirmed that ingredient additions, updates, and deletions persisted correctly.

## Important limitation
The starter matching logic checks ingredient presence and requires at least 1 stored unit for each ingredient. It does not yet convert between measurement units or model recipe-specific quantities.


## Testing

The application was tested in the Android emulator.

The following tests passed:

- Added pantry ingredients and verified that they appeared in the list.
- Edited an ingredient and confirmed that its updated quantity was displayed.
- Deleted an ingredient and verified that it remained deleted after restarting the app.
- Restarted the app and confirmed that pantry data persisted.
- Verified that recipe suggestions only appeared when all required ingredients were available in sufficient quantities.
- Confirmed that the app displayed a helpful message when no recipes matched the pantry.
- Verified that recipe details displayed the recipe name, ingredients, and cooking steps.
- Confirmed that the app rejected an empty ingredient name and a quantity of zero.
- Verified that the expiry-alert preference persisted after navigating away from Settings and returning.
- Added an ingredient with an expiry date and confirmed that the date was saved and displayed.

## How to Run the Application

1. Install Android Studio on your computer.
2. Clone or download the Smart Pantry Manager repository from GitHub.
3. Open Android Studio and select **Open**.
4. Select the `SmartPantryManager` project folder.
5. Allow Gradle to sync and download the required dependencies.
6. Start an Android emulator or connect an Android device with USB debugging enabled.
7. Click **Run** in Android Studio to build and launch the application.

## Technologies Used

- **Programming language:** Java
- **Development environment:** Android Studio
- **Database:** SQLite
- **User interface:** Android XML layouts
- **Navigation:** Android Activities and Intents
- **Version control:** Git and GitHub

## Project Structure

- `MainActivity.java` manages the main pantry screen.
- `IngredientFormActivity.java` handles adding and editing ingredients.
- `SuggestedRecipesActivity.java` displays recipes that match available ingredients.
- `RecipeDetailActivity.java` displays recipe ingredients and preparation steps.
- `SettingsActivity.java` manages application preferences.
- `DatabaseHelper.java` manages SQLite database operations.
- `IngredientAdapter.java` displays pantry ingredients.
- `RecipeAdapter.java` displays suggested recipes.
