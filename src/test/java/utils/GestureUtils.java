package utils;

import constants.FrameworkConstants;
import core.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

public final class GestureUtils {

    private static final Logger LOG = LoggerUtil.getLogger(GestureUtils.class);
    private static final int MAX_SCROLL_ATTEMPTS = 10;

    private GestureUtils() {}

    private static AppiumDriver driver() {
        return DriverManager.getDriver();
    }

    private static Dimension screenSize() {
        return driver().manage().window().getSize();
    }

    /** Swipe upward (scroll down content). */
    public static void swipeUp() {
        Dimension size = screenSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * FrameworkConstants.SWIPE_START_RATIO);
        int endY   = (int) (size.getHeight() * FrameworkConstants.SWIPE_END_RATIO);
        swipe(x, startY, x, endY);
    }

    /** Swipe downward (scroll up content). */
    public static void swipeDown() {
        Dimension size = screenSize();
        int x = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * FrameworkConstants.SWIPE_END_RATIO);
        int endY   = (int) (size.getHeight() * FrameworkConstants.SWIPE_START_RATIO);
        swipe(x, startY, x, endY);
    }

    /** Swipe left (reveal right content). */
    public static void swipeLeft() {
        Dimension size = screenSize();
        int y      = size.getHeight() / 2;
        int startX = (int) (size.getWidth() * 0.8);
        int endX   = (int) (size.getWidth() * 0.2);
        swipe(startX, y, endX, y);
    }

    /** Swipe right (reveal left content). */
    public static void swipeRight() {
        Dimension size = screenSize();
        int y      = size.getHeight() / 2;
        int startX = (int) (size.getWidth() * 0.2);
        int endX   = (int) (size.getWidth() * 0.8);
        swipe(startX, y, endX, y);
    }

    /** Swipe on a specific element from one edge to the other. */
    public static void swipeLeftOnElement(WebElement element) {
        Point loc = element.getLocation();
        Dimension dim = element.getSize();
        int startX = loc.getX() + (int)(dim.getWidth() * 0.9);
        int endX   = loc.getX() + (int)(dim.getWidth() * 0.1);
        int y      = loc.getY() + dim.getHeight() / 2;
        swipe(startX, y, endX, y);
    }

    /** Core swipe using W3C Actions API. */
    public static void swipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(FrameworkConstants.SWIPE_DURATION_MS), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(Collections.singletonList(swipe));
    }

    /** Tap at given screen coordinates. */
    public static void tapByCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 0);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerMove(Duration.ofMillis(50), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(Collections.singletonList(tap));
    }

    /** Long press at given screen coordinates. */
    public static void longPress(int x, int y, int durationMs) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence press = new Sequence(finger, 0);
        press.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        press.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        press.addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), x, y));
        press.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(Collections.singletonList(press));
    }

    /** Long press on a WebElement. */
    public static void longPressElement(WebElement element, int durationMs) {
        Point loc = element.getLocation();
        Dimension dim = element.getSize();
        int x = loc.getX() + dim.getWidth() / 2;
        int y = loc.getY() + dim.getHeight() / 2;
        longPress(x, y, durationMs);
    }

    /** Drag an element to target coordinates. */
    public static void dragTo(WebElement source, int targetX, int targetY) {
        Point loc = source.getLocation();
        Dimension dim = source.getSize();
        int srcX = loc.getX() + dim.getWidth() / 2;
        int srcY = loc.getY() + dim.getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drag = new Sequence(finger, 0);
        drag.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), srcX, srcY));
        drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        drag.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), targetX, targetY));
        drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(Collections.singletonList(drag));
    }

    /** Two-finger pinch to zoom in. */
    public static void pinchZoomIn() {
        Dimension size = screenSize();
        int centerX = size.getWidth() / 2;
        int centerY = size.getHeight() / 2;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence zoomIn1 = new Sequence(finger1, 0);
        zoomIn1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        zoomIn1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        zoomIn1.addAction(finger1.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), centerX - 150, centerY));
        zoomIn1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence zoomIn2 = new Sequence(finger2, 0);
        zoomIn2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        zoomIn2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        zoomIn2.addAction(finger2.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), centerX + 150, centerY));
        zoomIn2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver().perform(Arrays.asList(zoomIn1, zoomIn2));
    }

    /**
     * Scroll by swiping upward until the element matching the given locator
     * is found or the page stops changing.
     */
    public static void scrollToElement(By locator) {
        LOG.debug("Scrolling to element: {}", locator);
        String previousSource = "";

        for (int attempt = 0; attempt < MAX_SCROLL_ATTEMPTS; attempt++) {
            if (!driver().findElements(locator).isEmpty()) {
                LOG.debug("Element found after {} swipe(s)", attempt);
                return;
            }
            String currentSource = driver().getPageSource();
            if (currentSource.equals(previousSource)) {
                break;
            }
            previousSource = currentSource;
            swipeUp();
        }
        throw new NoSuchElementException("Element not found after scrolling: " + locator);
    }

    /**
     * Scroll down until the element matching the given locator is found.
     */
    public static void scrollDownToElement(By locator) {
        LOG.debug("Scrolling down to element: {}", locator);
        String previousSource = "";

        for (int attempt = 0; attempt < MAX_SCROLL_ATTEMPTS; attempt++) {
            if (!driver().findElements(locator).isEmpty()) {
                return;
            }
            String currentSource = driver().getPageSource();
            if (currentSource.equals(previousSource)) {
                break;
            }
            previousSource = currentSource;
            swipeDown();
        }
        throw new NoSuchElementException("Element not found after scrolling down: " + locator);
    }
}
