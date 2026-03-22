# 📱 Calling App


This project is a fully functional Android calling interface built entirely with modern Android development standards. It was developed to demonstrate proficiency in UI/UX implementation, state management, local data persistence, and software architecture.

## 🎯 Project Overview

## 🏗️ Core Features & Implementation

### 1. Modern UI & State Management
The application's presentation layer is built entirely using **Jetpack Compose**. The UI is divided into four primary screens, reacting dynamically to the current call state:
* **DialPad Screen:** For inputting numbers and initiating calls.
* **Outgoing Call Screen:** Displays the dialing state with animated UI elements.
* **Incoming Call Screen:** Allows the user to accept or reject simulated incoming calls.
* **Active Call Screen:** Features a live call timer and functional toggle buttons (Mute, Speaker) that update the state in real-time.

These screens are driven by a central `CallViewModel`. By leveraging Kotlin `StateFlow`, the ViewModel acts as the single source of truth. As the call state changes (e.g., from `Idle` to `Calling` to `Active`), the Compose navigation automatically routes the user to the appropriate screen without tightly coupling the UI to the business logic.

### 2. Dual Calling Mechanism
To demonstrate both Android system integration and internal state handling, the app handles two types of calls:
* **Real SIM Calls:** Utilizing Android `Intents` (`ACTION_CALL`) and permission handling to pass the inputted number directly to the device's native dialer.
* **In-App Simulation:** A fully mocked calling flow managed by the ViewModel (using Kotlin Coroutines for delays and transitions) to showcase how the UI reacts to incoming rings, active call durations, and call terminations.

### 3. Local Persistence with Room Database
To expand the app's functionality beyond a simple dialer, I implemented a Contact Saving feature. 
* **Implementation:** I utilized the **Room Persistence Library** to create a local SQLite database. 
* **Functionality:** Users can save a dialed number alongside a contact name. When a call is initiated or received, the app queries the database. If a match is found, the UI gracefully falls back from displaying the raw phone number to displaying the saved contact's name.

### 4. Dependency Injection with Dagger Hilt
As the application grew to include database modules and repositories, manual dependency management became inefficient. 
* **Implementation:** I integrated **Dagger Hilt** to handle Dependency Injection.
* **Impact:** Hilt is used to inject the Room Database instance into the repository, and the repository into the `CallViewModel`. This ensures the architecture remains clean, scalable, and highly testable by decoupling the data layer from the presentation layer.

---

## 🛠️ Technical Stack

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Architecture:** MVVM (Model-View-ViewModel)
* **State Management:** StateFlow / Coroutines
* **Dependency Injection:** Dagger Hilt
* **Local Storage:** Room Database
* **Navigation:** Jetpack Navigation Compose

