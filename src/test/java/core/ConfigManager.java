package core;

import constants.FrameworkConstants;
import constants.MobileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream(FrameworkConstants.CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("config.properties not found on classpath: " + FrameworkConstants.CONFIG_FILE);
            }
            PROPS.load(input);
            LOG.info("Loaded config from: {}", FrameworkConstants.CONFIG_FILE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigManager() {}

    /**
     * Reads a property, giving precedence to JVM system properties over the file.
     * Environment variables are checked last as a fallback.
     */
    public static String get(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }
        String fileProp = PROPS.getProperty(key);
        if (fileProp != null && !fileProp.isBlank()) {
            return fileProp.trim();
        }
        String envProp = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envProp != null && !envProp.isBlank()) {
            return envProp.trim();
        }
        return defaultValue;
    }

    // ---- Execution ----

    public static String getExecutionType() {
        return get("executionType", MobileConstants.EXEC_LOCAL_EMULATOR);
    }

    public static String getRunMode() {
        return get("runMode", "distributed");
    }

    public static String getPlatform() {
        return get("platform", MobileConstants.PLATFORM_ANDROID);
    }

    // ---- Device ----

    public static String getDeviceName() {
        return get("deviceName", "Android Emulator");
    }

    public static String getUdid() {
        return get("udid", "emulator-5554");
    }

    public static int getSystemPort() {
        return Integer.parseInt(get("systemPort", "8200"));
    }

    // ---- App ----

    public static String getAppPath() {
        String configured = get("appPath", MobileConstants.APP_APK_NAME);
        if (!configured.startsWith("/") && !configured.contains(":\\")) {
            return System.getProperty("user.dir") + "/" + configured;
        }
        return configured;
    }

    // ---- BrowserStack (read from env vars in CI; file only for local dev) ----

    public static String getBsUser() {
        String user = get("bsUser", "veeraswamykallur_15CJ49");
        if (user.isEmpty()) {
            throw new RuntimeException("BrowserStack username (bsUser) is not configured.");
        }
        return user;
    }

    public static String getBsKey() {
        String key = get("bsKey", "L9b2SCbUFhsJRagkEqsX");
        if (key.isEmpty()) {
            throw new RuntimeException("BrowserStack access key (bsKey) is not configured.");
        }
        return key;
    }

    public static String getBsAppId() {
        String appId = get("bsAppId", "bs://750022a7fe4ec5ee176abeb8c314dfd08df12876");
        if (appId.isEmpty()) {
            throw new RuntimeException("BrowserStack app ID (bsAppId) is not configured.");
        }
        return appId;
    }

    public static String getCloudUrl() {
        return get("cloudUrl", MobileConstants.BS_CLOUD_URL);
    }
}
