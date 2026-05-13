# GitHub Actions Setup Guide

## Step 1: Repository Configuration

### Enable GitHub Pages

1. Go to `Settings > Pages`
2. Select:
   - **Source**: Deploy from a branch
   - **Branch**: `gh-pages`
   - **Folder**: `/ (root)`
3. Click `Save`

### Enable Actions

1. Go to `Settings > Actions > General`
2. Under "Actions permissions":
   - Select: **Allow all actions and reusable workflows**
3. Under "Workflow permissions":
   - Select: **Read and write permissions**
   - Check: **Allow GitHub Actions to create and approve pull requests**
4. Click `Save`

---

## Step 2: Create Environments

### Production Environment

1. Go to `Settings > Environments`
2. Click `New environment`
3. Enter name: `production`
4. Click `Configure environment`
5. Add these secrets:
   - `BS_USER`
   - `BS_KEY`
   - `BS_APP_ID`
   - `SLACK_WEBHOOK_URL` (optional)

### Staging Environment (Optional)

For testing before production:

1. Create environment: `staging`
2. Add same secrets as `production`
3. Configure deployment protection rules

---

## Step 3: Add Secrets

### BrowserStack Credentials

1. Go to `Settings > Secrets and variables > Actions`
2. Click `New repository secret`

**For Cloud Testing (BrowserStack)**:

| Secret Name | Value | Where to Get |
|-------------|-------|--------------|
| `BS_USER` | Your BrowserStack username | BrowserStack account page |
| `BS_KEY` | Your BrowserStack access key | BrowserStack account security |
| `BS_APP_ID` | App ID from BrowserStack upload | BrowserStack app settings |

**For Notifications (Optional)**:

| Secret Name | Value | Where to Get |
|-------------|-------|--------------|
| `SLACK_WEBHOOK_URL` | Slack webhook URL | Create in Slack workspace settings |

### Adding Secrets via GitHub CLI

```bash
# Login to GitHub
gh auth login

# Add BrowserStack secrets
gh secret set BS_USER --body "your_username"
gh secret set BS_KEY --body "your_access_key"
gh secret set BS_APP_ID --body "your_app_id"

# Add Slack webhook (optional)
gh secret set SLACK_WEBHOOK_URL --body "https://hooks.slack.com/services/..."

# Verify
gh secret list
```

---

## Step 4: Configure Branch Protection

### Protect Main Branch

1. Go to `Settings > Branches`
2. Click `Add rule`
3. Branch name pattern: `main`
4. Enable:
   - ✅ Require a pull request before merging
   - ✅ Require status checks to pass
   - ✅ Dismiss stale pull request approvals
   - ✅ Require code review before merging (recommended: 1 review)
5. Click `Create`

### Required Status Checks

After first pipeline run, you can require specific workflows:

1. Go to `Settings > Branches > main`
2. Under "Require status checks to pass before merging":
3. Select these checks:
   - `prepare`
   - `mobile-tests`
   - `publish-allure-report`

---

## Step 5: Configure Runners (Optional)

### GitHub-Hosted Runners (Default)

Current setup uses standard GitHub runners:
- **Ubuntu Latest** for Android/Cloud tests
- **macOS Latest** for iOS tests

No additional configuration needed.

### Self-Hosted Runners (Advanced)

For faster/cheaper builds on your own hardware:

1. Go to `Settings > Actions > Runners`
2. Click `New self-hosted runner`
3. Choose OS: Linux/macOS/Windows
4. Download and configure
5. Tag with meaningful name (e.g., `local-linux`, `fast-build`)

Then update workflows to use tagged runner:

```yaml
jobs:
  mobile-tests:
    runs-on: self-hosted  # Or use: ubuntu-latest
```

---

## Step 6: Slack Integration

### Create Slack Webhook

1. Go to your Slack workspace
2. Create app: https://api.slack.com/apps
3. Click `Create New App > From scratch`
4. Name: "GitHub Actions Bot"
5. Select workspace
6. Under `Features > Incoming Webhooks`:
   - Toggle: **On**
   - Click: **Add New Webhook to Workspace**
   - Select channel: **#test-reports** (or create new)
   - Click: **Allow**
7. Copy the **Webhook URL**

### Add Webhook to GitHub

1. Go to GitHub repo settings
2. `Settings > Secrets and variables > Actions`
3. Click `New repository secret`
4. Name: `SLACK_WEBHOOK_URL`
5. Value: Paste webhook URL from Slack
6. Click `Add secret`

### Test Webhook

```bash
# Test from command line
curl -X POST https://hooks.slack.com/services/YOUR/WEBHOOK/URL \
  -H 'Content-Type: application/json' \
  -d '{"text":"Test from GitHub Actions"}'
```

---

## Step 7: Repository Variables

### Add CI/CD Variables

For configurations that don't require secrecy:

1. Go to `Settings > Secrets and variables > Actions`
2. Select `Variables` tab
3. Click `New repository variable`

**Recommended variables**:

| Name | Value | Used For |
|------|-------|----------|
| `JAVA_VERSION` | `11` | Java setup |
| `MAVEN_VERSION` | `3.9.0` | Maven setup |
| `NODE_VERSION` | `18` | Node.js setup |
| `ALLURE_REPORT_URL` | `https://yourorg.github.io/repo` | Report link |

