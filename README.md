
# FDSATest

FDSATest is a sample project demonstrating an Android application built with **Jetpack Compose**, **Kotlin**, and **modern Android development principles**. It includes features like data persistence using Room, dependency injection with Hilt, and REST API communication with Retrofit.

---

## Features

- **Jetpack Compose**: Modern declarative UI framework for Android.
- **Hilt**: Dependency injection for easier and cleaner management of app dependencies.
- **Room**: Local database support with schema management.
- **Retrofit**: For seamless REST API integration.
- **Pull-to-Refresh**: Custom table implementation with a refresh feature.
- **Material Design 3**: A modern UI/UX design approach with dynamic theming.

---

## Project Structure

The project follows the **MVVM (Model-View-ViewModel)** architectural pattern, ensuring scalability, testability, and maintainability.

- **data**: Handles data sources (local/remote) and models.
  - `remote`: Manages API calls and DTOs (Data Transfer Objects).
  - `local`: Manages Room entities and DAOs.
  - `repository`: Provides a unified interface for data access.
- **domain**: Contains business logic and domain models.
- **ui**: Manages Compose-based UI components and screens.
- **utils**: Utility classes for common operations like date formatting.

---

## Tech Stack

- **Kotlin**: Programming language.
- **Jetpack Compose**: UI development.
- **Hilt**: Dependency injection.
- **Room**: Local database.
- **Retrofit**: REST API communication.
- **Accompanist**: Utilities for Compose (e.g., Glide and Coil for image loading).
- **Material 3**: Modern design components.
- **Kotlinx Serialization**: JSON serialization/deserialization.

---

## Requirements

- **Android Studio**: Flamingo or newer.
- **Minimum SDK**: 25
- **Target SDK**: 34
- **Kotlin Version**: 2.0.21
- **Gradle Version**: 8.6.1

---

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/manu0396/FDSATest.git
   cd FDSATest
   ```

2. Open the project in Android Studio.

3. Sync Gradle to download dependencies:
   ```bash
   ./gradlew build
   ```

4. Run the application on an emulator or physical device.

---

## Key Components

### Custom Table
The app features a reusable **custom table component** built with Jetpack Compose. It supports:
- Row selection.
- Long press to navigate to a detail screen.
- Pull-to-refresh functionality.

### Theming
- **Light and Dark Themes** are implemented dynamically using Material 3.
- Color customization based on primary and secondary palettes.

### Dependency Injection
- Powered by Hilt for managing dependencies throughout the app.

### Data Persistence
- Local data is handled via Room, with a schema export feature for migration management.

---

## Scripts

- Build the app:
  ```bash
  ./gradlew assembleDebug
  ```

- Run unit tests:
  ```bash
  ./gradlew test
  ```

---

## Contribution Guidelines

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new feature branch (`feature/new-feature`).
3. Commit your changes and push to the branch.
4. Open a pull request.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Contact

For inquiries or support, contact [manu0396](https://github.com/manu0396).
