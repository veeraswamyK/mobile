package core;

import io.appium.java_client.android.options.UiAutomator2Options;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CapabilityFactory {

    public static UiAutomator2Options getCapabilities(
            String type,
            String udid,
            String deviceName,
            int systemPort) {

        UiAutomator2Options options =
                new UiAutomator2Options();

        options.setAutomationName("UiAutomator2");
        options.setPlatformName("Android");

        if (type.equals("local")) {

            String appPath =
                    System.getProperty("user.dir")
                            + "/apps/app.apk";

            options.setUdid(udid);
            options.setDeviceName(deviceName);
            options.setSystemPort(systemPort);
            options.setApp(appPath);

            options.setAppPackage(
                    "com.swaglabsmobileapp");

            options.setAppActivity(
                    "com.swaglabsmobileapp.MainActivity");
        }

        else if (type.equalsIgnoreCase("cloud")) {

            String user = System.getProperty(
                    "bsUser",
                    ConfigManager.getBsUser());

            String key = System.getProperty(
                    "bsKey",
                    ConfigManager.getBsKey());

            String app = System.getProperty(
                    "bsAppId",
                    ConfigManager.getBsAppId());
            System.out.println("bsUser = [" + user + "]");
            System.out.println("bsKey  = [" + key + "]");
            System.out.println("bsApp  = [" + app + "]");

            if (user == null || key == null || app == null) {
                throw new RuntimeException(
                        "BrowserStack credentials missing");
            }

            Map<String, Object> bstack = new HashMap<>();

            bstack.put("userName", user);
            bstack.put("accessKey", key);
            bstack.put("projectName", "Mobile Automation");
            bstack.put("buildName", "Build 2");
            bstack.put("sessionName", "Test");
            bstack.put("deviceName", deviceName);
//            bstack.put("platformVersion", "12.0");

            options.setCapability("bstack:options", bstack);

            options.setApp(app);
        }

        options.setAppWaitActivity("*");
        options.setAppWaitDuration(
                Duration.ofSeconds(60));

        return options;
    }
}