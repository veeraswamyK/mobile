package stepdefinitions;

import io.appium.java_client.AppiumBy;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AssertionUtils;
import utils.ContextManager;
import utils.GestureUtils;
import utils.ScreenshotUtils;
import utils.WaitUtils;
import core.DriverManager;
import constants.FrameworkConstants;

/**
 * ═══════════════════════════════════════════════════════════════
 *  BaseStepDefinitions — Ready-to-Use Generic Cucumber Steps
 * ═══════════════════════════════════════════════════════════════
 *
 *  WHAT THIS CLASS IS:
 *  A set of generic step definitions that work for ANY mobile app
 *  without modification. These steps cover the most common testing
 *  actions — gestures, waits, visibility checks, data storage, and
 *  app lifecycle — using plain English that matches Gherkin naturally.
 *
 *  HOW TO USE:
 *  1. These steps are automatically picked up by Cucumber (no config needed).
 *  2. Just use them in your .feature files directly.
 *  3. Write your APP-SPECIFIC steps in separate step definition classes.
 *
 *  EXAMPLE FEATURE FILE:
 *  ─────────────────────────────────────────────────────────────
 *  Feature: Product browsing
 *
 *    Scenario: User scrolls to a product and taps it
 *      Given the app is on the home screen          ← YOUR step
 *      When I scroll down                           ← BaseStepDefinitions
 *      And I tap on the element with text "Backpack" ← BaseStepDefinitions
 *      Then the element with text "Add to Cart" is visible ← BaseStepDefinitions
 *  ─────────────────────────────────────────────────────────────
 *
 *  STEP GROUPS:
 *  ── GESTURES
 *     "I scroll up/down"
 *     "I scroll up/down {int} times"
 *     "I swipe left/right"
 *     "I swipe left on the element with text {string}"
 *     "I long press on the element with text {string}"
 *  ── TAPS & CLICKS
 *     "I tap on the element with text {string}"
 *     "I tap on the element with accessibility id {string}"
 *     "I tap on the element with id {string}"
 *     "I double tap on the element with text {string}"
 *  ── TEXT INPUT
 *     "I type {string} into the field with hint {string}"
 *     "I type {string} into the field with id {string}"
 *     "I clear the field with hint {string}"
 *     "I clear the field with id {string}"
 *  ── WAITS
 *     "I wait for {int} seconds"
 *     "I wait until the element with text {string} is visible"
 *     "I wait until the element with text {string} disappears"
 *  ── VISIBILITY ASSERTIONS
 *     "the element with text {string} is visible"
 *     "the element with text {string} is not visible"
 *     "the element with id {string} is visible"
 *     "the element with accessibility id {string} is visible"
 *  ── APP LIFECYCLE
 *     "I send the app to the background for {int} seconds"
 *     "I restart the app"
 *     "I hide the keyboard"
 *  ── CONTEXT / DATA STORAGE
 *     "I save the text of element with id {string} as {string}"
 *     "the saved value {string} equals {string}"
 *  ── REPORTING
 *     "I take a screenshot"
 */
public class BaseStepDefinitions {

    private static final Logger LOG = LoggerFactory.getLogger(BaseStepDefinitions.class);

    // ════════════════════════════════════════════════════════════
    //  GESTURES — SCROLL
    // ════════════════════════════════════════════════════════════

    /**
     * Performs one swipe-up gesture (scrolls page down / reveals content below).
     * Use: "When I scroll down"
     */
    @When("I scroll down")
    @Step("Scroll down")
    public void iScrollDown() {
        LOG.info("Step: I scroll down");
        GestureUtils.swipeUp();
    }

    /**
     * Performs one swipe-down gesture (scrolls page up / reveals content above).
     * Use: "When I scroll up"
     */
    @When("I scroll up")
    @Step("Scroll up")
    public void iScrollUp() {
        LOG.info("Step: I scroll up");
        GestureUtils.swipeDown();
    }

