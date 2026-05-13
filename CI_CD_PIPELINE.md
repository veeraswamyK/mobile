# GitHub Actions CI/CD Pipeline Documentation

## Overview

This project includes a comprehensive, production-ready GitHub Actions CI/CD pipeline for mobile automation testing with the following features:

- **Multi-platform support**: Android Emulator, iOS Simulator, BrowserStack Cloud
- **Appium integration**: Automatic server setup and health checks
- **Comprehensive reporting**: Allure, TestNG, and Cucumber reports with GitHub Pages publishing
- **Artifact management**: Screenshots, logs, and test reports
- **Scheduled testing**: Nightly regression runs
- **Notifications**: Slack integration for test results
- **Environment management**: Support for multiple environments (dev, staging, production)

---

## Quick Start

### 1. Prerequisites

Ensure these tools are available locally (for development):

```bash
# Java 11+
java -version

# Maven 3.6+
mvn -version

# Node.js 16+ (for Appium)
node --version
npm --version

# Android SDK (for local Android testing)
echo $ANDROID_HOME
```

### 2. GitHub Secrets Configuration

Add these secrets to your GitHub repository (`Settings > Secrets and variables > Actions`):

#### Required for BrowserStack:
```
BS_USER       - Your BrowserStack username
BS_KEY        - Your BrowserStack access key
BS_APP_ID     - Your app ID on BrowserStack
```

#### Optional for Notifications:
```
SLACK_WEBHOOK_URL - Slack webhook for test notifications
```

### 3. GitHub Environments

Create the `production` environment in GitHub:

1. Go to `Settings > Environments`
2. Click `New environment`
3. Name it: `production`
4. Add required secrets (see above)
5. Configure protection rules if needed (optional)

---

## Workflow Execution Modes

### Mode 1: Push-Triggered (Automatic)

**When**: Code pushed to `main` or `develop` branches  
**What runs**: Cloud tests with @smoke tags  
**Duration**: ~15-20 minutes

```bash
git push origin feature/my-feature
# Pipeline runs automatically with default BrowserStack configuration
```

### Mode 2: Schedule (Nightly)

**When**: Daily at 2 AM UTC  
**What runs**: Full regression suite on local Android emulator  
**Duration**: ~30-40 minutes  
**Configure**: Edit `on.schedule.cron` in [main.yml](.github/workflows/main.yml)

### Mode 3: Manual Workflow Dispatch

**When**: Manually triggered from GitHub UI  
**Options**:
- **Execution Type**: cloud | local-android | local-ios | hybrid
- **Tags**: @smoke | @critical | @regression (supports multiple: `@smoke and not @slow`)
- **Upload Artifacts**: true | false

**Run manually**:

1. Go to `Actions` tab in GitHub
2. Select `Mobile Automation CI/CD Pipeline`
3. Click `Run workflow`
4. Configure inputs and click `Run workflow`

Or use GitHub CLI:

```bash
gh workflow run main.yml \
  -f executionType=local-android \
  -f tags="@smoke" \
  -f uploadArtifacts=true
```

---

## Workflow Details

### Main Pipeline (main.yml)

**Features**:
- ✅ Automatic emulator/simulator creation and lifecycle management
- ✅ Appium server startup with health checks
- ✅ Maven build caching for faster builds
- ✅ Comprehensive error handling and retry logic
- ✅ Multi-artifact collection and upload
- ✅ Allure report generation
- ✅ GitHub Pages deployment
- ✅ Slack notifications

**File**: [.github/workflows/main.yml](.github/workflows/main.yml)

**Execution flow**:
```
Prepare (determine execution mode)
    ↓
Mobile Tests (build + test + collect + upload)
    ↓
Publish Allure Report (generate + deploy to GitHub Pages)
    ↓
Notify (Slack)
```

### Android Emulator Pipeline (android-emulator.yml)

**Triggers**:
- Push to `main`/`develop`
- Pull requests to `main`/`develop`
- Daily at 3 AM UTC

**What it does**:
1. Installs Java 11 and Maven
2. Installs Node.js and Appium
3. Installs UIAutomator2 driver for Android
4. Creates Android API 33 emulator
5. Boots emulator with performance optimizations
6. Starts Appium server
7. Runs smoke tests
8. Runs regression tests (if smoke passes)
9. Collects and uploads all artifacts

**File**: [.github/workflows/android-emulator.yml](.github/workflows/android-emulator.yml)

### iOS Simulator Pipeline (ios-simulator.yml)

**Triggers**:
- Push to `main`/`develop` (with iOS-specific file changes)
- Daily at 4 AM UTC

**Requirements**:
- Runs on `macos-latest` (GitHub-hosted macOS runner)
- ~2x slower than Android on Linux due to hardware emulation