### Using Variables in Workflows

```yaml
steps:
  - name: Set up Java
    uses: actions/setup-java@v4
    with:
      java-version: ${{ vars.JAVA_VERSION }}
```

---

## Step 8: Local Testing Before Push

### Validate Workflow Syntax

```bash
# Install workflow linter
npm install -g @github/super-linter

# Validate all workflows
actionlint .github/workflows/*.yml

# Or use Docker
docker run -v $PWD:/repo ghcr.io/rhysd/actionlint:latest
```

### Test Locally with Act

Test GitHub Actions locally without pushing:

```bash
# Install act
brew install act  # macOS
# OR download from: https://github.com/nektos/act

# Run workflow locally
act -j mobile-tests -s GITHUB_TOKEN=your_token

# Run specific workflow
act push -W .github/workflows/main.yml
```

---

## Step 9: Workflow Monitoring

### View Workflow Runs

1. Go to `Actions` tab
2. Select workflow from list
3. View runs, logs, and artifacts

### Check Workflow Status

Via GitHub CLI:

```bash
# List recent runs
gh run list --workflow main.yml --limit 10

# View specific run details
gh run view <run-id>

# View run logs
gh run view <run-id> --log

# Download artifacts
gh run download <run-id> -n allure-results-local-android
```

### Real-time Log Monitoring

```bash
# Stream workflow run logs
gh run watch <run-id> --exit-status
```

---

## Step 10: Notifications & Reports

### Email Notifications

GitHub sends automatic emails for:
- ✅ Workflow failures
- ✅ Action required
- ✅ Deployment review requests

Configure in `Settings > Notifications`

### Pull Request Checks

When workflow runs on PR:
1. Checks appear at bottom of PR
2. Details available by clicking workflow name
3. Required to pass before merge (if branch protection enabled)

### Commit Status Badge

Add to `README.md`:

```markdown
## Pipeline Status

[![Mobile Tests](https://github.com/ORG/REPO/actions/workflows/main.yml/badge.svg)](https://github.com/ORG/REPO/actions)
[![Android Tests](https://github.com/ORG/REPO/actions/workflows/android-emulator.yml/badge.svg)](https://github.com/ORG/REPO/actions)
[![iOS Tests](https://github.com/ORG/REPO/actions/workflows/ios-simulator.yml/badge.svg)](https://github.com/ORG/REPO/actions)
```

---

## Troubleshooting Setup

### Workflows Not Triggering

**Problem**: Workflows aren't running on push  
**Solutions**:
1. Check `Settings > Actions > General > Allow all actions` is selected
2. Verify workflow files are in `.github/workflows/`
3. Check file extension is `.yml` (not `.yaml`)
4. Verify branch name matches trigger (e.g., `main` not `master`)

### Secrets Not Available

**Problem**: "Secret not found" error in workflow  
**Solutions**:
1. Verify secret exists in `Settings > Secrets`
2. Check exact spelling (case-sensitive)
3. For organization secrets, verify repo has access
4. Secrets must be added before workflow runs

### GitHub Pages Not Deploying

**Problem**: Reports not appearing on GitHub Pages  
**Solutions**:
1. Verify `Settings > Pages` is enabled
2. Check branch: must be set to `gh-pages`
3. Wait 1-2 minutes for deployment
4. Clear browser cache
5. Check `Deployments` tab for errors

### Permission Issues

**Problem**: "Permission denied" or "Unauthorized" errors  
**Solutions**:
1. Go to `Settings > Actions > General`
2. Ensure "Read and write permissions" selected
3. Check "Allow GitHub Actions to create and approve pull requests"
4. Repository tokens inherit from this setting

---

## Security Checklist

✅ **Before Going to Production**

- [ ] Created `production` environment
- [ ] Added all required secrets
- [ ] Validated secret values are correct
- [ ] Enabled branch protection on `main`
- [ ] Limited GitHub Actions permissions
- [ ] Configured Slack webhook (if using)
- [ ] Tested workflow with manual dispatch
- [ ] Reviewed artifact retention policies
- [ ] Enabled GitHub Pages
- [ ] Created status badges

✅ **Ongoing Maintenance**

- [ ] Rotate BrowserStack credentials monthly
- [ ] Review workflow logs for errors
- [ ] Monitor secret usage in audit logs
- [ ] Update dependencies regularly
- [ ] Archive old artifacts
- [ ] Review GitHub Pages deployment health

---

## Next Steps

1. **Update README.md** with pipeline badge and links
2. **Run first workflow**: Use `workflow_dispatch` to test manually
3. **Verify artifacts**: Check all reports generated correctly
4. **Configure Slack** (optional but recommended)
5. **Set up branch protection** for main branch
6. **Document** any project-specific configurations

---

## Support

- **GitHub Docs**: https://docs.github.com/en/actions
- **Actions Marketplace**: https://github.com/marketplace?type=actions
- **Troubleshooting**: Check workflow logs in Actions tab
- **Questions**: Open issue or contact DevOps team

