package core;

import io.appium.java_client.android.options.UiAutomator2Options;

public class CapabilityFactory {

    public static UiAutomator2Options getCapabilities() {

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Android Device");
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setUdid("adb-RZ8R12B5L5H-ptoOBA._adb-tls-connect._tcp");
        options.setApp("C:\\Users\\Admin\\Downloads\\app.apk");
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.MainActivity");
        options.setNoReset(false);
        options.setPlatformVersion("13");



        return options;
    }
}