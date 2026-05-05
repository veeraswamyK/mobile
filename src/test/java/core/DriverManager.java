package core;

import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DriverManager {

    private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static void setDriver(AppiumDriver driver) {
        if (DRIVER.get() != null) {
            throw new IllegalStateException("Driver is already initialized for this thread. Call quitDriver() first.");
        }
        DRIVER.set(driver);
        LOG.debug("Driver set for thread: {}", Thread.currentThread().getName());
    }

    public static AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No driver initialized for thread: " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
                LOG.debug("Driver quit for thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                LOG.warn("Exception while quitting driver", e);
            } finally {
                DRIVER.remove();
            }
        }
    }
}
