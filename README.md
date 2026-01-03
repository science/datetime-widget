# DateTime Widget

A simple Android home screen widget that displays the current date.

```
Sat
Jan 3
```

Font size automatically scales to fill the widget.

## Install

Download [datetime-widget.apk](downloads/datetime-widget.apk) to your Android phone and open it to install. You'll need to allow installation from unknown sources.

After installing, long-press your home screen → Widgets → find "DateTime Widget" → drag to home screen.

## Development

Built with Gradle from WSL2, tested on Windows Android emulator.

```bash
# Build
./gradlew assembleDebug

# Install (connect to emulator first)
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Update the download
cp app/build/outputs/apk/debug/app-debug.apk downloads/datetime-widget.apk
```

## License

Apache 2.0 - see [LICENSE](LICENSE)
