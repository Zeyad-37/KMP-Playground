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
Version 1.0:
- Bad Habit Tracker
- Main Activity
- Fix todos (AI)
- Remove database module (Spike)
- Fix tests (AI)
- Notifications
- Profile 
- Settings (Multiplatform settings / Storo)
- GEN AI
  - Icon Generation
  - Motivational quotes generation
- 2FA / Bio security
- Forgot password ??
- Sign Up/In (Firebase Auth - Google/Apple/Facebook/Phone/Anonymous) ??
- Firebase Crashlytics
- Firebase Analytics
- Firebase RemoteConfig
- Monetization
- Web JS or Wasm ??
Version 2.0:
- Weekly Routine
- Monthly Routine ?
- Yearly Routine ?
- Theme Picker
Version 3.0:
- Release Bad Habit Tracker?
- Mood Tracker
- Sleep Tracker (integration?)
- Heart Rate Tracker (integration?)
- Steps Tracker (integration?)
- Period Tracker (integration?)
Version 4.0:
- Data Overview and analysis (AI)
- Multi device support
Version 5.0:
- Advice as content
- Social media integration
Version 6.0:
- In app purchases