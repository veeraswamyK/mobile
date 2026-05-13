package core;

import constants.MobileConstants;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ADBUtils;

import java.net.URL;

public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static void initDriver(String executionType, String udid, String deviceName, int systemPort) {
        try {
            URL serverUrl = resolveServerUrl(executionType);
            UiAutomator2Options options = CapabilityFactory.getCapabilities(executionType, udid, deviceName, systemPort);
            clearStaleUiAutomatorPort(executionType, udid, systemPort);

            LOG.info("Initialising AndroidDriver → {}", serverUrl);
            LOG.info("========================================");
            LOG.info("Starting driver initialization");
            LOG.info("Execution Type : {}", executionType);
            LOG.info("Device Name    : {}", deviceName);
            LOG.info("UDID           : {}", udid);
            LOG.info("System Port    : {}", systemPort);
            LOG.info("Server URL     : {}", serverUrl);
            LOG.info("Capabilities   : {}", options.asMap());
            LOG.info("========================================");
            AndroidDriver driver = new AndroidDriver(serverUrl, options);
            // No implicit wait — use explicit waits via WaitUtils throughout
            DriverManager.setDriver(driver);
            LOG.info("Driver initialised for device: {}", deviceName);

        } catch (Exception e) {
            LOG.error("========================================");
            LOG.error("Driver initialization FAILED");
            LOG.error("Execution Type : {}", executionType);
            LOG.error("Device Name    : {}", deviceName);
            LOG.error("UDID           : {}", udid);
            LOG.error("Root Cause     : {}", e.getMessage(), e);
            LOG.error("========================================");
            throw new RuntimeException("Failed to initialise driver for device: " + deviceName, e);
        }
    }

    private static void clearStaleUiAutomatorPort(String executionType, String udid, int systemPort) {
        if (executionType.equalsIgnoreCase(MobileConstants.EXEC_CLOUD)
                || udid == null
                || udid.isBlank()
                || systemPort <= 0) {
            return;
        }

        LOG.info("Clearing stale UiAutomator2 port forward tcp:{}", systemPort);
        ADBUtils.execute(ADBUtils.resolveAdb(), "-s", udid, "forward", "--remove", "tcp:" + systemPort);
    }

    private static URL resolveServerUrl(String executionType) throws Exception {
        if (executionType.equalsIgnoreCase(MobileConstants.EXEC_CLOUD)) {
            String cloudUrl = ConfigManager.getCloudUrl();
            LOG.info("Using cloud Appium server: {}", cloudUrl);
            return new URL(cloudUrl);
        }
        AppiumServerManager.startServer();
        String localUrl = AppiumServerManager.getServerUrl();
        LOG.info("Using local Appium server: {}", localUrl);
        return new URL(localUrl);
    }
}
