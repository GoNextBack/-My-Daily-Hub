# MyDailyHub

MyDailyHub is a Jetpack Compose sample that demonstrates a bottom navigation driven workflow with three productivity modules: Notes for free-form text, Tasks for checkbox lists, and a Calendar placeholder with lightweight month switching.

## Key Features

- Bottom navigation uses `BottomNavigation`/`BottomNavigationItem` with `currentBackStackEntryAsState()` to highlight the active destination and route strings backed by a sealed `HubDestination` definition.
- Each screen keeps its own mutable state via a `ViewModel`, ensuring content survives recompositions and is restored when users return via the navigation bar.
- Navigation arguments (`fromRoute`) drive slide/fade animations so transitions always follow the direction of travel between tabs.
- `popUpTo`, `launchSingleTop`, and `restoreState = true` keep the back stack shallow while restoring previously created screen state.
- Material icons from `Icons.Default.*` visually distinguish the Notes, Tasks, and Calendar tabs.

## Back Stack & Back Button Verification

The animated transitions are computed from the `fromRoute` navigation argument supplied on every `navigate` call. Use the matrix below as a quick checklist when exercising the back button on device/emulator:

| Scenario | Steps | Expected Result | Status |
| --- | --- | --- | --- |
| Back from Tasks | Notes → Tasks → system back | Returns to Notes; Tasks state (checkboxes & text field) preserved | Expected ✅ |
| Back from Calendar | Notes → Tasks → Calendar → system back | Pops back to Tasks; Calendar month selection restored | Expected ✅ |
| Deep switch without back | Notes → Tasks → Calendar → Notes (via bottom nav) | Back stack trimmed to single entry; system back exits app | Expected ✅ |

`launchSingleTop` prevents duplicate destinations, while `popUpTo(startDestination) { saveState = true }` tucks away prior state so returning via the bottom bar or back button restores each screen’s content.

## Running the App

```bash
./gradlew installDebug
adb shell am start -n com.example.mydailyhub/.MainActivity
```

Feel free to adjust the initial sample data by editing the respective `ViewModel` classes under `app/src/main/java/com/example/mydailyhub/features/*`.

> **Build note:** The Android Gradle Plugin `8.13.0` requires running Gradle with a Java 11+ runtime. If your environment defaults to Java 8, set `JAVA_HOME` to an installed JDK 11/17 before invoking Gradle tasks.
