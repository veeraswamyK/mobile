# CI/CD Pipeline Troubleshooting Guide

Quick reference for diagnosing and fixing GitHub Actions pipeline issues.

---

## Quick Diagnosis Flowchart

```
Pipeline Failed?
│
├─ Red X on workflow?
│  └─ Check GitHub Actions logs (click on failed job)
│
├─ Tests didn't run?
│  └─ Check trigger conditions and workflow YAML syntax
│
├─ Emulator/Simulator won't boot?
│  └─ See "Device Setup" section
│
├─ Appium connection error?
│  └─ See "Appium Server" section
│
├─ Tests timeout?
│  └─ See "Performance" section
│
└─ Reports not generated?
   └─ See "Reporting" section
```

---

## Workflow Trigger Issues

### Workflows Not Running

**Symptoms**: Pushed code but no workflow started

**Checklist**:
- [ ] Workflow file is in `.github/workflows/` directory
- [ ] File extension is `.yml` (not `.yaml`)
- [ ] Branch name matches trigger condition
- [ ] GitHub Actions enabled in `Settings > Actions > General`
- [ ] Commit matches trigger branch pattern
- [ ] No conflicting concurrency rules

**Debug**:
```bash
# Check workflow syntax locally
npm install -g @github/super-linter
actionlint .github/workflows/*.yml

# Validate with act
act push --dry-run
```

**Fix**:
1. Go to `Settings > Actions > General`
2. Select "Allow all actions and reusable workflows"
3. For local runners, check runner tags match workflow

---

## Java & Build Issues

### Java Version Mismatch

**Error**: `java.lang.UnsupportedClassVersionError`

**Cause**: Java version mismatch between build and runtime

**Solution**:
1. Check pom.xml target version:
```xml
<maven.compiler.target>11</maven.compiler.target>
```

2. Update workflow to match:
```yaml
- name: Set up Java
  uses: actions/setup-java@v4
  with:
    java-version: '11'  # Must match pom.xml
```

3. Verify locally:
```bash
java -version  # Should match target version
```

### Maven Build Failures

**Error**: `[ERROR] BUILD FAILURE`

**Diagnosis**:
1. Check full error message in logs
2. Look for missing dependencies
3. Verify pom.xml syntax

**Common causes**:

| Error | Solution |
|-------|----------|
| `Module not found` | Run `mvn clean install` locally first |
| `Compilation error` | Check Java version matches |
| `Test timeout` | Increase `timeout-minutes` in workflow |
| `Out of memory` | Increase Maven heap: `-Xmx2g -Xms1g` |

**Fix**:
```yaml
- name: Run tests
  run: |
    mvn clean test \
      -X \
      -Dmaven.failsafe.pluginDependencyResolution=test \
      -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

### Dependency Issues

**Error**: `Could not find artifact`

**Solution**:
```bash
# Clear cache locally
mvn clean install -U

# In workflow
- name: Cache Maven
  uses: actions/cache@v4
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
    restore-keys: |
      ${{ runner.os }}-maven-
```

---

## Device Setup Issues

### Android Emulator Won't Boot

**Error**: "Emulator not responding" or "Timed out waiting for boot"

**Checklist**:
```bash
# Check if emulator process is running
ps aux | grep emulator

# Verify ADB recognizes it
adb devices

