# Tasks 🚀

Tasks is a sleek, modern, and productivity-focused task management application built with **Jetpack Compose** and **Material 3**. It's designed to help you stay organized, focused, and achieve your goals with a high-end user experience.

---

## Screenshots

| Home | Focus | Profile | Settings | Add Task |
|------|-------|---------|----------|----------|
| ![](Home.png) | ![](Empty.png) | ![](Info.png) | ![](Edit.png) | ![](Add.png) |

---

## Key Features ✨

*   **Task Management**: Effortlessly create, edit, and delete tasks with a clean UI.
*   **Focus Sessions**: Integrated Pomodoro-style timer with customizable durations (15m, 25m, 45m, 60m) to boost productivity.
*   **Personalized Profile**: Track your progress with real-time statistics (Total Tasks, Completed, and Today's Wins) along with motivational bios.
*   **Dynamic Theming**: Support for System Default, Light Mode, and a specialized "Deep Sea" Dark Mode.
*   **Custom Navigation**: A unique floating navigation bar with an elevated Floating Action Button (FAB) for quick task entry.
*   **Smart Reminders**: Toggleable notifications to help you stay on track.
*   **Data Management**: Robust options to reset your progress or securely delete all data.
*   **Persistent Storage**: Powered by Room Database for local task persistence and DataStore for user preferences.
*   **Polished UI/UX**: Features custom animations, gradient backgrounds, and refined Material 3 components.

---

## Tech Stack 🛠

*   **Kotlin**: Primary language for modern Android development.
*   **Jetpack Compose**: Declarative UI toolkit for building beautiful native interfaces.
*   **Material 3**: The latest evolution of Material Design.
*   **Room Database**: Local SQLite abstraction for task persistence.
*   **DataStore (Preferences)**: Modern way to store simple user settings.
*   **Navigation Compose**: Type-safe routing between screens.
*   **Coroutines & Flow**: Reactive programming for smooth data handling.
*   **ViewModel**: Lifecycle-aware state management.
*   **SplashScreen API**: Optimized and professional app launch experience.
*   **Hilt/AppContainer**: Efficient dependency injection/management.

---

## Architecture 🏛️

The project adheres to the **MVVM (Model-View-ViewModel)** architectural pattern combined with the **Repository Pattern** for a clean separation of concerns:

*   **Presentation**: UI Screens (Home, Focus, Profile, Settings) and their respective ViewModels.
*   **Domain**: Business logic and data abstraction through Repositories.
*   **Data**: Room DAOs, Entities, and Preferences DataStore implementation.
*   **Navigation**: Centralized `ToDoNavHost` managing the app flow within a Single Activity.

---

## Project Structure 📂

```text
app/
 ├── data/                # Database entities, DAOs, Repositories, and DataStore
 ├── ui/
 │   ├── home/            # Home screen logic and UI
 │   ├── screens/         # Focus, Profile, Settings, Add/Edit screens
 │   ├── navigation/      # NavHost and Destination definitions
 │   └── theme/           # Color schemes (Deep Sea), Typography, and Shapes
 ├── ToDoApp.kt           # Custom Floating Navigation & Scaffold setup
 └── MainActivity.kt      # App entry point with theme & splash support
```

---

## Getting Started ⚙️

1.  **Clone the repo**:
    ```bash
    git clone https://github.com/SamratVsn/Todovsn.git
    ```
2.  **Open in Android Studio**:
    Select `Open` and navigate to the project folder.
3.  **Sync & Build**:
    Wait for Gradle to sync dependencies.
4.  **Run**:
    Click the `Run` button to deploy to your device or emulator.

---

## Requirements 📋

*   **Min SDK**: 24 (Android 7.0)
*   **Target SDK**: 37 (Android 15)
*   **Kotlin**: 2.2.10
*   **Gradle**: 9.3.1

---

## Future Roadmap 🚀

*   **Search & Filter**: Quickly locate tasks by category or priority.
*   **Cloud Sync**: Firebase integration for multi-device synchronization.
*   **Custom Tags**: Create personal labels for better task organization.
*   **Interactive Widgets**: Access your tasks directly from the home screen.
*   **Detailed Analytics**: Visual charts for weekly and monthly productivity trends.

---

## Author 👤

**Samrat Parajuli**

*   **GitHub**: [@SamratVsn](https://github.com/SamratVsn)
*   **Portfolio**: [samratparajuli0.com.np](https://www.samratparajuli0.com.np/)
*   **LinkedIn**: [Samrat Parajuli](https://linkedin.com/in/samratvsn)

---

## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