**What it does**:
1. Installs Java 11 and Maven
2. Installs Node.js and Appium
3. Installs XCUITest driver
4. Creates/boots iOS 17 simulator
5. Starts Appium server
6. Runs smoke + regression tests
7. Collects and uploads artifacts

**File**: [.github/workflows/ios-simulator.yml](.github/workflows/ios-simulator.yml)

---

## Artifact Management

### What Gets Collected

| Artifact | Location | Retention | Purpose |
|----------|----------|-----------|---------|
| Allure Results | `target/allure-results/` | 30 days | Test metrics & trends |
| TestNG Reports | `test-output/` | 30 days | Detailed test results |
| Screenshots | `test-output/screenshots/` | 14 days | Failure investigation |
| Appium Logs | `appium-logs/` | 7 days | Debugging Appium issues |
| Maven Logs | `target/maven.log` | 7 days | Build debugging |

### Access Artifacts

**Via GitHub UI**:
1. Go to `Actions` → select workflow run
2. Scroll to `Artifacts` section
3. Download desired artifacts

**Via GitHub CLI**:
```bash
# List artifacts
gh run list --workflow main.yml --limit 5

# Download specific artifact
gh run download <run-id> -n allure-results-local-android
```

---

## Report Access & GitHub Pages

### Allure Report on GitHub Pages

After successful test run on `main` branch:

1. Reports deployed to GitHub Pages
2. Access at: `https://<username>.github.io/<repo>/`
3. View trends, history, and detailed test metrics

**Configure custom domain**:

Edit [main.yml](.github/workflows/main.yml), line ~220:
```yaml
cname: testcenter.example.com  # Update this
```

Then configure DNS `CNAME` record pointing to GitHub Pages.

---

## Local Development

### Running Tests Locally

**Android Emulator**:
```bash
# Terminal 1: Start emulator
$ANDROID_HOME/emulator/emulator -avd emulator-5554 -no-snapshot-load

# Terminal 2: Start Appium
appium --log-level debug

# Terminal 3: Run tests
mvn test \
  -Dtest=runners.TestRunner \
  -Dcucumber.filter.tags="@smoke" \
  -DexecutionType=local-android
```

**iOS Simulator**:
```bash
# Terminal 1: Start simulator
xcrun simctl boot <simulator-uuid>

# Terminal 2: Start Appium
appium --log-level debug

# Terminal 3: Run tests
mvn test \
  -Dtest=runners.TestRunner \
  -Dcucumber.filter.tags="@smoke" \
  -DexecutionType=local-ios
```

**Generate Allure Report**:
```bash
# After test execution
mvn allure:report

# View report (opens in browser)
mvn allure:serve
```

---

## Troubleshooting

### Emulator Won't Boot

**Problem**: Emulator times out during startup  
**Solutions**:
1. Increase timeout: Edit `startline-Android` in [main.yml](.github/workflows/main.yml)
2. Check disk space on runner
3. Verify emulator image is cached (check Actions logs)

### Appium Server Fails to Start

**Problem**: "Connection refused" errors  
**Solutions**:
1. Check Appium logs: Download `logs-*` artifact from failed run
2. Verify Node.js/npm versions: `node --version && npm --version`
3. Reinstall Appium: `npm install -g appium@next --force`

### Tests Timeout on CI

**Problem**: Tests pass locally but timeout on CI  
**Solutions**:
1. Increase `timeout-minutes` in workflow YAML
2. Add explicit waits in test code for slow devices
3. Check CI runner specs (may need faster runners)
4. Review `FrameworkConstants` for appropriate timeout values

### Artifacts Not Generated

