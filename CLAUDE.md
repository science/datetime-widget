# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Simple Android widget displaying current date and time. Widget-only app (no launcher activity).

## Project Structure

```
datetime-widget/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/science/datetimewidget/
│   │   │   └── DateTimeWidgetProvider.kt    # Widget logic
│   │   ├── res/
│   │   │   ├── layout/widget_layout.xml     # Widget UI (TextClock)
│   │   │   ├── xml/widget_info.xml          # Widget metadata
│   │   │   └── values/strings.xml           # App strings
│   │   └── AndroidManifest.xml              # Widget registration
│   └── build.gradle.kts                     # App build config
├── build.gradle.kts                         # Root build config
├── settings.gradle.kts                      # Project settings
└── gradle/wrapper/                          # Gradle wrapper
```

## Build & Test Commands

```bash
# Generate gradle wrapper (first time only)
gradle wrapper

# Build debug APK
./gradlew assembleDebug

# Connect to Windows emulator (from WSL2)
adb-win  # alias for: adb connect $(cat /etc/resolv.conf | grep nameserver | awk '{print $2}'):5555

# Install on emulator
adb install app/build/outputs/apk/debug/app-debug.apk

# Reinstall (if already installed)
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Uninstall
adb uninstall com.science.datetimewidget

# View logs
adb logcat | grep -i datetimewidget
```

## Development Methodology: TDD Red/Green

**All new functionality MUST follow Test-Driven Development:**

1. **RED**: Write a failing test first, run to prove it fails
2. **GREEN**: Write minimal code to pass, run to prove it passes
3. **REFACTOR**: Clean up while keeping tests green
4. **REPEAT**: Build functionality incrementally with test coverage

### Key Principles
- Never skip the RED step - running before implementation proves the test can fail
- Small increments - each test covers one small behavior
- For Android widgets: "test" = build succeeds, installs, displays correctly on emulator

### Widget-Specific TDD Approach
Android widgets are hard to unit test (RemoteViews, system integration). Apply TDD spirit:
1. **RED**: Attempt build/install - should fail
2. **GREEN**: Add minimal code to make it work
3. **VERIFY**: Manual verification on emulator (screenshot if needed)
4. **COMMIT**: Commit working increments

## Git Workflow

- Commit intermediate successful stages
- Each commit should represent a working state
- Use descriptive commit messages

## Development Environment

- **Code**: WSL2 (Linux)
- **Emulator**: Windows (Android Studio)
- **Build**: Gradle from WSL2
- **ADB**: Connects over TCP to Windows emulator

See README.md for detailed setup instructions.
