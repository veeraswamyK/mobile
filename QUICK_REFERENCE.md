# CI/CD Pipeline - Quick Reference Card

Print this page for quick reference during development.

---

## 🚀 Quick Commands

### Run Tests Locally
```bash
# Smoke tests
mvn test -Dcucumber.filter.tags="@smoke" -DexecutionType=cloud

# Full regression
mvn test -DexecutionType=local-android

# Specific test
mvn test -Dtest=runners.TestRunner
```

### View Reports
```bash
# Generate Allure report
mvn allure:report

# Serve locally (opens browser)
mvn allure:serve

# Clean Allure history
rm -rf allure-results allure-report
```

### Download Artifacts
```bash
# List recent runs
gh run list --workflow main.yml

# Download artifacts from latest run
gh run download -n allure-results

# Download from specific run
gh run download <run-id> -n test-reports-android
```

---

## 🔑 Secrets Setup

**Location**: `Settings > Secrets and variables > Actions`

```
BS_USER              = BrowserStack username
BS_KEY               = BrowserStack access key
BS_APP_ID            = App ID from BrowserStack
SLACK_WEBHOOK_URL    = Slack notification URL (optional)
```

---

## 📋 GitHub Configuration

### Enable Pages
```
Settings > Pages > Source: gh-pages branch > Save
```

### Enable Actions
```
Settings > Actions > General > Allow all actions > Save
```

### Create Production Environment
```
Settings > Environments > New > production
Add secrets > Save
```

---

## 🎯 Workflow Modes

| Mode | Trigger | Duration | Cost |
|------|---------|----------|------|
| Cloud | `git push` | ~15 min | BS credits |
| Android | Manual/Daily | ~30 min | Free |
| iOS | Manual/Daily | ~40 min | Free |
| Hybrid | Manual | ~60 min | Free + BS |

---

## 🔄 Typical Timeouts

```
Build:              5-10 minutes
Tests:              15-30 minutes
Report Gen:         2-5 minutes
Total:              35-50 minutes
```

---

## 📊 Tag Filters

```bash
# Single tag
@smoke

# Multiple tags (AND)
@smoke and @critical

# Multiple tags (OR)
@api or @ui

# Exclude tags
not @slow

# Complex filters
@smoke and not @slow and (@api or @ui)
```

---

## 🐛 Troubleshooting Quick Fixes

| Problem | Quick Fix |
|---------|-----------|
| Workflow not running | Check branch name and file in `.github/workflows/` |
| Tests timeout | ↑ `timeout-minutes` in YAML |
| Appium error | Kill process: `lsof -ti:4723 \| xargs kill -9` |
| No artifacts | Check test execution succeeded, not just uploaded |
| GitHub Pages empty | Clear cache, wait 2 min, check settings |
| Emulator slow | Use smaller API level, disable `-gpu`, `-no-boot-anim` |

---

## 📁 Key Files

```
.github/workflows/
  ├─ main.yml                 (Primary pipeline)
  ├─ android-emulator.yml     (Android only)
  └─ ios-simulator.yml        (iOS only)

src/test/
  ├─ java/                    (Test code)
  └─ resources/               (Test data, features)

target/
  ├─ allure-results/         (Allure data)
  ├─ surefire-reports/       (TestNG data)
  └─ classes/                (Compiled tests)

CI_CD_PIPELINE.md            (Architecture)
GITHUB_ACTIONS_SETUP.md      (Setup steps)
APPIUM_CONFIGURATION.md      (Appium config)
TROUBLESHOOTING.md           (Debug guide)
```

---

## 🎬 Running Workflows

### Manual Trigger
1. `Actions` tab
2. Select workflow
3. `Run workflow`
4. Configure inputs
5. `Run workflow`

### Via CLI
```bash
gh workflow run main.yml \
  -f executionType=local-android \
  -f tags="@smoke" \
  -f uploadArtifacts=true
```

---

## 📊 Artifact Retention

| Type | Path | Days | Size |
|------|------|------|------|
| Allure Results | `allure-results/` | 30 | ~50 MB |
| Test Reports | `test-output/` | 30 | ~20 MB |
| Screenshots | `screenshots/` | 14 | ~100 MB |
| Logs | `logs/` | 7 | ~10 MB |

---

## ✅ Pre-Push Checklist

- [ ] Tests pass locally: `mvn test`
- [ ] No hardcoded credentials
- [ ] Workflow syntax valid: `actionlint`
- [ ] Proper tags on scenarios: `@smoke`, `@critical`
- [ ] All dependencies in `pom.xml`
- [ ] Code committed to branch

---

## 🔗 Useful Links

- **GitHub Actions**: https://docs.github.com/en/actions
- **Appium Docs**: https://appium.io/docs/
- **Allure Docs**: https://docs.qameta.io/allure/
- **TestNG Docs**: https://testng.org/

---

## 🆘 Emergency Procedures

### Disable Workflow Quickly
```bash
mv .github/workflows/main.yml .github/workflows/main.yml.bak
git push  # Stops automatic triggers
```

### Revert to Previous Version
```bash
git log --oneline | head -5  # Find commit
git revert <commit-hash>
git push
```

### Kill Hanging Process
```bash
# Kill Appium
pkill -f appium

# Kill Emulator
pkill -f emulator

# Kill Java
pkill -f java
```

---

## 📱 Common Appium Capabilities

```json
{
  "platformName": "Android/iOS",
  "appium:automationName": "UIAutomator2/XCUITest",
  "appium:deviceName": "emulator-5554/iPhone 15",
  "appium:app": "/path/to/app.apk",
  "appium:autoGrantPermissions": true,
  "newCommandTimeout": 300
}
```

---

## 🎯 Test Execution Status

Check status at: `https://github.com/ORG/REPO/actions`

**Symbols**:
- 🟢 Success
- 🔴 Failure
- 🟡 In Progress
- ⚪ Queued

---

## 💾 Environment Variables

```bash
# Set locally
export APPIUM_HOST=127.0.0.1
export APPIUM_PORT=4723
export DEVICE_UDID=emulator-5554

# In workflow
env:
  APPIUM_HOST: 127.0.0.1
  APPIUM_PORT: 4723
```

---

## 🔐 Security Reminders

✅ Use GitHub Secrets for:
- Credentials (BrowserStack)
- API keys
- Webhook URLs
- Private tokens

❌ Never commit:
- Passwords or tokens
- Private keys
- API credentials
- Sensitive test data

---

## 📞 Quick Help

**Workflow not starting?**
→ Check branch and file location

**Tests failing on CI only?**
→ Check timeouts and environment differences

**Artifacts missing?**
→ Check test execution succeeded

**Need details?**
→ See full documentation files

---

## 🗓️ Maintenance Tasks

```
Weekly:  Review failed runs
Monthly: Update dependencies, rotate credentials
Quarterly: Full system audit
As needed: Add new tests/features
```

---

## 📈 Monitor Performance

Expected metrics:
- **Success rate**: > 95%
- **Execution time**: 35-50 minutes
- **Artifact size**: 150-200 MB
- **Report load time**: < 5 seconds

---

## 🎓 Learning Resources

Start with:
1. CI_CD_README.md (overview)
2. GITHUB_ACTIONS_SETUP.md (steps)
3. CI_CD_PIPELINE.md (details)
4. TROUBLESHOOTING.md (fixes)

---

**Last Updated**: 2024-05-13  
**Laminate for Reference!**