    /**
     * Scrolls down multiple times.
     * Use: "When I scroll down 3 times"
     */
    @When("I scroll down {int} times")
    @Step("Scroll down {times} times")
    public void iScrollDownTimes(int times) {
        LOG.info("Step: I scroll down {} times", times);
        for (int i = 0; i < times; i++) GestureUtils.swipeUp();
    }

    /**
     * Scrolls up multiple times.
     * Use: "When I scroll up 3 times"
     */
    @When("I scroll up {int} times")
    @Step("Scroll up {times} times")
    public void iScrollUpTimes(int times) {
        LOG.info("Step: I scroll up {} times", times);
        for (int i = 0; i < times; i++) GestureUtils.swipeDown();
    }

    /**
     * Scrolls to the very top of the screen.
     * Use: "When I scroll to the top"
     */
    @When("I scroll to the top")
    @Step("Scroll to top")
    public void iScrollToTop() {
        LOG.info("Step: I scroll to the top");
        GestureUtils.scrollToTop();
    }

    /**
     * Scrolls to the very bottom of the screen.
     * Use: "When I scroll to the bottom"
     */
    @When("I scroll to the bottom")
    @Step("Scroll to bottom")
    public void iScrollToBottom() {
        LOG.info("Step: I scroll to the bottom");
        GestureUtils.scrollToBottom();
    }

    // ════════════════════════════════════════════════════════════
    //  GESTURES — SWIPE
    // ════════════════════════════════════════════════════════════

    /**
     * Swipes left across the screen (next card/page in carousel).
     * Use: "When I swipe left"
     */
    @When("I swipe left")
    @Step("Swipe left")
    public void iSwipeLeft() {
        LOG.info("Step: I swipe left");
        GestureUtils.swipeLeft();
    }

    /**
     * Swipes right across the screen (previous card/page in carousel).
     * Use: "When I swipe right"
     */
    @When("I swipe right")
    @Step("Swipe right")
    public void iSwipeRight() {
        LOG.info("Step: I swipe right");
        GestureUtils.swipeRight();
    }

