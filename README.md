This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

TODO:
- Bad Habit Tracker
- Main Activity
- Fix todos
- Remove database module
- Fix tests
- Notifications
- Add Screens:
  - Profile 
  - Settings (Multiplatform settings)
  - GEN AI
  - 2FA / Bio security
  - Forgot password ??
  - Sign Up/In (Firebase Auth - Google/Apple/Facebook/Phone/Anonymous) ??
- Web JS or Wasm ??
Version 2.0:
- Weekly Routine
- Monthly Routine ?
- Yearly Routine ?
- Theme Picker
Version 3.0:
- Mood Tracker
- Sleep Tracker
- Heart Rate Tracker
- Steps Tracker
- Period Tracker
