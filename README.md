# Smart Kids

Smart Kids is an Android application developed as an educational project for children.

The project is implemented as a native Android application using Kotlin and Gradle. It provides a mobile application environment designed around educational content and child-oriented interaction.

## Table of Contents

- [Overview](#overview)
- [Objectives](#objectives)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Documentation](#documentation)
- [Development](#development)
- [Build](#build)
- [GitHub Repository](#github-repository)
- [Author](#author)
- [License](#license)

## Overview

**Smart Kids** is an Android application project focused on creating a dedicated mobile environment for children and educational use.

The project follows the standard Android application structure and uses Gradle with Kotlin DSL for project configuration.

The repository contains the Android application source code, Gradle configuration, and a project presentation document.

## Objectives

The project aims to provide an Android-based educational application for children while applying the fundamentals of native Android development.

The main development objectives include:

- Designing a mobile application for children
- Developing Android application interfaces
- Organizing an Android project using Gradle
- Implementing application logic with Kotlin/Android components
- Building and testing the application on Android
- Applying good practices for Android project organization

## Technologies Used

### Android

- Android SDK
- Native Android development
- Android Studio

### Programming

- Kotlin
- XML for Android layouts

### Build System

- Gradle
- Gradle Kotlin DSL

The project uses files such as:

```text
build.gradle.kts
settings.gradle.kts
gradle.properties
gradlew
gradlew.bat
```

## Project Structure

The main repository structure is:

```text
KidSmart/
│
├── app/
│   └── Android application source code
│
├── docs/
│   └── Presentation.pdf
│
├── gradle/
│   └── Gradle wrapper files
│
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── .gitignore
└── README.md
```

### `app/`

This directory contains the Android application module, including the source code, resources, manifest, and module-level configuration.

### `docs/`

Contains the project presentation:

```text
Presentation.pdf
```

The document is included to provide additional information about the project.

### Gradle Files

The project uses Gradle Kotlin DSL for its build configuration.

Important files include:

- `build.gradle.kts`
- `settings.gradle.kts`
- `gradle.properties`

The Gradle wrapper allows the project to be built using the configured Gradle environment.

## Requirements

To work with the project, the following tools are recommended:

- Android Studio
- Android SDK
- JDK compatible with the project's Android Gradle configuration
- Git

The Android SDK path should be configured locally through `local.properties`.

`local.properties` is intentionally excluded from Git because it contains machine-specific configuration.

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/naziha-goubaa/smart-kids.git
```

Then enter the project directory:

```bash
cd smart-kids
```

### 2. Open the project

Open the project with Android Studio.

Android Studio will detect the Gradle configuration and synchronize the project.

### 3. Configure the Android SDK

Make sure the Android SDK is installed and correctly configured.

Android Studio normally creates the local SDK configuration automatically.

If required, configure the SDK path locally. This information is stored in:

```text
local.properties
```

This file is not committed to the repository.

## Running the Application

After opening and synchronizing the project in Android Studio:

1. Connect an Android device or start an Android Emulator.
2. Make sure USB debugging is enabled if using a physical device.
3. Select the `app` configuration.
4. Click **Run** in Android Studio.

The application will then be installed and launched on the selected Android device or emulator.

## Building the Application

The project includes the Gradle wrapper.

On Windows:

```cmd
gradlew.bat build
```

To build the debug APK:

```cmd
gradlew.bat assembleDebug
```

The generated build files are placed under the Gradle/Android build directories and are intentionally excluded from Git.

## Development

For development, the recommended workflow is:

```text
Clone Repository
       ↓
Open in Android Studio
       ↓
Gradle Synchronization
       ↓
Develop / Modify
       ↓
Build
       ↓
Test on Emulator or Android Device
       ↓
Commit Changes
       ↓
Push to GitHub
```

## Git Ignore Policy

The repository excludes generated and machine-specific files such as:

```text
.gradle/
.idea/
build/
local.properties
.cxx/
.externalNativeBuild/
captures/
*.iml
```

These files are generated locally by Android Studio or Gradle and should not be stored in the source repository.

## Documentation

The project presentation is available in:

```text
docs/Presentation.pdf
```

It provides additional project information and can be consulted alongside the source code.

## Academic Purpose

Smart Kids was developed as an academic Android project.

It provides practical experience with:

- Native Android application development
- Kotlin
- Android project architecture
- XML-based interfaces
- Gradle and Android build configuration
- Mobile application testing
- Git and GitHub project management

## Possible Improvements

Future versions could extend the application with additional educational features, richer child-oriented interactions, improved interface design, additional learning activities, progress tracking, and other functionality depending on the project's requirements.

These improvements are suggestions for future development and are not presented as features of the current version.

## GitHub Repository

The project repository is:

https://github.com/naziha-goubaa/smart-kids

## Author

**Naziha Goubaa**

GitHub:

https://github.com/naziha-goubaa

## License

This project was developed for academic and educational purposes.

Unless otherwise specified, the source code is provided for learning and demonstration purposes.
