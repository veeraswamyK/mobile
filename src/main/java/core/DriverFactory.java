package core;

import io.appium.java_client.android.AndroidDriver;
import java.net.URL;

public class DriverFactory {

    public static AndroidDriver initDriver() throws Exception {

        String execution = System.getProperty("execution", "local");
        URL url;

        if (execution.equalsIgnoreCase("cloud")) {
            url = new URL("https://hub-cloud.browserstack.com/wd/hub");
        } else {
            url = new URL("http://127.0.0.1:4723");
        }

        AndroidDriver driver = new AndroidDriver(url, CapabilityFactory.getCapabilities());

        System.out.println("Driver created: " + driver); // 🔍 Debug

        return driver;
    }
}