# Check for port conflicts
lsof -i :5554-5585
```

**Solutions**:

1. **Insufficient system resources**:
   - GitHub-hosted runners have limited CPU/RAM
   - Try different emulator image: `-k "system-images;android-33;google_apis_playstore;x86_64"`
   - Disable unnecessary features: `-no-audio`, `-no-boot-anim`, `-gpu off`

2. **Increase timeout**:
   ```yaml
   - name: Wait for emulator
     run: |
       for i in {1..90}; do  # Increased from 60
         if adb shell getprop sys.boot_completed | grep -q 1; then
           break
         fi
         sleep 2
       done
   ```

3. **Check available images**:
   ```bash
   $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --list
   ```

### iOS Simulator Won't Boot

**Error**: "Failed to boot simulator" or "Simulator took too long to boot"

**Solutions**:

1. **List available simulators**:
   ```bash
   xcrun simctl list devices available
   ```

2. **Clean up old simulators**:
   ```bash
   xcrun simctl erase all  # ⚠️ Careful!
   ```

3. **Increase timeout**:
   ```yaml
   - name: Start simulator
     timeout-minutes: 10
     run: |
       xcrun simctl boot "<device-uuid>" || true
       sleep 60  # Increased from 30
   ```

### Emulator/Simulator Too Slow on CI

**Error**: Tests passing locally but timing out on CI

**Causes**:
- CI runners have limited CPU compared to local machines
- No GPU acceleration available
- Heavy workloads running on runner

**Solutions**:
1. Use smaller/faster emulator images
2. Add explicit waits in test code:
   ```java
   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
   wait.until(ExpectedConditions.presenceOfElementLocated(locator));
   ```
3. Increase test timeouts in workflow:
   ```yaml
   timeout-minutes: 60  # Instead of 45
   ```
4. Run fewer tests in parallel

---

## Appium Server Issues

### Appium Server Fails to Start

**Error**: "Failed to start Appium server" or connection refused

**Debug**:
```bash
# Check Appium installation
appium --version
appium driver list installed

# Test manual start
appium --log-level debug

# Check port availability
lsof -i :4723
```

**Solutions**:

1. **Reinstall Appium**:
   ```bash
   npm install -g appium@next --force --no-save
   appium driver install uiautomator2
   ```

2. **Port conflict**:
   ```bash
   # Kill process on 4723
   lsof -ti:4723 | xargs kill -9
   
   # Use different port
   appium --port 4724
   ```

3. **Verify installation in workflow**:
   ```yaml
   - name: Verify Appium
     run: |
       appium --version
       appium driver list installed
       npm list -g appium
   ```

### Appium Connection Timeout

**Error**: "Connection refused" or "Unable to connect to Appium"

**Solutions**:

1. **Increase wait time**:
   ```yaml
   - name: Wait for Appium
     run: |
       for i in {1..60}; do  # Increased from 30
         if curl -s http://localhost:4723/status > /dev/null 2>&1; then
           echo "Appium ready"
           break
         fi
         echo "Waiting ($i/60)..."
         sleep 2  # Longer delay between checks
       done
   ```

2. **Check Appium logs**:
   ```bash
   # In workflow, ensure logs are captured
   appium --log-level debug --log-file appium.log
   
   # Upload logs as artifact
   - name: Upload Appium logs
     if: always()
     uses: actions/upload-artifact@v4
     with:
       name: appium-logs
       path: appium.log
   ```

3. **Verify capabilities match driver**:
   ```yaml
   # Must install driver before tests
   - name: Install Appium drivers
     run: |
       appium driver install uiautomator2
       # Then verify
       curl -s http://localhost:4723/status | jq .
   ```

### Tests Connection to Wrong Device/Simulator

**Error**: "No devices found" or tests run on wrong emulator

**Solutions**:

1. **Verify device is available**:
   ```yaml
   - name: List devices
     run: adb devices  # Android
     # or
     run: xcrun simctl list devices  # iOS
   ```

2. **Ensure UDID is correct**:
   ```yaml
   - name: Run tests
     env:
       DEVICE_UDID: emulator-5554
     run: |
       mvn test -Ddevice.udid=$DEVICE_UDID
   ```

3. **Check Appium capabilities**:
   ```java
   DesiredCapabilities caps = new DesiredCapabilities();
   caps.setCapability("appium:udid", "emulator-5554");  // Must match
   caps.setCapability("appium:deviceName", "emulator-5554");
   ```

---

## Test Execution Issues

### Tests Timeout

**Error**: "Test timed out after X seconds"

**Diagnosis**:
```bash
# Check if emulator/device is responsive
adb shell getprop sys.boot_completed  # Should show "1"

# Check Appium is still running
curl http://localhost:4723/status

