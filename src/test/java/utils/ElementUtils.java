package utils;

import core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════
 *  ElementUtils — Element-Level Inspection Helpers
 * ═══════════════════════════════════════════════════════════════
 *
 *  WHAT THIS CLASS IS:
 *  Static helpers for inspecting element properties without
 *  performing any action. Think of these as "read-only" tools
 *  that answer questions about an element's current state.
 *
 *  These complement WaitUtils (which blocks until a condition is true)
 *  and GestureUtils (which performs touch gestures).
 *
 *  DO NOT add interactions (clicks, swipes) here.
 *  DO NOT add waits here — use WaitUtils for those.
 *
 *  AVAILABLE METHODS:
 *  ── STATE CHECKS
 *     isEnabled / isSelected / isChecked / isFocused
 *  ── BOUNDS & GEOMETRY
 *     getCenter / getBounds / getSize / getLocation
 *  ── ATTRIBUTE SHORTCUTS
 *     getText / getContentDescription / getResourceId
 *  ── LIST HELPERS
 *     getTexts / findElementWithText
 */
public final class ElementUtils {

    private static final Logger LOG = LoggerUtil.getLogger(ElementUtils.class);

    private ElementUtils() {}

    // ════════════════════════════════════════════════════════════
    //  STATE CHECKS
    // ════════════════════════════════════════════════════════════

    /**
     * Returns TRUE if the element is currently ENABLED (interactable).
     *
     * "Enabled" reads the native "enabled" attribute:
     *   - Android: "true" / "false" on the ViewGroup
     *   - iOS: "enabled" on the XCUIElement
     *
     * Use for: verifying submit buttons activate after required fields are filled.
     */
    public static boolean isEnabled(By locator) {
        String value = WaitUtils.waitForVisibility(locator).getAttribute("enabled");
        return "true".equalsIgnoreCase(value);
    }

    /**
     * Returns TRUE if the element is currently SELECTED or CHECKED.
     *
     * Works for: checkboxes, radio buttons, toggle switches, tab bar items.
     * Reads the "selected" attribute (Android) or "value"/"selected" (iOS).
     */
    public static boolean isSelected(By locator) {
        WebElement el = WaitUtils.waitForVisibility(locator);
        // Try "selected" first (Android), then "checked" (Android checkboxes)
        String selected = el.getAttribute("selected");
        if (selected != null) return "true".equalsIgnoreCase(selected);
        String checked = el.getAttribute("checked");
        return "true".equalsIgnoreCase(checked);
    }

    /**
     * Returns TRUE if a checkbox or toggle is CHECKED.
     * Reads the "checked" attribute (Android CheckBox, ToggleButton).
     */
    public static boolean isChecked(By locator) {
        String value = WaitUtils.waitForVisibility(locator).getAttribute("checked");
        return "true".equalsIgnoreCase(value);
    }

    /**
     * Returns TRUE if the element currently has INPUT FOCUS.
     * Reads the "focused" attribute. Use to verify a text field was tapped.
     */
    public static boolean isFocused(By locator) {
        String value = WaitUtils.waitForVisibility(locator).getAttribute("focused");
        return "true".equalsIgnoreCase(value);
    }

    // ════════════════════════════════════════════════════════════
    //  BOUNDS & GEOMETRY
    // ════════════════════════════════════════════════════════════

    /**
     * Returns the CENTRE POINT (x, y) of the element in screen coordinates.
     * Use when you need to tap a specific point but want to compute it
     * from the element rather than hardcoding coordinates.
     */
    public static Point getCenter(By locator) {
        WebElement el = WaitUtils.waitForVisibility(locator);
        int x = el.getLocation().getX() + el.getSize().getWidth() / 2;
        int y = el.getLocation().getY() + el.getSize().getHeight() / 2;
        return new Point(x, y);
    }

    /**
     * Returns the BOUNDING RECTANGLE of the element:
     *   Rectangle.x      → left edge (pixels from screen left)
     *   Rectangle.y      → top edge  (pixels from screen top)
     *   Rectangle.width  → element width in pixels
     *   Rectangle.height → element height in pixels
     *
     * Use for: precise gesture calculations, overlap detection.
     */
    public static Rectangle getBounds(By locator) {
        WebElement el = WaitUtils.waitForVisibility(locator);
        return new Rectangle(
                el.getLocation().getX(),
                el.getLocation().getY(),
                el.getSize().getHeight(),
                el.getSize().getWidth()
        );
    }

