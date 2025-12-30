# Smart Notification Listener

Smart Notification Listener is an Android library that allows applications to listen to system notifications and expose them as a reactive StateFlow. The library is designed for modern Android applications using Jetpack Compose and follows clean architecture principles.

The goal of this library is to provide a reusable, lifecycle-safe, and Compose-friendly way to observe system notifications, while keeping permission handling and UI responsibilities fully in the application layer.

---

## Features

- Listens to system notifications using NotificationListenerService
- Exposes notifications as Kotlin StateFlow
- Designed for Jetpack Compose
- Safe handling of duplicate notification IDs
- Clean separation between library logic and permission handling
- Includes a complete sample application
- Lightweight and production-ready

---

## Demo Video

<details>
<summary>Click to watch demo</summary>

<video src="https://github.com/user-attachments/assets/d521221e-6792-4939-9807-4087b95209be"
       width="360"
       controls>
</video>
</details>

---

## Installation (GitHub / JitPack)

Add JitPack repository to your settings.gradle.kts:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

Add dependency to your app module build.gradle.kts:

```kotlin
implementation("com.github.pascaladitia:smart-notification-listener:1.0.0")
```

---

## Notification Listener Permission

This library requires Android Notification Listener Access. This permission is not a runtime permission and cannot be requested using a dialog.

The user must enable it manually from system settings.

Typical path:

    Settings → Notifications → Notification access → Your App → Allow

Notes:
- The app must be opened at least once before the setting appears
- Some OEM devices may place this setting in a different menu
- The permission may be revoked by the system at any time

---

## Basic Usage

### Observing notifications

Notifications are exposed as a StateFlow and can be collected in a lifecycle-aware manner.

Example with Jetpack Compose:

```kotlin
val notifications by SmartNotificationListener
    .notifications
    .collectAsStateWithLifecycle()
```

---

### Displaying notifications

Example using LazyColumn:

```kotlin
LazyColumn {
    items(
        items = notifications,
        key = { it.packageName + "-" + it.id + "-" + it.postedAt }
    ) { notification ->
        Text(
            text = notification.appName + ": " + notification.title
        )
    }
}
```

Important:
- Android does not guarantee notification IDs are unique
- Always use a stable and unique key when displaying lists

---

## Recommended Permission Handling Pattern

Permission handling is intentionally not included in the library.

Recommended flow:
1. Check whether notification listener permission is granted
2. If not granted, show an explanation screen
3. Redirect the user to system settings
4. Re-check permission when the app resumes

This approach ensures better UX, Play Store compliance, and library reusability.

---

## Architecture Overview

    smart-notification-listener/
    ├── core        Notification repository and internal state
    ├── service     NotificationListenerService implementation
    ├── model       Notification data models
    ├── utils       Public API access
    └── sample-app  Jetpack Compose sample application

---

## How It Works

1. The library registers a NotificationListenerService
2. Incoming notifications are received by the service
3. Notification data is mapped into a NotifMeta model
4. Data is stored in an internal repository
5. Updates are emitted via StateFlow
6. UI layers collect and react to updates

---

## Known Limitations

- Notification listener permission must be enabled manually
- Some applications emit notifications with duplicate IDs
- OEM devices may restrict background services
- Notifications may be removed by the system at any time

---

## Compatibility

- Minimum SDK: 24
- Recommended Android version: 8.0 and above
- Kotlin: 1.9 or newer
- Jetpack Compose: Latest stable
- Material 3 supported

---

## Roadmap

- Notification grouping by application
- Conversation and message extraction
- Application-level filtering
- Persistent storage support
- Maven Central publication

---

## License

MIT License

Copyright (c) 2025 Pascal

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files to deal in the Software without restriction.

The software is provided "as is", without warranty of any kind.

---

## Author

Pascal  
GitHub: https://github.com/pascaladitia