# Check network connectivity
adb shell ping 8.8.8.8
```

**Solutions**:

1. **Increase test timeout**:
   ```yaml
   timeout-minutes: 60  # Overall job timeout
   ```

2. **Increase framework timeouts** (in test code):
   ```java
   // In FrameworkConstants or equivalent
   public static final long IMPLICIT_TIMEOUT = 20;  // seconds
   public static final long EXPLICIT_TIMEOUT = 30;  // seconds
   public static final long PAGE_LOAD_TIMEOUT = 45; // seconds
   ```

3. **Reduce parallel execution**:
   ```xml
   <!-- In pom.xml -->
   <parallel>methods</parallel>
   <threadCount>1</threadCount>  <!-- Reduce from 4 -->
   ```

4. **Skip slow tests on CI**:
   ```gherkin
   @smoke
   Scenario: Fast test
   ```
   Then filter:
   ```yaml
   run: mvn test -Dcucumber.filter.tags="@smoke and not @slow"
   ```

### Tests Fail Only on CI

**Error**: Tests pass locally but fail on GitHub Actions

**Common causes**:
1. **Timing issues**: Local machine is faster
2. **Environment variables**: Different on CI
3. **Network differences**: CI behind proxy
4. **Appium differences**: Version mismatch

**Debug**:
1. Check Appium version:
   ```bash
   appium --version
   ```

2. Compare local vs CI capabilities:
   ```java
   System.out.println(driver.getCapabilities());
   ```

3. Capture full logs:
   ```yaml
   - name: Run tests with verbose logging
     run: |
       mvn test \
         -X \
         -Dorg.slf4j.simpleLogger.defaultLogLevel=debug \
         -Dappium.log.level=debug
   ```

### Individual Tests Fail Randomly

**Error**: Same test passes sometimes, fails other times

**Cause**: Likely race condition or timing issue

**Solutions**:
1. Add explicit waits:
   ```java
   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
   wait.until(ExpectedConditions.elementToBeClickable(locator));
   ```

2. Avoid `Thread.sleep()`:
   ```java
   // Bad
   Thread.sleep(5000);
   
   // Good
   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
   ```

3. Retry failed tests:
   ```xml
   <!-- In testng.xml -->
   <listeners>
     <listener class-name="listeners.RetryListener"/>
   </listeners>
   ```

---

## Reporting Issues

### No Allure Results Generated

**Error**: "Allure artifacts not found" or empty allure-results

**Debug**:
```bash
# Check if directory exists
ls -la target/allure-results/