    /**
     * Swipes left on a list item identified by its visible text.
     * Use: "When I swipe left on the element with text 'Delete Me'"
     */
    @When("I swipe left on the element with text {string}")
    @Step("Swipe left on element with text: {text}")
    public void iSwipeLeftOnElementWithText(String text) {
        LOG.info("Step: I swipe left on element with text '{}'", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        GestureUtils.swipeLeftOnElement(WaitUtils.waitForVisibility(locator));
    }

    /**
     * Swipes right on a list item identified by its visible text.
     * Use: "When I swipe right on the element with text 'Archive'"
     */
    @When("I swipe right on the element with text {string}")
    @Step("Swipe right on element with text: {text}")
    public void iSwipeRightOnElementWithText(String text) {
        LOG.info("Step: I swipe right on element with text '{}'", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        GestureUtils.swipeRightOnElement(WaitUtils.waitForVisibility(locator));
    }

    /**
     * Long-presses an element identified by its visible text.
     * Duration: DEFAULT_LONG_PRESS_MS (1500 ms)
     * Use: "When I long press on the element with text 'Hold Me'"
     */
    @When("I long press on the element with text {string}")
    @Step("Long press on element with text: {text}")
    public void iLongPressOnElementWithText(String text) {
        LOG.info("Step: I long press on element with text '{}'", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        GestureUtils.longPressElement(WaitUtils.waitForVisibility(locator),
                FrameworkConstants.DEFAULT_LONG_PRESS_MS);
    }

    // ════════════════════════════════════════════════════════════
    //  TAPS & CLICKS
    // ════════════════════════════════════════════════════════════

    /**
     * Waits for an element with the given visible text to be clickable, then taps it.
     * Use: "When I tap on the element with text 'Login'"
     */
    @When("I tap on the element with text {string}")
    @Step("Tap element with text: {text}")
    public void iTapOnElementWithText(String text) {
        LOG.info("Step: I tap on element with text '{}'", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        WaitUtils.waitForClickability(locator).click();
    }

    /**
     * Taps an element identified by its Accessibility ID (content-desc on Android,
     * accessibility identifier on iOS).
     * Use: "When I tap on the element with accessibility id 'test-LOGIN'"
     */
    @When("I tap on the element with accessibility id {string}")
    @Step("Tap element with accessibility id: {accessibilityId}")
    public void iTapOnElementWithAccessibilityId(String accessibilityId) {
        LOG.info("Step: I tap on element with accessibility id '{}'", accessibilityId);
        By locator = AppiumBy.accessibilityId(accessibilityId);
        WaitUtils.waitForClickability(locator).click();
    }

    /**
     * Taps an element identified by its resource-id (Android) or name (iOS).
     * Use: "When I tap on the element with id 'com.example:id/login_button'"
     */
    @When("I tap on the element with id {string}")
    @Step("Tap element with id: {id}")
    public void iTapOnElementWithId(String id) {
        LOG.info("Step: I tap on element with id '{}'", id);
        By locator = By.id(id);
        WaitUtils.waitForClickability(locator).click();
    }

    /**
     * Performs a double-tap on an element identified by its visible text.
     * Use: "When I double tap on the element with text 'Map Area'"
     */
    @When("I double tap on the element with text {string}")
    @Step("Double tap on element with text: {text}")
    public void iDoubleTapOnElementWithText(String text) {
        LOG.info("Step: I double tap on element with text '{}'", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        GestureUtils.doubleTap(WaitUtils.waitForVisibility(locator));
    }

    // ════════════════════════════════════════════════════════════
    //  TEXT INPUT
    // ════════════════════════════════════════════════════════════

    /**
     * Finds an input field by its hint text (placeholder), clears it, and types the value.
     * Use: "When I type 'john@example.com' into the field with hint 'Enter email'"
     */
    @When("I type {string} into the field with hint {string}")
    @Step("Type '{value}' into field with hint '{hint}'")
    public void iTypeIntoFieldWithHint(String value, String hint) {
        LOG.info("Step: I type '{}' into field with hint '{}'", value, hint);
        By locator = By.xpath("//*[@hint='" + hint + "']");
        var el = WaitUtils.waitForClickability(locator);
        el.clear();
        el.sendKeys(value);
    }

    /**
     * Finds an input field by its resource id, clears it, and types the value.
     * Use: "When I type 'password123' into the field with id 'test-Password'"
     */
    @When("I type {string} into the field with id {string}")
    @Step("Type '{value}' into field with id '{id}'")
    public void iTypeIntoFieldWithId(String value, String id) {
        LOG.info("Step: I type '{}' into field with id '{}'", value, id);
        By locator = By.id(id);
        var el = WaitUtils.waitForClickability(locator);
        el.clear();
        el.sendKeys(value);
    }

    /**
     * Finds an input field by its accessibility id, clears it, and types the value.
     * Use: "When I type 'admin' into the field with accessibility id 'test-Username'"
     */
    @When("I type {string} into the field with accessibility id {string}")
    @Step("Type '{value}' into field with accessibility id '{accessId}'")
    public void iTypeIntoFieldWithAccessibilityId(String value, String accessId) {
        LOG.info("Step: I type '{}' into field with accessibility id '{}'", value, accessId);
        By locator = AppiumBy.accessibilityId(accessId);
        var el = WaitUtils.waitForClickability(locator);
        el.clear();
        el.sendKeys(value);
    }

    /**
     * Clears the text of an input field identified by its hint text.
     * Use: "When I clear the field with hint 'Search...'"
     */
    @When("I clear the field with hint {string}")
    @Step("Clear field with hint '{hint}'")
    public void iClearFieldWithHint(String hint) {
        LOG.info("Step: I clear field with hint '{}'", hint);
        By locator = By.xpath("//*[@hint='" + hint + "']");
        WaitUtils.waitForClickability(locator).clear();
    }

    /**
     * Clears the text of an input field identified by its resource id.
     * Use: "When I clear the field with id 'search_input'"
     */
    @When("I clear the field with id {string}")
    @Step("Clear field with id '{id}'")
    public void iClearFieldWithId(String id) {
        LOG.info("Step: I clear field with id '{}'", id);
        WaitUtils.waitForClickability(By.id(id)).clear();
    }

    // ════════════════════════════════════════════════════════════
    //  WAITS
    // ════════════════════════════════════════════════════════════

    /**
     * Pauses the test for the given number of seconds.
     * NOTE: Prefer the other wait steps over this one — explicit waits are better
     * than fixed sleeps. Use this only for intentional delays (e.g., waiting for
     * an animation you can't detect with a condition).
     *
     * Use: "And I wait for 2 seconds"
     */
    @And("I wait for {int} seconds")
    @Step("Wait {seconds} seconds")
    public void iWaitForSeconds(int seconds) throws InterruptedException {
        LOG.info("Step: I wait for {} seconds", seconds);
        Thread.sleep((long) seconds * 1000);
    }

    /**
     * Waits until the element with the given text is VISIBLE on screen.
     * Polls every 500 ms up to EXPLICIT_WAIT_SECONDS.
     * Use: "And I wait until the element with text 'Welcome' is visible"
     */
    @And("I wait until the element with text {string} is visible")
    @Step("Wait until element with text '{text}' is visible")
    public void iWaitUntilElementWithTextIsVisible(String text) {
        LOG.info("Step: I wait until element with text '{}' is visible", text);
        WaitUtils.waitForVisibility(By.xpath("//*[@text='" + text + "']"));
    }

    /**
     * Waits until the element with the given text DISAPPEARS from the screen.
     * Use: "And I wait until the element with text 'Loading...' disappears"
     */
    @And("I wait until the element with text {string} disappears")
    @Step("Wait until element with text '{text}' disappears")
    public void iWaitUntilElementWithTextDisappears(String text) {
        LOG.info("Step: I wait until element with text '{}' disappears", text);
        WaitUtils.waitForInvisibility(By.xpath("//*[@text='" + text + "']"));
    }

    // ════════════════════════════════════════════════════════════
    //  VISIBILITY ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts that an element with the given visible text IS present on screen.
     * Use: "Then the element with text 'Login successful' is visible"
     */
    @Then("the element with text {string} is visible")
    @Step("Assert element with text '{text}' is visible")
    public void elementWithTextIsVisible(String text) {
        LOG.info("Step: element with text '{}' is visible", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        AssertionUtils.assertVisible(locator, "Element with text '" + text + "' should be visible");
    }

    /**
     * Asserts that an element with the given visible text is NOT present on screen.
     * Use: "Then the element with text 'Error' is not visible"
     */
    @Then("the element with text {string} is not visible")
    @Step("Assert element with text '{text}' is NOT visible")
    public void elementWithTextIsNotVisible(String text) {
        LOG.info("Step: element with text '{}' is not visible", text);
        By locator = By.xpath("//*[@text='" + text + "']");
        AssertionUtils.assertInvisible(locator, "Element with text '" + text + "' should NOT be visible");
    }

    /**
     * Asserts that an element with the given resource id IS visible.
     * Use: "Then the element with id 'success_message' is visible"
     */
    @Then("the element with id {string} is visible")
    @Step("Assert element with id '{id}' is visible")
    public void elementWithIdIsVisible(String id) {
        LOG.info("Step: element with id '{}' is visible", id);
        AssertionUtils.assertVisible(By.id(id), "Element with id '" + id + "' should be visible");
    }

    /**
     * Asserts that an element with the given accessibility id IS visible.
     * Use: "Then the element with accessibility id 'test-PRODUCTS' is visible"
     */
    @Then("the element with accessibility id {string} is visible")
    @Step("Assert element with accessibility id '{accessId}' is visible")
    public void elementWithAccessibilityIdIsVisible(String accessId) {
        LOG.info("Step: element with accessibility id '{}' is visible", accessId);
        AssertionUtils.assertVisible(AppiumBy.accessibilityId(accessId),
                "Element with accessibility id '" + accessId + "' should be visible");
    }

    // ════════════════════════════════════════════════════════════
    //  APP LIFECYCLE
    // ════════════════════════════════════════════════════════════

    /**
     * Sends the app to background for the given seconds, then restores it.
     * Use: "When I send the app to the background for 3 seconds"
     */
    @When("I send the app to the background for {int} seconds")
    @Step("Send app to background for {seconds}s")
    public void iSendAppToBackground(int seconds) {
        LOG.info("Step: I send app to background for {} seconds", seconds);
        DriverManager.getDriver().runAppInBackground(java.time.Duration.ofSeconds(seconds));
    }

    /**
     * Terminates the app and relaunches it (force-stop + reopen).
     * Use: "When I restart the app"
     */
    @When("I restart the app")
    @Step("Restart the app")
    public void iRestartTheApp() {
        LOG.info("Step: I restart the app");
        DriverManager.getDriver().terminateApp(constants.MobileConstants.APP_PACKAGE);
        DriverManager.getDriver().activateApp(constants.MobileConstants.APP_PACKAGE);
    }

    /**
     * Hides the soft keyboard if it is visible.
     * Use: "And I hide the keyboard"
     */
    @And("I hide the keyboard")
    @Step("Hide keyboard")
    public void iHideTheKeyboard() {
        LOG.info("Step: I hide the keyboard");
        try {
            DriverManager.getDriver().hideKeyboard();
        } catch (Exception e) {
            LOG.debug("Keyboard not present — skipping hideKeyboard");
        }
    }

    // ════════════════════════════════════════════════════════════
    //  CONTEXT / DATA STORAGE
    // ════════════════════════════════════════════════════════════

    /**
     * Reads the visible text of the element with the given resource id
     * and stores it in the test context under the given key.
     *
     * Use in multi-step data flows where a value from step 1 is needed in step 3.
     * Use: "When I save the text of element with id 'order_number' as 'savedOrderNumber'"
     * Then later: "And the saved value 'savedOrderNumber' equals '12345'"
     */
    @When("I save the text of element with id {string} as {string}")
    @Step("Save text of element '{id}' as '{key}'")
    public void iSaveTextOfElementAsKey(String id, String key) {
        LOG.info("Step: I save text of element '{}' as '{}'", id, key);
        String text = WaitUtils.waitForVisibility(By.id(id)).getText();
        ContextManager.getContext().set(key, text);
        LOG.debug("Saved '{}' = '{}'", key, text);
    }

    /**
     * Asserts that the previously saved value (stored in the test context under key)
     * exactly equals the expected string.
     * Use: "Then the saved value 'orderNumber' equals '#ORD-001'"
     */
    @Then("the saved value {string} equals {string}")
    @Step("Assert saved value '{key}' = '{expected}'")
    public void theSavedValueEquals(String key, String expected) {
        LOG.info("Step: the saved value '{}' equals '{}'", key, expected);
        String actual = ContextManager.getContext().get(key, String.class);
        AssertionUtils.assertFalse(actual == null,
                "Expected context key '" + key + "' to have a value, but it was never saved.");
        AssertionUtils.assertTrue(expected.equals(actual),
                String.format("Saved value '%s': expected '%s' but was '%s'", key, expected, actual));
    }

    // ════════════════════════════════════════════════════════════
    //  REPORTING
    // ════════════════════════════════════════════════════════════

    /**
     * Captures a screenshot and attaches it to the Allure report.
     * Use: "And I take a screenshot"
     * Useful for visual checkpoints in manual review of test runs.
     */
    @And("I take a screenshot")
    @Step("Take screenshot")
    public void iTakeAScreenshot() {
        LOG.info("Step: I take a screenshot");
        ScreenshotUtils.capture("manual-checkpoint-" + System.currentTimeMillis());
    }
}
