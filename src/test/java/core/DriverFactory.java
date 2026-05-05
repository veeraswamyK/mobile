package core;

import constants.MobileConstants;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static void initDriver(String executionType, String udid, String deviceName, int systemPort) {
        try {
            URL serverUrl = resolveServerUrl(executionType);
            UiAutomator2Options options = CapabilityFactory.getCapabilities(executionType, udid, deviceName, systemPort);

            LOG.info("Initialising AndroidDriver → {}", serverUrl);
            AndroidDriver driver = new AndroidDriver(serverUrl, options);

            // No implicit wait — use explicit waits via WaitUtils throughout
            DriverManager.setDriver(driver);
            LOG.info("Driver initialised for device: {}", deviceName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise driver for device: " + deviceName, e);
        }
    }

    private static URL resolveServerUrl(String executionType) throws Exception {
        if (executionType.equalsIgnoreCase(MobileConstants.EXEC_CLOUD)) {
            return new URL(ConfigManager.getCloudUrl());
        }
        AppiumServerManager.startServer();
        return new URL(AppiumServerManager.getServerUrl());
    }
}