# Check if tests actually ran
ls -la target/surefire-reports/
```

**Solutions**:

1. **Verify test execution**:
   ```yaml
   - name: Check test output
     if: always()
     run: |
       ls -la target/allure-results/ || echo "No allure results"
       ls -la test-output/ || echo "No test output"
   ```

2. **Check Maven surefire config** (in pom.xml):
   ```xml
   <plugin>
     <groupId>org.apache.maven.plugins</groupId>
     <artifactId>maven-surefire-plugin</artifactId>
     <configuration>
       <argLine>
         -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
       </argLine>
       <systemPropertyVariables>
         <allure.results.directory>${project.build.directory}/allure-results</allure.results.directory>
       </systemPropertyVariables>
     </configuration>
   </plugin>
   ```

3. **Ensure tests execute**:
   ```yaml
   - name: Run tests
     run: mvn test -v  # Verbose output
   ```

### Allure Report Not Deploying to GitHub Pages

**Error**: Report generated but not showing on pages

**Solutions**:

1. **Check GitHub Pages settings**:
   - Go to `Settings > Pages`
   - Source: "Deploy from a branch"
   - Branch: `gh-pages`
   - Folder: `/ (root)`

2. **Verify deployment job ran**:
   ```yaml
   - name: Deploy Report
     if: github.ref == 'refs/heads/main'  # Only on main branch
     uses: peaceiris/actions-gh-pages@v3
     with:
       github_token: ${{ secrets.GITHUB_TOKEN }}
       publish_dir: allure-report
   ```

3. **Clear cache**:
   - Ctrl+Shift+Delete (browser cache clear)
   - Wait 5 minutes for GitHub Pages CDN

### Screenshots Not Captured

**Error**: Screenshots directory empty or missing

**Solutions**:

1. **Verify screenshots are being saved**:
   ```bash
   # In test code, ensure screenshots saved on failure
   ls -la test-output/screenshots/
   ```

2. **Check framework configuration**:
   ```java
   // Your screenshot utility should save to test-output/screenshots
   File screenshotFile = new File("test-output/screenshots/screenshot.png");
   ```

3. **Collect in workflow**:
   ```yaml
   - name: Collect screenshots
     if: failure()  # Or always()
     run: |
       mkdir -p artifacts/screenshots
       cp -r test-output/screenshots/* artifacts/screenshots/ || true
   ```

---

## Artifact & Storage Issues

### Artifacts Not Uploading

**Error**: "Failed to upload artifact" or file not found

**Solutions**:

1. **Verify artifact exists**:
   ```yaml
   - name: Check artifacts
     run: |
       ls -la target/allure-results/
       ls -la test-output/
   ```

2. **Use `if-no-files-found: warn`**:
   ```yaml
   - name: Upload artifacts
     uses: actions/upload-artifact@v4
     with:
       name: results
       path: target/allure-results/
       if-no-files-found: warn  # Won't fail if empty
   ```

### Disk Space Issues

**Error**: "No space left on device"

**Solutions**:

1. **Clean up before tests**:
   ```yaml
   - name: Free disk space
     run: |
       sudo apt-get clean
       sudo rm -rf /usr/share/dotnet
       sudo rm -rf /usr/local/lib/android
   ```

2. **Remove old artifacts**:
   - Go to `Settings > Actions > General`
   - Set "Artifact and log retention" to 7-14 days

---

## Network & Connectivity

### Timeout Connecting to BrowserStack

**Error**: "Connection timeout" when using BrowserStack

**Solutions**:

1. **Verify credentials**:
   ```bash
   echo $BS_USER $BS_KEY  # Should show values
   ```

2. **Check internet on runner**:
   ```yaml
   - name: Connectivity test
     run: |
       ping -c 1 google.com
       curl -I https://www.browserstack.com
   ```

3. **Increase timeout**:
   ```yaml
   - name: Run tests
     timeout-minutes: 45
     run: mvn test ...
   ```

---

## Performance Optimization

### Pipeline Too Slow

**Current timing**:
- Build: ~5-10 minutes
- Tests: ~15-30 minutes
- Reports: ~2-5 minutes

**Optimization steps**:

1. **Skip unnecessary steps**:
   ```yaml
   - name: Skip docs update
     if: github.event_name != 'push'
     run: echo "Skipping..."
   ```

2. **Cache more aggressively**:
   ```yaml
   - name: Cache Gradle (if used)
     uses: actions/cache@v4
     with:
       path: ~/.gradle/caches
       key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle') }}
   ```

3. **Parallel tests** (if your framework supports):
   ```xml
   <parallel>methods</parallel>
   <threadCount>4</threadCount>
   ```

4. **Use faster runners** (self-hosted):
   ```yaml
   runs-on: self-hosted  # Much faster than ubuntu-latest
   ```

---

## Getting Help

### Check Logs Systematically

1. **Workflow logs**: `Actions` tab > workflow run > step logs
2. **Artifact logs**: Download from workflow run artifacts
3. **Runner logs**: Check GitHub Actions runner output
4. **Application logs**: Check test framework logs

### Enable Debug Logging

```yaml
# Temporarily enable for debugging
- name: Debug info
  run: |
    java -version
    mvn -version
    appium --version
    adb devices
    env | grep -i app
```

### Create Minimal Reproducible Example

```bash
# Test locally with same commands
mvn clean test -Dcucumber.filter.tags="@smoke" -X

# Compare with CI output
```

---

## Prevention Checklist

✅ **Before pushing code**:
- [ ] Tests pass locally
- [ ] No hardcoded paths or credentials
- [ ] All dependencies in pom.xml
- [ ] Workflow syntax valid (run through actionlint)
- [ ] Test data is accessible
- [ ] Proper wait strategies used

✅ **After deployment**:
- [ ] Monitor first few pipeline runs
- [ ] Check artifacts are uploaded
- [ ] Verify reports are accessible
- [ ] Test manual workflow dispatch
- [ ] Document any specific issues found

---

## Quick Command Reference

```bash
# Validate workflow syntax
actionlint .github/workflows/*.yml

# Test Appium locally
appium --log-level debug

# Check device status
adb devices
adb shell getprop sys.boot_completed

# View Maven logs
cat target/maven.log

# Extract artifact info
unzip -l allure-results.zip

# Debug network issues
curl -v http://localhost:4723/status
```

---

**Last Updated**: 2024-05-13  
**For additional help**: Check GitHub Actions documentation or create an issue

