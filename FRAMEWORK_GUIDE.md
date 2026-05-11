# Mobile Automation Framework — Complete Guide

## What You Need to Do to Write a New Test

As a test author, your job is exactly **two things**:

1. **Create a Feature File** — describe the test in plain English (Gherkin)
2. **Create a Page Class** — map app screens to reusable actions

The framework handles everything else: driver lifecycle, waits, gestures, screenshots, retries, and Allure reporting.

---

## Architecture at a Glance

```
YOUR WORK                   FRAMEWORK (do not edit)
────────────────────────    ──────────────────────────────────────
feature files           ─►  Cucumber runner picks them up
page classes            ─►  extend BasePage (all actions built in)
step definitions        ─►  call page methods + BaseStepDefinitions
                            (generic steps already wired up)
                        ─►  WaitUtils      (explicit waits)
                        ─►  GestureUtils   (all touch gestures)
                        ─►  ElementUtils   (element inspection)
                        ─►  AssertionUtils (typed mobile assertions)
                        ─►  AppDataKeys    (typed context keys)
                        ─►  FrameworkConstants (timeouts, ratios)
                        ─►  TestHooks      (before/after lifecycle)
                        ─►  DriverManager  (thread-local driver)
```

---

## Step 1 — Create a Feature File

Location: `src/test/resources/features/`

```gherkin
@smoke
Feature: Shopping Cart
  As a logged-in user
  I want to add items to my cart
  So that I can purchase them

  Background:
    Given user logs in with username "standard_user" and password "secret_sauce"

  Scenario: Add a single product to cart
    When I tap on the element with text "Sauce Labs Backpack"
    And I tap on the element with accessibility id "test-ADD TO CART"
    Then the element with accessibility id "test-Cart" is visible
    And the element with text "1" is visible

  Scenario: Scroll to a product and add it
    When I scroll down 2 times
    And I tap on the element with text "Sauce Labs Fleece Jacket"
    And I tap on the element with accessibility id "test-ADD TO CART"
    Then the element with text "1" is visible
```

### Built-in Steps (no code required)

These steps work for **any app** out of the box — they're defined in `BaseStepDefinitions.java`.

**Scroll & Swipe**
```gherkin
When I scroll down
When I scroll up
When I scroll down 3 times
When I scroll up 3 times
When I scroll to the top
When I scroll to the bottom
When I swipe left
When I swipe right
When I swipe left on the element with text "Delete"
When I swipe right on the element with text "Archive"
When I long press on the element with text "Hold Me"
```

**Tap**
```gherkin
When I tap on the element with text "Login"
When I tap on the element with accessibility id "test-LOGIN"
When I tap on the element with id "com.example:id/btn_submit"
When I double tap on the element with text "Map"
```

**Text Input**
```gherkin
When I type "john@example.com" into the field with hint "Enter email"
When I type "password123" into the field with id "test-Password"
When I type "admin" into the field with accessibility id "test-Username"
When I clear the field with hint "Search..."
When I clear the field with id "search_input"
```

**Waits**
```gherkin
And I wait for 2 seconds
And I wait until the element with text "Welcome" is visible
And I wait until the element with text "Loading..." disappears
```

**Assertions**
```gherkin
Then the element with text "Login successful" is visible
Then the element with text "Error" is not visible
Then the element with id "success_message" is visible
Then the element with accessibility id "test-PRODUCTS" is visible
```

**App Lifecycle**
```gherkin
When I send the app to the background for 3 seconds
When I restart the app
And I hide the keyboard
```

**Data Sharing Between Steps**
```gherkin
When I save the text of element with id "order_number" as "savedOrder"
Then the saved value "savedOrder" equals "#ORD-001"
```

**Reporting**
```gherkin
And I take a screenshot
```

---

## Step 2 — Create a Page Class

Location: `src/test/java/pages/`

A page class represents **one screen** in your app. It:
- Declares private locators at the top
- Exposes public business methods that call BasePage's protected helpers

```java
package pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // ── Locators (private — never expose these to steps) ──────────
    private static final By USERNAME  = AppiumBy.accessibilityId("test-Username");
    private static final By PASSWORD  = AppiumBy.accessibilityId("test-Password");
    private static final By LOGIN_BTN = AppiumBy.accessibilityId("test-LOGIN");
    private static final By ERROR_MSG = AppiumBy.accessibilityId("test-Error message");

    // ── Business methods (public — called from step definitions) ──

    @Step("Login with username '{username}'")
    public void login(String username, String password) {
        waitUntilVisible(USERNAME);     // wait for page to load
        type(USERNAME, username);       // clear + type
        type(PASSWORD, password);
        hideKeyboard();                 // dismiss soft keyboard
        click(LOGIN_BTN);               // wait for clickable, then click
    }

    public boolean isDisplayed() {
        return isVisible(LOGIN_BTN);    // true/false, no exception
    }

    public String getErrorMessage() {
        return getText(ERROR_MSG);      // waits for visibility, returns text
    }

    public boolean isErrorMessageDisplayed() {
        return isVisible(ERROR_MSG);
    }
}
```

