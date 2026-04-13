package core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    public static void initDriver(
            String type,
            String udid,
            String deviceName,
            int systemPort) {

        try {

            URL url;

            if (type.equals("cloud")) {
                url = new URL(ConfigManager.getCloudUrl());
            } else {
                AppiumServerManager.startServer();
                url = new URL(AppiumServerManager.getServerUrl());
            }

            UiAutomator2Options options =
                    CapabilityFactory.getCapabilities(
                            type, udid, deviceName, systemPort);

            AndroidDriver driver =
                    new AndroidDriver(url, options);

            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(5));

            DriverManager.setDriver(driver);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}