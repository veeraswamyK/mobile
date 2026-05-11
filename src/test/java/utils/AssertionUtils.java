package utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.slf4j.Logger;

import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════
 *  AssertionUtils — Mobile-Specific Assertion Library
 * ═══════════════════════════════════════════════════════════════
 *
 *  WHAT THIS CLASS IS:
 *  A library of assertion methods tailored for mobile automation.
 *  Every method wraps a TestNG Assert call with:
 *    1. A clear failure message that names the locator and actual value
 *    2. An Allure @Step annotation so the assertion appears in the report
 *    3. Logging of the assertion being made
 *
 *  WHY USE THIS INSTEAD OF ASSERT.ASSERT*:
 *  Raw TestNG asserts give messages like:
 *    "expected [true] but found [false]"
 *  These methods give messages like:
 *    "Expected element 'By.id: submit_btn' to be visible, but it was not.
 *     Custom context: Login button should appear after valid credentials"
 *
 *  HOW TO USE:
 *  Call from step definition @Then steps:
 *    AssertionUtils.assertVisible(LOGIN_BTN, "Login button visible on home screen");
 *
 *  Or from BasePage assertion wrapper methods:
 *    assertVisible(locator, "message");  ← calls AssertionUtils internally
 *
 *  AVAILABLE ASSERTIONS:
 *  ── VISIBILITY
 *     assertVisible / assertInvisible
 *  ── TEXT
 *     assertTextEquals / assertTextContains / assertTextNotEquals
 *  ── STATE
 *     assertEnabled / assertDisabled / assertChecked / assertSelected
 *  ── COUNT
 *     assertCount / assertCountAtLeast
 *  ── ATTRIBUTE
 *     assertAttributeEquals / assertAttributeContains
 *  ── GENERIC (boolean)
 *     assertTrue / assertFalse
 */
public final class AssertionUtils {

    private static final Logger LOG = LoggerUtil.getLogger(AssertionUtils.class);

    private AssertionUtils() {}

    // ════════════════════════════════════════════════════════════
    //  VISIBILITY ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts that the element IS VISIBLE on screen.
     * Uses SHORT_WAIT for the check — fails immediately if not visible.
     *
     * @param locator the element to check
     * @param message context shown in the failure report (what should be visible and why)
     */
    @Step("Assert visible: {message}")
    public static void assertVisible(By locator, String message) {
        LOG.debug("assertVisible: {} — {}", locator, message);
        boolean visible = WaitUtils.isVisible(locator);
        Assert.assertTrue(visible,
                String.format("[assertVisible FAILED] %s | locator: %s", message, locator));
    }

    /**
     * Asserts that the element is NOT VISIBLE (absent or hidden).
     *
     * @param locator the element that should not be present
     * @param message context for the failure report
     */
    @Step("Assert NOT visible: {message}")
    public static void assertInvisible(By locator, String message) {
        LOG.debug("assertInvisible: {} — {}", locator, message);
        boolean visible = WaitUtils.isVisible(locator);
        Assert.assertFalse(visible,
                String.format("[assertInvisible FAILED] %s | locator: %s", message, locator));
    }

    // ════════════════════════════════════════════════════════════
    //  TEXT ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts the element's text EXACTLY EQUALS the expected string.
     * Waits for the element to be visible first.
     *
     * @param locator  element to read text from
     * @param expected the exact expected text (case-sensitive)
     * @param message  context for the failure report
     */
    @Step("Assert text equals '{expected}': {message}")
    public static void assertTextEquals(By locator, String expected, String message) {
        String actual = WaitUtils.waitForVisibility(locator).getText();
        LOG.debug("assertTextEquals expected='{}' actual='{}': {}", expected, actual, locator);
        Assert.assertEquals(actual, expected,
                String.format("[assertTextEquals FAILED] %s | locator: %s | expected: '%s' | actual: '%s'",
                        message, locator, expected, actual));
    }

    /**
     * Asserts the element's text CONTAINS the expected substring.
     * Use when only part of the text is known or dynamic.
     *
     * @param locator      element to read text from
     * @param partial      substring that must appear in the text
     * @param message      context for the failure report
     */
    @Step("Assert text contains '{partial}': {message}")
    public static void assertTextContains(By locator, String partial, String message) {
        String actual = WaitUtils.waitForVisibility(locator).getText();
        LOG.debug("assertTextContains partial='{}' actual='{}': {}", partial, actual, locator);
        Assert.assertTrue(actual.contains(partial),
                String.format("[assertTextContains FAILED] %s | locator: %s | expected to contain: '%s' | actual: '%s'",
                        message, locator, partial, actual));
    }

    /**
     * Asserts the element's text does NOT EQUAL the given string.
     * Use to verify that a field was updated away from a previous value.
     */
    @Step("Assert text NOT equals '{unexpected}': {message}")
    public static void assertTextNotEquals(By locator, String unexpected, String message) {
        String actual = WaitUtils.waitForVisibility(locator).getText();
        LOG.debug("assertTextNotEquals unexpected='{}' actual='{}': {}", unexpected, actual, locator);
        Assert.assertNotEquals(actual, unexpected,
                String.format("[assertTextNotEquals FAILED] %s | locator: %s | expected NOT to be: '%s'",
                        message, locator, unexpected));
    }

    // ════════════════════════════════════════════════════════════
    //  STATE ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts the element is ENABLED (interactable, not grayed out).
     * Reads the "enabled" attribute.
     */
    @Step("Assert enabled: {message}")
    public static void assertEnabled(By locator, String message) {
        boolean enabled = ElementUtils.isEnabled(locator);
        Assert.assertTrue(enabled,
                String.format("[assertEnabled FAILED] %s | locator: %s", message, locator));
    }

