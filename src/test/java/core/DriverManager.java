package core;

import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private DriverManager() {}

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static void setDriver(AppiumDriver driverRef) {
        if (driver.get() != null) {
            throw new IllegalStateException("Driver already initialized for this thread.");
        }
        driver.set(driverRef);
    }

    public static AppiumDriver getDriver() {
        if (driver.get() == null) {
            throw new IllegalStateException("Driver not initialized.");
        }
        return driver.get();
    }

    public static void quitDriver() {
        AppiumDriver driverRef = driver.get();
        if (driverRef != null) {
            driverRef.quit();
            driver.remove();
        }
    }
}