    /**
     * Returns the PIXEL SIZE (width × height) of the element.
     */
    public static Dimension getSize(By locator) {
        return WaitUtils.waitForVisibility(locator).getSize();
    }

    /**
     * Returns the TOP-LEFT CORNER (x, y) of the element in screen coordinates.
     */
    public static Point getLocation(By locator) {
        return WaitUtils.waitForVisibility(locator).getLocation();
    }

    // ════════════════════════════════════════════════════════════
    //  ATTRIBUTE SHORTCUTS
    // ════════════════════════════════════════════════════════════

    /**
     * Returns the visible TEXT of the element (Android "text" / iOS "label").
     * Waits for the element to be visible first.
     */
    public static String getText(By locator) {
        return WaitUtils.waitForVisibility(locator).getText();
    }

    /**
     * Returns the CONTENT DESCRIPTION (accessibility label) of the element.
     * On Android this is the "content-desc" attribute.
     * On iOS this is the "name" or "label" attribute.
     */
    public static String getContentDescription(By locator) {
        return WaitUtils.waitForVisibility(locator).getAttribute("content-desc");
    }

    /**
     * Returns the RESOURCE ID of the element (Android only).
     * Format: "com.example.app:id/button_submit"
     * Use for debugging — do not use resource IDs as locators in production tests.
     */
    public static String getResourceId(By locator) {
        return WaitUtils.waitForVisibility(locator).getAttribute("resource-id");
    }

    /**
     * Returns the HINT TEXT (placeholder text) of an input field.
     * The hint is shown when the field is empty. Android attribute: "hint".
     */
    public static String getHint(By locator) {
        return WaitUtils.waitForVisibility(locator).getAttribute("hint");
    }

    // ════════════════════════════════════════════════════════════
    //  LIST HELPERS
    // ════════════════════════════════════════════════════════════

    /**
     * Returns the TEXT of every element matching the locator, in DOM order.
     * Waits for at least one element to be visible first.
     *
     * Use to assert list contents without iterating manually:
     *   List<String> items = getTexts(PRODUCT_NAMES);
     *   Assert.assertTrue(items.contains("Sauce Labs Backpack"));
     */
    public static List<String> getTexts(By locator) {
        List<WebElement> elements = WaitUtils.waitForAllVisible(locator);
        return elements.stream()
                .map(WebElement::getText)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Finds the FIRST element in a list whose text EXACTLY MATCHES the given value.
     * Returns the element so you can click/swipe/inspect it.
     *
     * Throws NoSuchElementException if no element has the matching text.
     *
     * Example:
     *   WebElement product = findElementWithText(PRODUCT_NAMES, "Sauce Labs Backpack");
     *   product.click();
     */
    public static WebElement findElementWithText(By locator, String text) {
        List<WebElement> elements = DriverManager.getDriver().findElements(locator);
        return elements.stream()
                .filter(el -> text.equals(el.getText()))
                .findFirst()
                .orElseThrow(() -> new org.openqa.selenium.NoSuchElementException(
                        "No element with text '" + text + "' found for locator: " + locator));
    }

    /**
     * Finds the FIRST element in a list whose text CONTAINS the given substring.
     * Less strict than findElementWithText — use when the full text is dynamic.
     */
    public static WebElement findElementContainingText(By locator, String partialText) {
        List<WebElement> elements = DriverManager.getDriver().findElements(locator);
        return elements.stream()
                .filter(el -> el.getText().contains(partialText))
                .findFirst()
                .orElseThrow(() -> new org.openqa.selenium.NoSuchElementException(
                        "No element containing text '" + partialText + "' found for locator: " + locator));
    }

    /**
     * Returns the INDEX of the first element whose text equals the given value.
     * Returns -1 if not found.
     * Use when you need to know the position of an item in a list.
     */
    public static int indexOfElementWithText(By locator, String text) {
        List<WebElement> elements = DriverManager.getDriver().findElements(locator);
        for (int i = 0; i < elements.size(); i++) {
            if (text.equals(elements.get(i).getText())) return i;
        }
        return -1;
    }
}