**Problem**: Expected artifacts are missing  
**Solutions**:
1. Check Maven build logs for errors
2. Verify `target/allure-results/` exists after test run
3. Check file permissions in test framework
4. Review `.gitignore` (shouldn't exclude test artifacts)

### GitHub Pages Not Updating

**Problem**: Allure report not showing after deploy  
**Solutions**:
1. Check `Settings > Pages` is configured for GitHub Actions
2. Verify `GITHUB_TOKEN` has repo write access (default)
3. Check workflow `publish-allure-report` job status
4. Clear browser cache and hard-refresh GitHub Pages URL

---

## Advanced Configuration

### Parallel Test Execution

To run multiple test suites in parallel:

Edit `pom.xml`, update `maven-surefire-plugin`:
```xml
<configuration>
  <parallel>methods</parallel>
  <threadCount>4</threadCount>
  <suiteXmlFiles>
    <suiteXmlFile>testng.xml</suiteXmlFile>
    <suiteXmlFile>dynamic-testng.xml</suiteXmlFile>
  </suiteXmlFiles>
</configuration>
```

Then update workflow to use multiple suites:
```yaml
- name: Run tests
  run: |
    mvn test -v
```

### Custom Test Filters

Cucumber tag expressions supported:

```bash
# Run only smoke tests
@smoke

# Run smoke AND critical
@smoke and @critical

# Run everything except slow tests
not @slow

# Run API OR UI tests
@api or @ui
```

### Slack Notifications

1. Create Slack webhook:
   - Go to Slack workspace settings
   - Create Incoming Webhook
   - Copy webhook URL

2. Add to GitHub secrets:
   ```
   SLACK_WEBHOOK_URL: https://hooks.slack.com/services/YOUR/WEBHOOK/URL
   ```

3. Workflow will automatically post notifications

---

## Security Best Practices

✅ **What We Do**:
- Secrets never exposed in logs
- Environment-based secret management
- Limited artifact retention (7-30 days)
- GitHub token scoped to necessary permissions
- Checkout with `fetch-depth: 0` only when needed

✅ **What You Should Do**:
- Rotate BrowserStack credentials regularly
- Use GitHub Environments for access control
- Review Actions logs for sensitive data
- Use branch protection rules
- Require workflow approval for manual runs
- Audit secret usage with GitHub audit logs

---

## Performance Optimization

### Build Caching

Workflow automatically caches Maven dependencies. To optimize:

```yaml
# In workflow file - already implemented
- name: Cache Maven
  uses: actions/cache@v4
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
    restore-keys: |
      ${{ runner.os }}-maven-
```

### Faster Emulator Boot

Current optimizations:
- GPU disabled: `-gpu off`
- Boot animation disabled: `-no-boot-anim`
- Audio disabled: `-no-audio`
- Snapshot mode disabled: `-no-snapshot-load`
- Pre-built emulator images cached

Expected startup time: 60-90 seconds

### Parallel Workflows

Use GitHub's concurrency to prevent duplicate runs:

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true
```

---

## CI/CD Pipeline Architecture

```
┌─────────────────────────────────────────────────────┐
│  GitHub Events                                       │
│  • Push to main/develop                              │
│  • Pull request                                      │
│  • Schedule (nightly)                                │
│  • Manual workflow_dispatch                          │
└────────────────┬────────────────────────────────────┘
                 │
        ┌────────▼────────┐
        │  Determine Mode │
        │  & Parameters   │
        └────────┬────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
    ▼            ▼            ▼
  Cloud       Android         iOS
  Tests       Emulator      Simulator
    │            │            │
    └────────────┼────────────┘
                 │
        ┌────────▼────────────┐
        │  Run Test Suite     │
        │  • Build (Maven)    │
        │  • Test (TestNG)    │
        │  • Report (Allure)  │
        └────────┬────────────┘
                 │
     ┌───────────┼───────────┐
     │           │           │
     ▼           ▼           ▼
  Upload     Generate    Send
  Artifacts  Reports    Notify
     │           │           │
     └───────────┼───────────┘
                 │
        ┌────────▼────────────┐
        │  Pipeline Complete  │
        │  View Results       │
        └─────────────────────┘
```

---

## Status Badges

Add these to your `README.md`:

```markdown
### CI/CD Status

![Main Pipeline](https://github.com/YOUR_ORG/YOUR_REPO/actions/workflows/main.yml/badge.svg)
![Android Tests](https://github.com/YOUR_ORG/YOUR_REPO/actions/workflows/android-emulator.yml/badge.svg)
![iOS Tests](https://github.com/YOUR_ORG/YOUR_REPO/actions/workflows/ios-simulator.yml/badge.svg)
```

---

## Support & Resources

### Useful Links

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Appium Official Documentation](https://appium.io/)
- [Allure Report Documentation](https://docs.qameta.io/allure/)
- [TestNG Documentation](https://testng.org/)
- [Cucumber Documentation](https://cucumber.io/)

### Common Commands

```bash
# Check workflow syntax
npm install -g @github/super-linter
super-linter --actionlintrc .actionlintrc.json

# Local testing
mvn test -Dcucumber.filter.tags="@smoke"

# Generate Allure report
mvn allure:report
mvn allure:serve

# Debug Appium
appium --log-level debug --log-file appium.log
```

### Contact

For issues or questions:
1. Check this documentation
2. Review GitHub Actions logs
3. Check Appium server logs (in artifacts)
4. Open GitHub issue with logs attached

---

## Changelog

### Version 2.0 (Current)
- ✅ Multi-platform support (Android, iOS, Cloud)
- ✅ Appium server auto-management
- ✅ GitHub Pages integration
- ✅ Slack notifications
- ✅ Comprehensive error handling
- ✅ Production-ready security

### Version 1.0
- Basic cloud testing with BrowserStack
- Simple artifact collection

---

**Last Updated**: 2024-05-13  
**Maintained By**: QA Automation Team
