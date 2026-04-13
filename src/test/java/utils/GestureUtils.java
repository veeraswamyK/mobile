package utils;

import core.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import java.util.Collections;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;

public class GestureUtils {

    public static void swipeUp() {
        AppiumDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();

        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.8); // bottom
        int endY   = (int) (size.getHeight() * 0.3); // middle

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
    public static void swipeDown() {
        AppiumDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();

        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.3); // middle
        int endY   = (int) (size.getHeight() * 0.8); // bottom

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600),
                PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }
    public static void scrollToElement(By locator) {
        AppiumDriver driver = DriverManager.getDriver();

        String previousPageSource = "";
        String currentPageSource = driver.getPageSource();

        while (!currentPageSource.equals(previousPageSource)) {
            if (!driver.findElements(locator).isEmpty()) {
                System.out.println("Element found!");
                return;
            }
            swipeUp();
            previousPageSource = currentPageSource;
            currentPageSource = driver.getPageSource();
        }

        throw new NoSuchElementException("Element not found after full scroll");
    }
}