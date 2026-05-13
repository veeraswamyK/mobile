# Appium Configuration Guide

## Overview

This guide explains how to configure Appium for your mobile automation tests in the CI/CD pipeline.

---

## Quick Setup

### Installation

Workflows handle installation automatically, but for local development:

```bash
# Install Appium globally
npm install -g appium@next --ignore-scripts

# Install drivers
appium driver install uiautomator2  # Android
appium driver install xcuitest      # iOS

# Verify installation
appium --version
appium driver list installed
```

### Start Appium Server

```bash
# Basic server (debug mode)
appium --log-level debug --log-timestamp

# With log file
appium --log-level debug --log-file appium.log

# Custom host/port
appium --address 127.0.0.1 --port 4723

# Allow insecure operations
appium --allow-insecure chromedriver_autodownload
```

---

## Capabilities Configuration

### Android Capabilities

**Sample Configuration for Local Emulator**:

```json
{
  "platformName": "Android",
  "appium:automationName": "UIAutomator2",
  "appium:deviceName": "emulator-5554",
  "appium:udid": "emulator-5554",
  "appium:app": "/path/to/app.apk",
  "appium:autoGrantPermissions": true,
  "appium:noSign": true,
  "appium:skipServerInstallation": false,
  "appium:systemPort": 8201,
  "appium:chromedriver": "/usr/local/bin/chromedriver",
  "newCommandTimeout": 300,
  "autoWebview": true,
  "autoWebviewTimeout": 2000
}
```

**Capabilities Explained**:

| Capability | Value | Purpose |
|------------|-------|---------|
| `platformName` | Android | Target platform |
| `appium:automationName` | UIAutomator2 | Android automation engine |
| `appium:deviceName` | emulator-5554 | Device identifier |
| `appium:udid` | emulator-5554 | Unique device ID |
| `appium:app` | /path/to/app.apk | App to test (path or URL) |
| `appium:autoGrantPermissions` | true | Auto-grant app permissions |
| `appium:systemPort` | 8201 | ADB server port |
| `appium:skipServerInstallation` | false | Install AppiumSettings app |
| `newCommandTimeout` | 300 | Timeout between commands (seconds) |
| `autoWebview` | true | Automatically switch to webview |

### iOS Capabilities

**Sample Configuration for Local Simulator**:

```json
{
  "platformName": "iOS",
  "appium:automationName": "XCUITest",
  "appium:deviceName": "iPhone 15",
  "appium:platformVersion": "17.2",
  "appium:app": "/path/to/app.app",
  "appium:xcodeOrgId": "YOUR_TEAM_ID",
  "appium:xcodeSigningId": "iPhone Developer",
  "appium:newCommandTimeout": 300,
  "appium:wdaLaunchTimeout": 60000,
  "appium:wdaConnectionTimeout": 120000,
  "appium:startIWDP": true,
  "simpleIsVisibleCheck": true
}
```

**Capabilities Explained**:

| Capability | Value | Purpose |
|------------|-------|---------|
| `platformName` | iOS | Target platform |
| `appium:automationName` | XCUITest | iOS automation engine |
| `appium:deviceName` | iPhone 15 | Simulator name |
| `appium:platformVersion` | 17.2 | iOS version |
| `appium:app` | /path/to/app.app | App bundle path |
| `appium:xcodeOrgId` | YOUR_TEAM_ID | Xcode team ID (for signing) |
| `appium:xcodeSigningId` | iPhone Developer | Signing certificate |
| `appium:wdaLaunchTimeout` | 60000 | WebDriverAgent launch timeout |
| `appium:startIWDP` | true | Start IWDP for Safari tests |
| `simpleIsVisibleCheck` | true | Simple visibility check (faster) |

### BrowserStack Capabilities

**For Cloud Testing**:

```json
{
  "platformName": "Android",
  "appium:automationName": "UIAutomator2",
  "appium:app": "bs://YOUR_APP_ID",
  "appium:deviceName": "Google Pixel 6",
  "appium:platformVersion": "12",
  "bstack:options": {
    "projectName": "Mobile Automation",
    "buildName": "Build-123",
    "sessionName": "Smoke Test Run",
    "userName": "YOUR_BS_USERNAME",
    "accessKey": "YOUR_BS_ACCESS_KEY",
    "deviceLogs": true,
    "networkLogs": true,
    "appiumVersion": "2.0.0"
  }
}
```

---

## Integration with Test Framework

### Maven Properties Configuration

Edit `pom.xml` to pass capabilities:

```xml
<properties>
  <!-- Appium Configuration -->
  <appium.host>127.0.0.1</appium.host>
  <appium.port>4723</appium.port>
  
  <!-- App Details -->
  <app.path>/path/to/app.apk</app.path>
  <app.package>com.example.app</app.package>
  <app.activity>.MainActivity</app.activity>
  
  <!-- Device Configuration -->
  <device.name>emulator-5554</device.name>
  <device.udid>emulator-5554</device.udid>
  <platform.name>Android</platform.name>
  <automation.name>UIAutomator2</automation.name>
  
  <!-- Timeouts (milliseconds) -->
  <timeout.implicit>10000</timeout.implicit>
  <timeout.explicit>20000</timeout.explicit>
  <timeout.pageload>30000</timeout.pageload>
</properties>
```

### DesiredCapabilities in Java

**Example Setup in Test Code**:

```java
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumSetup {
    
    public static AppiumDriver setupAndroidDriver(String host, int port) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Platform
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:automationName", "UIAutomator2");
        
        // Device
        capabilities.setCapability("appium:deviceName", "emulator-5554");
        capabilities.setCapability("appium:udid", "emulator-5554");
        
        // App
        capabilities.setCapability("appium:app", "/path/to/app.apk");
        capabilities.setCapability("appium:appPackage", "com.example.app");
        capabilities.setCapability("appium:appActivity", ".MainActivity");
        
        // Permissions & Settings
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:noSign", true);
        
        // Timeouts
        capabilities.setCapability("newCommandTimeout", 300);
        capabilities.setCapability("connectTimeout", 60000);
        
        // URL
        URL appiumServerURL = new URL("http://" + host + ":" + port);
        
        return new AndroidDriver(appiumServerURL, capabilities);
    }
    
    public static AppiumDriver setupIOSDriver(String host, int port) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Platform
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("appium:automationName", "XCUITest");
        
        // Device
        capabilities.setCapability("appium:deviceName", "iPhone 15");
        capabilities.setCapability("appium:platformVersion", "17.2");
        
        // App
        capabilities.setCapability("appium:app", "/path/to/app.app");
        
        // WDA Configuration
        capabilities.setCapability("appium:wdaLaunchTimeout", 60000);
        capabilities.setCapability("appium:wdaConnectionTimeout", 120000);
        
        // Timeouts
        capabilities.setCapability("newCommandTimeout", 300);
        
        // URL
        URL appiumServerURL = new URL("http://" + host + ":" + port);
        
        return new IOSDriver(appiumServerURL, capabilities);
    }
}
```

---

## Server Configuration Options

### Performance Tuning

**For Faster Tests**:

```bash
# Disable logs (performance improvement)
appium --log-level error

# Pre-launch WebDriverAgent (iOS only)
appium -c /path/to/appium.config.json
```

**Config File** (`appium.config.json`):

```json
{
  "serverArgs": {
    "log-level": "debug",
    "log-timestamp": true,
    "allow-insecure": [
      "chromedriver_autodownload",
      "shutdown"
    ],
    "base-path": "/wd/hub"
  },
  "driverConfigs": {
    "android": {
      "skipServerInstallation": false,
      "skipUnlock": false
    },
    "ios": {
      "startIWDP": true,
      "prereleaseSimulators": false
    }
  }
}
```

### Debugging & Logging

**Enable Detailed Logging**:

```bash
# All logs to console
appium --log-level debug --log-timestamp

# Logs to file
appium --log-level debug --log-file appium-$(date +%s).log

# Specific module logging
appium --log-level debug:*
appium --log-level debug:appium:*
appium --log-level debug:appium:*,-appium:http:*
```

**Log Levels**:
- `error` - Only errors
- `warn` - Errors and warnings
- `info` - General info (default)
- `debug` - Detailed debug info
- `trace` - Very verbose output

---

## CI/CD Appium Configuration

### In GitHub Actions Workflow

**Android Setup**:

```yaml
- name: Start Appium
  run: |
    mkdir -p appium-logs
    nohup appium \
      --log-level debug \
      --log-timestamp \
      --log-file appium-logs/server.log \
      --allow-insecure chromedriver_autodownload &
    
    # Wait for startup
    for i in {1..30}; do
      if curl -s http://localhost:4723/status > /dev/null; then
        echo "Appium ready"
        break
      fi
      sleep 1
    done
```

**Pass Capabilities to Maven**:

```yaml
- name: Run Tests
  run: |
    mvn test \
      -Dappium.host=127.0.0.1 \
      -Dappium.port=4723 \
      -Ddevice.udid=emulator-5554 \
      -Dapp.path=${{ github.workspace }}/apps/app.apk
```

---

## Common Issues & Solutions

### Issue: "Could not find a connected Android device"

**Cause**: Emulator not running or not recognized

**Solution**:
```bash
# Check connected devices
adb devices

# Verify emulator is running
emulator -list-avds

# Restart emulator
adb emu kill
emulator -avd emulator-5554 &
```

### Issue: "WebDriverAgent connection timeout"

**Cause**: WDA failed to launch on iOS

**Solution**:
```bash
# Increase timeout
capabilities.setCapability("appium:wdaLaunchTimeout", 120000);

# Pre-build WDA
appium driver run xcuitest build-wda
```

### Issue: "Appium server already in use"

**Cause**: Port 4723 already in use

**Solution**:
```bash
# Kill process on port 4723
lsof -ti:4723 | xargs kill -9

# Or use different port
appium --port 4724
```

### Issue: "App installation failed"

**Cause**: APK/IPA signature issues

**Solution - Android**:
```xml
<capability name="appium:noSign">true</capability>
<capability name="appium:autoGrantPermissions">true</capability>
```

**Solution - iOS**:
```xml
<capability name="appium:xcodeOrgId">YOUR_TEAM_ID</capability>
<capability name="appium:xcodeSigningId">iPhone Developer</capability>
```

---

## Best Practices

✅ **Do**:
- Use explicit waits instead of implicit waits
- Pass capabilities via environment variables
- Log all Appium commands in debug mode
- Clean up driver sessions after tests
- Use appropriate timeouts for CI environment
- Pre-install apps when possible
- Use system ports to avoid conflicts

❌ **Don't**:
- Hardcode capabilities in test code
- Use very short timeouts (< 5 seconds)
- Leave Appium server running permanently
- Install apps during every test
- Mix implicit and explicit waits
- Use deprecated capabilities
- Ignore Appium logs during debugging

---

## References

- **Appium Documentation**: https://appium.io/docs/
- **UIAutomator2 Driver**: https://github.com/appium/appium-uiautomator2-driver
- **XCUITest Driver**: https://github.com/appium/appium-xcuitest-driver
- **Capabilities Format**: https://www.w3.org/TR/webdriver/

