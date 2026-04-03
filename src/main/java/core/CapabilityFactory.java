package core;

import io.appium.java_client.android.options.UiAutomator2Options;
import java.util.HashMap;
import java.util.Map;

public class CapabilityFactory {

    public static UiAutomator2Options getCapabilities() {

        String execution = System.getProperty("execution", "local");

        UiAutomator2Options options = new UiAutomator2Options();

        // Common capabilities
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setNoReset(false);

        switch (execution.toLowerCase()) {

            case "local":
                setupLocal(options);
                break;

            case "emulator":
                setupEmulator(options);
                break;

            case "cloud":
                setupCloud(options);
                break;

            default:
                throw new RuntimeException("Invalid execution type: " + execution);
        }

        return options;
    }
    private static void setupLocal(UiAutomator2Options options) {
        String udid = System.getProperty("udid");
        System.out.println(udid);
        if (udid == null) {
            throw new RuntimeException("UDID required for parallel execution");
        }
        options.setDeviceName(udid);
        options.setUdid(udid);

        options.setPlatformVersion("13");

        options.setApp("C:\\Users\\Admin\\Downloads\\app.apk");
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.MainActivity");
    }
    private static void setupEmulator(UiAutomator2Options options) {

        String udid = System.getProperty("udid");
        String systemPort = System.getProperty("systemPort");

        options.setDeviceName("Android Emulator");
        options.setUdid(udid);

        // 🔥 CRITICAL FOR PARALLEL
        options.setSystemPort(Integer.parseInt(systemPort));

        options.setApp("C:\\Users\\Admin\\Downloads\\app.apk");

        System.out.println("Running on UDID: " + udid);
        System.out.println("SystemPort: " + systemPort);
    }

    private static void setupCloud(UiAutomator2Options options) {

        options.setDeviceName("Samsung Galaxy S22");
        options.setPlatformVersion("12.0");
        options.setApp("bs://<your-app-id>");
        Map<String, Object> cloudOptions = new HashMap<>();
        cloudOptions.put("userName", System.getenv("veeraswamykallur_15CJ49"));
        cloudOptions.put("accessKey", System.getenv("L9b2SCbUFhsJRagkEqsX"));

        cloudOptions.put("projectName", "Appium Framework");
        cloudOptions.put("buildName", "Build 1");
        cloudOptions.put("sessionName", "Login Test");

        options.setCapability("bstack:options", cloudOptions);
    }
}