    /**
     * Asserts the element is DISABLED (not interactable).
     */
    @Step("Assert disabled: {message}")
    public static void assertDisabled(By locator, String message) {
        boolean enabled = ElementUtils.isEnabled(locator);
        Assert.assertFalse(enabled,
                String.format("[assertDisabled FAILED] %s | locator: %s", message, locator));
    }

    /**
     * Asserts the element is CHECKED (checkbox/toggle is ON).
     * Reads the "checked" attribute.
     */
    @Step("Assert checked: {message}")
    public static void assertChecked(By locator, String message) {
        boolean checked = ElementUtils.isChecked(locator);
        Assert.assertTrue(checked,
                String.format("[assertChecked FAILED] %s | locator: %s", message, locator));
    }

    /**
     * Asserts the element is NOT CHECKED (checkbox/toggle is OFF).
     */
    @Step("Assert NOT checked: {message}")
    public static void assertNotChecked(By locator, String message) {
        boolean checked = ElementUtils.isChecked(locator);
        Assert.assertFalse(checked,
                String.format("[assertNotChecked FAILED] %s | locator: %s", message, locator));
    }

    /**
     * Asserts the element IS SELECTED (radio button, tab, list item).
     */
    @Step("Assert selected: {message}")
    public static void assertSelected(By locator, String message) {
        boolean selected = ElementUtils.isSelected(locator);
        Assert.assertTrue(selected,
                String.format("[assertSelected FAILED] %s | locator: %s", message, locator));
    }

    // ════════════════════════════════════════════════════════════
    //  COUNT ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts that the number of elements matching the locator EQUALS expected.
     * Use to verify list sizes, badge counts, or that items were removed.
     *
     * @param locator  locator to count
     * @param expected the expected count
     * @param message  context for failure report
     */
    @Step("Assert count = {expected}: {message}")
    public static void assertCount(By locator, int expected, String message) {
        int actual = WaitUtils.waitForVisibility(locator) != null
                ? core.DriverManager.getDriver().findElements(locator).size() : 0;
        Assert.assertEquals(actual, expected,
                String.format("[assertCount FAILED] %s | locator: %s | expected: %d | actual: %d",
                        message, locator, expected, actual));
    }

    /**
     * Asserts that the number of elements is AT LEAST minCount.
     * Use when you know "at least N" items should exist.
     */
    @Step("Assert count >= {minCount}: {message}")
    public static void assertCountAtLeast(By locator, int minCount, String message) {
        int actual = core.DriverManager.getDriver().findElements(locator).size();
        Assert.assertTrue(actual >= minCount,
                String.format("[assertCountAtLeast FAILED] %s | locator: %s | expected >= %d | actual: %d",
                        message, locator, minCount, actual));
    }

    // ════════════════════════════════════════════════════════════
    //  ATTRIBUTE ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts that the named attribute of the element EQUALS the expected value.
     * Exact string comparison (case-sensitive).
     *
     * Example:
     *   assertAttributeEquals(TOGGLE, "checked", "true", "Dark mode toggle is on");
     */
    @Step("Assert attribute '{attribute}' = '{expected}': {message}")
    public static void assertAttributeEquals(By locator, String attribute, String expected, String message) {
        String actual = WaitUtils.waitForVisibility(locator).getAttribute(attribute);
        Assert.assertEquals(actual, expected,
                String.format("[assertAttributeEquals FAILED] %s | locator: %s | attr: %s | expected: '%s' | actual: '%s'",
                        message, locator, attribute, expected, actual));
    }

    /**
     * Asserts that the named attribute CONTAINS the expected substring.
     */
    @Step("Assert attribute '{attribute}' contains '{expected}': {message}")
    public static void assertAttributeContains(By locator, String attribute, String expected, String message) {
        String actual = WaitUtils.waitForVisibility(locator).getAttribute(attribute);
        Assert.assertNotNull(actual,
                String.format("[assertAttributeContains FAILED] attribute '%s' was null on %s", attribute, locator));
        Assert.assertTrue(actual.contains(expected),
                String.format("[assertAttributeContains FAILED] %s | attr: %s | expected to contain: '%s' | actual: '%s'",
                        message, attribute, expected, actual));
    }

    // ════════════════════════════════════════════════════════════
    //  GENERIC BOOLEAN ASSERTIONS
    // ════════════════════════════════════════════════════════════

    /**
     * Asserts that a boolean condition IS TRUE.
     * Use when none of the typed assertions above fit your scenario.
     */
    @Step("Assert true: {message}")
    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition,
                String.format("[assertTrue FAILED] %s", message));
    }

    /**
     * Asserts that a boolean condition IS FALSE.
     */
    @Step("Assert false: {message}")
    public static void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition,
                String.format("[assertFalse FAILED] %s", message));
    }

    /**
     * Asserts that a list of strings CONTAINS the expected string.
     * Use with ElementUtils.getTexts() to check list contents.
     *
     * Example:
     *   assertListContains(getTexts(PRODUCT_NAMES), "Sauce Labs Backpack",
     *                      "Product should appear in the catalog");
     */
    @Step("Assert list contains '{expected}': {message}")
    public static void assertListContains(List<String> actualList, String expected, String message) {
        Assert.assertTrue(actualList.contains(expected),
                String.format("[assertListContains FAILED] %s | expected: '%s' | actual list: %s",
                        message, expected, actualList));
    }
}
