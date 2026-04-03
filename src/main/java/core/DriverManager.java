package core;

import io.appium.java_client.AppiumDriver;

public final class DriverManager {

    private DriverManager() {}

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static void setDriver(AppiumDriver driverRef) {
        driver.set(driverRef);
    }

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void unload() {
        driver.remove();
    }

    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            unload();
        }
    }
}