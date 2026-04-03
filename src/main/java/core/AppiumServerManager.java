package core;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;

    public static void startServer() {
        if (service == null || !service.isRunning()) {
            service = new AppiumServiceBuilder()
                    .withAppiumJS(new File("C:\\Users\\Admin\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .build();
            service.start();

            System.out.println("Appium Server Started: " + getServerUrl());
        }
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium Server Stopped");
        }
    }

    public static String getServerUrl() {
        return service.getUrl().toString().replace("0.0.0.0", "127.0.0.1");
    }
}