---

## Step 3 — Create Step Definitions (only for app-specific steps)

Location: `src/test/java/stepdefinitions/`

Only write step definitions that are specific to your app's business logic.
Generic gestures and assertions are already handled by `BaseStepDefinitions`.

```java
package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.LoginPage;
import pages.ProductsPage;

public class LoginSteps {

    private final LoginPage loginPage    = new LoginPage();
    private final ProductsPage products  = new ProductsPage();

    @Given("user logs in with username {string} and password {string}")
    @Step("Login with username='{0}'")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("products page is displayed")
    public void productsPageIsDisplayed() {
        Assert.assertTrue(products.isDisplayed(), "Products page should be visible after login");
    }

    @Then("login error message is displayed")
    public void loginErrorMessageIsDisplayed() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should appear for bad credentials");
    }
}
```

---

## BasePage Method Reference

Every page class inherits these from `BasePage`. Use them freely.

### Find Elements

| Method | When to use |
|--------|------------|
| `find(By)` | Before reading text or state |
| `find(By, seconds)` | Custom timeout |
| `findClickable(By)` | Before any click action |
| `findPresent(By)` | Element in DOM but off-screen |
| `findAll(By)` | Get all matching elements (no wait) |
| `findAllVisible(By)` | Wait for list to load, then get all |

### Click / Tap

| Method | When to use |
|--------|------------|
| `click(By)` | Standard button click (waits for clickable) |
| `clickVisible(By)` | Custom component whose "enabled" state is unreliable |
| `clickIfVisible(By)` | Optional dismiss/skip button |
| `tapCenter(By)` | Element in scroll container where click() fails |
| `tapAtOffset(By, xOffset, yOffset)` | Sub-region of element |
| `tapAt(x, y)` | Absolute coordinates (no element) |
| `doubleTap(By)` | Double-tap for zoom, word selection |

### Text Input

| Method | When to use |
|--------|------------|
| `type(By, text)` | Standard field — clears then types |
| `appendText(By, text)` | Add to existing content |
| `clearField(By)` | Clear only |
| `replaceText(By, text)` | Full replace (select-all delete + type) |
| `pressEnter(By)` | Submit/Search via keyboard |

### Read Text & Attributes

| Method | Returns |
|--------|---------|
| `getText(By)` | Visible label text |
| `getAttribute(By, attr)` | Any attribute value |
| `getContentDescription(By)` | Accessibility description |
| `getResourceId(By)` | Android resource-id |
| `getHintText(By)` | Placeholder / hint text |

### Visibility & State Checks

| Method | Returns |
|--------|---------|
| `isVisible(By)` | true/false, waits EXPLICIT_WAIT |
| `isVisibleQuick(By)` | true/false, waits SHORT_WAIT (5s) |
| `isPresent(By)` | true/false, DOM presence only |
| `isEnabled(By)` | true if interactable |
| `isSelected(By)` | true if selected/checked |
| `countElements(By)` | int — count of matching elements |

### Explicit Waits

| Method | Blocks until |
|--------|-------------|
| `waitUntilVisible(By)` | Element appears on screen |
| `waitUntilInvisible(By)` | Element disappears |
| `waitUntilTextIs(By, text)` | Text exactly equals |
| `waitUntilTextContains(By, text)` | Text contains substring |
| `waitUntilClickable(By)` | Element is enabled |
| `waitUntilAttributeIs(By, attr, val)` | Attribute equals value |
| `waitUntilCountIs(By, n, sec)` | Count reaches n |

### Scroll Gestures

| Method | Effect |
|--------|--------|
| `scrollDown()` | One swipe — reveals content below |
| `scrollUp()` | One swipe — reveals content above |
| `scrollDown(n)` | n swipes down |
| `scrollUp(n)` | n swipes up |
| `scrollDownToElement(By)` | Scrolls until element is in DOM |
| `scrollUpToElement(By)` | Scrolls up until element is in DOM |
| `scrollToTop()` | Goes to the top of the page |
| `scrollToBottom()` | Goes to the bottom of the page |
| `flingUp()` | Fast fling — momentum scroll down |
| `flingDown()` | Fast fling — momentum scroll up |

