package pages;

import constants.FrameworkConstants;
import core.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GestureUtils;
import utils.WaitUtils;

import java.util.List;

/**
 * Base page providing all reusable mobile automation interactions.
 * Every page object extends this class — never call DriverManager directly from a page.
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // ─── Driver access ──────────────────────────────────────────────────────────

    protected AppiumDriver driver() {
        return DriverManager.getDriver();
    }

    // ─── Find ───────────────────────────────────────────────────────────────────

    /** Waits for element visibility, then returns it. */
    protected WebElement find(By locator) {
        return WaitUtils.waitForVisibility(locator);
    }

    /** Waits for element visibility with custom timeout. */
    protected WebElement find(By locator, int timeoutSeconds) {
        return WaitUtils.waitForVisibility(locator, timeoutSeconds);
    }

    /** Waits for clickability, then returns element. */
    protected WebElement findClickable(By locator) {
        return WaitUtils.waitForClickability(locator);
    }

    /** Returns all matching elements (no wait). */
    protected List<WebElement> findAll(By locator) {
        return driver().findElements(locator);
    }

    /** Returns all matching elements after waiting for at least one to be visible. */
    protected List<WebElement> findAllVisible(By locator) {
        return WaitUtils.waitForAllVisible(locator);
    }

    // ─── Tap / Click ────────────────────────────────────────────────────────────

    /** Waits for element to be clickable, then clicks it. */
    @Step("Click on element: {locator}")
    protected void click(By locator) {
        log.debug("Clicking: {}", locator);
        findClickable(locator).click();
    }

    /** Waits for element to be visible, then clicks it. */
    @Step("Click visible element: {locator}")
    protected void clickVisible(By locator) {
        find(locator).click();
    }

    /** Taps at the centre of the element referenced by locator. */
    @Step("Tap element at coordinates: {locator}")
    protected void tapCenter(By locator) {
        WebElement el = find(locator);
        int x = el.getLocation().getX() + el.getSize().getWidth() / 2;
        int y = el.getLocation().getY() + el.getSize().getHeight() / 2;
        GestureUtils.tapByCoordinates(x, y);
    }

    // ─── Text input ─────────────────────────────────────────────────────────────

    /** Clears the field, then types text. */
    @Step("Type '{text}' into: {locator}")
    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = findClickable(locator);
        el.clear();
        el.sendKeys(text);
    }

    /** Types text without clearing the field first. */
    @Step("Append '{text}' to: {locator}")
    protected void appendText(By locator, String text) {
        findClickable(locator).sendKeys(text);
    }

    /** Clears the field using select-all + delete. */
    @Step("Clear field: {locator}")
    protected void clearField(By locator) {
        WebElement el = findClickable(locator);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        el.clear();
    }

    // ─── Get text ───────────────────────────────────────────────────────────────

    /** Returns visible text of the element. */
    protected String getText(By locator) {
        return find(locator).getText();
    }

    /** Returns the value of an element attribute. */
    protected String getAttribute(By locator, String attribute) {
        return find(locator).getAttribute(attribute);
    }

    // ─── Visibility checks ──────────────────────────────────────────────────────

    /** True if the element is visible on screen (waits up to EXPLICIT_WAIT). */
    protected boolean isVisible(By locator) {
        return WaitUtils.isVisible(locator);
    }

    /** True if the element is visible with a short timeout (no exception thrown). */
    protected boolean isVisibleShort(By locator) {
        try {
            WaitUtils.waitForVisibility(locator, FrameworkConstants.SHORT_WAIT_SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** True if at least one element matching the locator exists in the DOM. */
    protected boolean isPresent(By locator) {
        return !driver().findElements(locator).isEmpty();
    }

    /** Returns count of elements matching the locator. */
    protected int countElements(By locator) {
        return driver().findElements(locator).size();
    }

    // ─── Waits ──────────────────────────────────────────────────────────────────

    /** Waits for element to be visible. */
    protected void waitUntilVisible(By locator) {
        WaitUtils.waitForVisibility(locator);
    }

    /** Waits for element to disappear. */
    protected void waitUntilInvisible(By locator) {
        WaitUtils.waitForInvisibility(locator);
    }

    /** Waits for element text to equal expected. */
    protected void waitUntilTextIs(By locator, String text) {
        WaitUtils.waitForText(locator, text);
    }

    // ─── Gestures ───────────────────────────────────────────────────────────────

    /** Scrolls upward until the element is visible, or throws after max attempts. */
    @Step("Scroll to element: {locator}")
    protected void scrollToElement(By locator) {
        GestureUtils.scrollToElement(locator);
    }

    /** Scrolls up (swipes upward). */
    protected void scrollUp() {
        GestureUtils.swipeUp();
    }

    /** Scrolls down (swipes downward). */
    protected void scrollDown() {
        GestureUtils.swipeDown();
    }

    /** Swipes left (reveals right-side content). */
    protected void swipeLeft() {
        GestureUtils.swipeLeft();
    }

    /** Swipes right (reveals left-side content). */
    protected void swipeRight() {
        GestureUtils.swipeRight();
    }

    /** Swipes left on a specific element (e.g., to delete). */
    protected void swipeLeftOnElement(By locator) {
        GestureUtils.swipeLeftOnElement(find(locator));
    }

    /** Long-presses a specific element. */
    @Step("Long press element: {locator}")
    protected void longPress(By locator, int durationMs) {
        GestureUtils.longPressElement(find(locator), durationMs);
    }

    // ─── Keyboard ───────────────────────────────────────────────────────────────

    /** Hides the software keyboard if it is open. */
    protected void hideKeyboard() {
        try {
            driver().hideKeyboard();
        } catch (Exception e) {
            log.debug("hideKeyboard: keyboard not present or already hidden");
        }
    }

    // ─── Context switching ──────────────────────────────────────────────────────

    /** Switches driver to a WebView context (first available). */
    protected void switchToWebView() {
        for (String ctx : driver().getContextHandles()) {
            if (ctx.contains("WEBVIEW")) {
                driver().context(ctx);
                log.debug("Switched to context: {}", ctx);
                return;
            }
        }
        throw new RuntimeException("No WEBVIEW context found.");
    }

    /** Switches driver back to native app context. */
    protected void switchToNative() {
        driver().context("NATIVE_APP");
        log.debug("Switched to NATIVE_APP context");
    }

    // ─── App management ─────────────────────────────────────────────────────────

    /** Launches the app (puts it in the foreground). */
    protected void launchApp() {
        driver().activateApp(constants.MobileConstants.APP_PACKAGE);
    }

    /** Terminates the app. */
    protected void terminateApp() {
        driver().terminateApp(constants.MobileConstants.APP_PACKAGE);
    }

    /** Sends the app to the background for the given number of seconds, then restores it. */
    protected void sendAppToBackground(int seconds) {
        driver().runAppInBackground(java.time.Duration.ofSeconds(seconds));
    }

    // ─── Device info ────────────────────────────────────────────────────────────

    protected Dimension getScreenSize() {
        return driver().manage().window().getSize();
    }

    // ─── Legacy compatibility helpers ───────────────────────────────────────────

    /** Checks if a page is loaded based on a key element being visible. */
    protected static boolean isTargetPageLoaded(By locator) {
        try {
            return DriverManager.getDriver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Alias kept for backward-compatibility with existing page objects. */
    protected boolean isDisplayed(By locator) {
        return isVisible(locator);
    }
}
