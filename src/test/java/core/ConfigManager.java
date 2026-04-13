package core;

import java.io.InputStream;
import java.util.Properties;

import static java.lang.System.getProperty;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        try {
            InputStream input = ConfigManager.class
                    .getClassLoader()
                    .getResourceAsStream("config/config.properties");

            if (input != null) {
                properties.load(input);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
    public static String getRunMode() {
        return get("runMode", "distributed").trim();
    }
    public static String get(String key, String defaultValue) {
        return getProperty(key,
                properties.getProperty(key, defaultValue));
    }

    public static String getExecutionType() {
        return get("executionType", "local-emulator").trim();
    }
    public static String getBsUser() {
        return properties.getProperty("bsUser");
    }

    public static String getBsKey() {
        return properties.getProperty("bsKey");
    }

    public static String getBsAppId() {
        return properties.getProperty("bsAppId");
    }
    public static String getUdid() {
        return get("udid", "");
    }

    public static String getDeviceName() {
        return get("deviceName", "Android Emulator");
    }

    public static String getCloudUrl() {
        return get("cloudUrl",
                "https://hub-cloud.browserstack.com/wd/hub");
    }

}