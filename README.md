# DateTime Widget

Simple Android widget displaying current date and time.

## Development Environment

- **Code**: WSL2 (Linux)
- **Emulator**: Windows (Android Studio)
- **Build**: Gradle from WSL2

## Emulator Configuration

| Property | Value |
|----------|-------|
| Name | Medium Phone API 36.1 |
| AVD ID | `Medium_Phone_API_36.1` |
| Target | android-36.1 |
| Architecture | x86_64 |
| Screen | 1080x2400 @ 420dpi |
| RAM | 2048 MB |
| Storage | 6 GB |
| Play Store | Enabled |

## Commands

### Windows (PowerShell): Start Emulator

```powershell
# Start emulator (from PowerShell)
emulator -avd Medium_Phone_API_36.1

# Or with cold boot (if having issues)
emulator -avd Medium_Phone_API_36.1 -no-snapshot-load
```

### WSL2: Connect to Emulator

```bash
# Find Windows host IP and connect
WIN_HOST=$(cat /etc/resolv.conf | grep nameserver | awk '{print $2}')
adb connect $WIN_HOST:5555

# Or use the alias (if configured in .bashrc)
adb-win

# Verify connection
adb devices
```

### WSL2: Build and Install

```bash
# Build debug APK
./gradlew assembleDebug

# Install on emulator
adb install app/build/outputs/apk/debug/app-debug.apk

# Reinstall (if already installed)
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### WSL2: Useful ADB Commands

```bash
# View device logs
adb logcat

# Filter to our app
adb logcat | grep -i datetimewidget

# Uninstall app
adb uninstall com.science.datetimewidget

# List installed packages
adb shell pm list packages | grep science
```

## Windows Setup (One-Time)

Before WSL2 can connect to the emulator, run these in PowerShell (Admin):

```powershell
# 1. Enable TCP mode on emulator (run after emulator starts)
adb tcpip 5555

# 2. Allow ADB through firewall
New-NetFirewallRule -DisplayName "ADB for WSL2" -Direction Inbound -Protocol TCP -LocalPort 5555,5554,5037 -Action Allow

# 3. Port forward WSL2 traffic to localhost (critical!)
netsh interface portproxy add v4tov4 listenport=5555 listenaddress=0.0.0.0 connectport=5555 connectaddress=127.0.0.1

# Verify port proxy
netsh interface portproxy show all
```

## Troubleshooting

### ADB Connection Fails

1. Ensure emulator is running on Windows
2. Verify port proxy is set up (see Windows Setup above)
3. Check Windows Firewall allows port 5555
4. Try restarting ADB server:
   ```bash
   adb kill-server
   adb start-server
   adb-win
   ```

### Emulator Won't Start

```powershell
# Cold boot (ignore snapshots)
emulator -avd Medium_Phone_API_36.1 -no-snapshot-load

# Check available AVDs
emulator -list-avds
```