### Swipe Gestures

| Method | Effect |
|--------|--------|
| `swipeLeft()` | Full-screen left swipe |
| `swipeRight()` | Full-screen right swipe |
| `swipeLeftOnElement(By)` | Left swipe within element bounds |
| `swipeRightOnElement(By)` | Right swipe within element bounds |
| `swipe(x1,y1,x2,y2)` | Custom coordinate swipe |

### Advanced Gestures

| Method | Effect |
|--------|--------|
| `longPress(By)` | 1500ms long press |
| `longPress(By, ms)` | Custom duration |
| `doubleTap(By)` | Two quick taps |
| `dragToElement(By, By)` | Drag source to target element |
| `dragTo(By, x, y)` | Drag to coordinates |
| `pinchZoomIn()` | Two-finger spread |
| `pinchZoomOut()` | Two-finger pinch |
| `horizontalScrollToElement(By, By)` | Horizontal carousel scroll |

### Context Switching

| Method | Effect |
|--------|--------|
| `switchToWebView()` | Enter first available WebView |
| `switchToNative()` | Return to NATIVE_APP context |
| `switchToContext(name)` | Switch to specific context |
| `getContextHandles()` | List all available contexts |

### App Lifecycle

| Method | Effect |
|--------|--------|
| `launchApp()` | Bring app to foreground |
| `terminateApp()` | Kill app process |
| `sendAppToBackground(sec)` | Background then restore |
| `resetApp()` | Terminate + relaunch |

### Assertions (from step definitions)

| Method | Asserts |
|--------|---------|
| `assertVisible(By, msg)` | Element is on screen |
| `assertInvisible(By, msg)` | Element is gone |
| `assertTextEquals(By, expected, msg)` | Exact text match |
| `assertTextContains(By, partial, msg)` | Text contains substring |
| `assertCount(By, n, msg)` | Exact element count |

---

## Sharing Data Between Steps (AppDataKeys)

Use `ContextManager` with typed keys from `AppDataKeys`:

```java
// Step 1: store something
ContextManager.getContext().set(AppDataKeys.ORDER_NUMBER, orderNum);

// Step 2 (different class): retrieve it
String order = ContextManager.getContext().get(AppDataKeys.ORDER_NUMBER, String.class);
```

All context data is automatically cleared after each scenario.

---

## Configuration

Edit `src/test/resources/config/config.properties`:

```properties
# Execution mode: local-emulator | local-real | cloud | hybrid
executionType=local-emulator

platform=ANDROID
udid=emulator-5554
systemPort=8200
appPath=apps/app.apk
```

Tune timeouts and gesture speeds in `FrameworkConstants.java`.

---

## Running Tests

```bash
# All tests
mvn test

# Tagged tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Specific execution type
mvn test -DexecutionType=local-real

# Cloud execution
mvn test -DexecutionType=cloud -Dbrowserstack.user=X -DbrowserstackKey=Y
```

## Viewing Reports

```bash
# Generate and open Allure report
allure serve target/allure-results
```

---

## File Layout Summary

```
src/test/
├── java/
│   ├── constants/
│   │   ├── AppDataKeys.java        ← context key registry (no magic strings)
│   │   ├── FrameworkConstants.java ← all timeouts, ratios, durations
│   │   └── MobileConstants.java    ← platform, app identifiers
│   ├── core/                       ← driver + server management (do not edit)
│   ├── hooks/
│   │   └── TestHooks.java          ← before/after lifecycle (do not edit)
│   ├── pages/
│   │   ├── BasePage.java           ← ALL reusable actions live here
│   │   └── YourPage.java           ← YOUR page classes go here
│   ├── stepdefinitions/
│   │   ├── BaseStepDefinitions.java ← generic steps (auto-loaded, do not edit)
│   │   └── YourSteps.java           ← YOUR app-specific steps go here
│   └── utils/
│       ├── AssertionUtils.java     ← typed mobile assertions
│       ├── ElementUtils.java       ← element inspection helpers
│       ├── GestureUtils.java       ← complete gesture library
│       ├── WaitUtils.java          ← explicit wait strategies
│       ├── ContextManager.java     ← scenario-scoped data store
│       └── ScreenshotUtils.java    ← failure screenshots + Allure attach
└── resources/
    ├── features/
    │   └── your_feature.feature    ← YOUR feature files go here
    └── config/
        └── config.properties       ← execution configuration
```
