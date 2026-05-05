package core;

import constants.FrameworkConstants;
import constants.MobileConstants;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public final class CapabilityFactory {

    private static final Logger LOG = LoggerFactory.getLogger(CapabilityFactory.class);

    private CapabilityFactory() {}

    public static UiAutomator2Options getCapabilities(
            String executionType,
            String udid,
            String deviceName,
            int systemPort) {

        UiAutomator2Options options = new UiAutomator2Options();
        options.setAutomationName(MobileConstants.AUTOMATION_ANDROID);
        options.setPlatformName(MobileConstants.PLATFORM_ANDROID);
        options.setAppWaitActivity("*");
        options.setAppWaitDuration(Duration.ofSeconds(FrameworkConstants.APP_WAIT_SECONDS));
        options.setNewCommandTimeout(Duration.ofSeconds(300));

        if (executionType.equalsIgnoreCase(MobileConstants.EXEC_CLOUD)) {
            configureCloud(options, deviceName);
        } else {
            configureLocal(options, udid, deviceName, systemPort);
        }

        return options;
    }

    private static void configureLocal(UiAutomator2Options options,
                                       String udid, String deviceName, int systemPort) {
        String appPath = ConfigManager.getAppPath();
        LOG.info("Local execution — device: {}, udid: {}, port: {}, app: {}", deviceName, udid, systemPort, appPath);

        options.setUdid(udid);
        options.setDeviceName(deviceName);
        options.setSystemPort(systemPort);
        options.setApp(appPath);
        options.setAppPackage(MobileConstants.APP_PACKAGE);
        options.setAppActivity(MobileConstants.APP_ACTIVITY);
        options.setNoReset(false);
        options.setFullReset(false);
    }

    private static void configureCloud(UiAutomator2Options options, String deviceName) {
        String user  = ConfigManager.getBsUser();
        String key   = ConfigManager.getBsKey();
        String appId = ConfigManager.getBsAppId();

        LOG.info("Cloud execution — device: {}", deviceName);

        Map<String, Object> bstack = new HashMap<>();
        bstack.put("userName",    user);
        bstack.put("accessKey",   key);
        bstack.put("projectName", MobileConstants.BS_PROJECT_NAME);
        bstack.put("buildName",   MobileConstants.BS_BUILD_NAME);
        bstack.put("sessionName", deviceName);
        bstack.put("deviceName",  deviceName);
        bstack.put("debug",       true);
        bstack.put("networkLogs", true);

        options.setCapability("bstack:options", bstack);
        options.setApp(appId);
    }
}
