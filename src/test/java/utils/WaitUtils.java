package utils;

import constants.FrameworkConstants;
import core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public final class WaitUtils {

    private static final Logger LOG = LoggerUtil.getLogger(WaitUtils.class);

    private WaitUtils() {}

    public static WebDriverWait getWait() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT_SECONDS)
        );
    }

    public static WebDriverWait getWait(int seconds) {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(seconds)
        );
    }

    public static FluentWait<io.appium.java_client.AppiumDriver> getFluentWait(int timeoutSeconds) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(FrameworkConstants.POLLING_INTERVAL_MS))
                .ignoring(NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }

    /** Wait for element to be visible and return it. */
    public static WebElement waitForVisibility(By locator) {
        LOG.debug("Waiting for visibility of: {}", locator);
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait for element to be visible with custom timeout. */
    public static WebElement waitForVisibility(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait for element to be clickable and return it. */
    public static WebElement waitForClickability(By locator) {
        LOG.debug("Waiting for clickability of: {}", locator);
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Wait for element to be clickable with custom timeout. */
    public static WebElement waitForClickability(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Wait for element to be present in DOM (not necessarily visible). */
    public static WebElement waitForPresence(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Wait for all elements matching locator to be visible. */
    public static List<WebElement> waitForAllVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /** Wait for element to disappear. */
    public static boolean waitForInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Wait for element text to equal expected value. */
    public static boolean waitForText(By locator, String text) {
        return getWait().until(ExpectedConditions.textToBe(locator, text));
    }

    /** Wait for element text to contain expected value. */
    public static boolean waitForTextContains(By locator, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /** Check element exists without throwing an exception. */
    public static boolean isPresent(By locator) {
        try {
            waitForPresence(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Check element is visible without throwing an exception. */
    public static boolean isVisible(By locator) {
        try {
            waitForVisibility(locator, FrameworkConstants.SHORT_WAIT_SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Backwards-compatible alias used by existing page objects. */
    public static void waitForElement(By locator) {
        waitForVisibility(locator);
    }
}
