package core;

import constants.FrameworkConstants;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class AppiumServerManager {

    private static final Logger LOG = LoggerFactory.getLogger(AppiumServerManager.class);
    private static AppiumDriverLocalService service;
    private static final Object LOCK = new Object();

    private AppiumServerManager() {}

    public static void startServer() {
        synchronized (LOCK) {
            if (service != null && service.isRunning()) {
                LOG.debug("Appium server already running at {}", getServerUrl());
                return;
            }
            service = createServiceBuilder().build();
            service.start();
            LOG.info("Appium server started at {}", getServerUrl());
        }
    }

    private static AppiumServiceBuilder createServiceBuilder() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(FrameworkConstants.APPIUM_HOST)
                .usingPort(FrameworkConstants.APPIUM_PORT);

        File nodeExecutable = findNodeExecutable();
        if (nodeExecutable != null) {
            builder.usingDriverExecutable(nodeExecutable);
            LOG.info("Using Node executable: {}", nodeExecutable.getAbsolutePath());
        }

        File appiumMainJs = findAppiumMainJs();
        if (appiumMainJs != null) {
            builder.withAppiumJS(appiumMainJs);
            LOG.info("Using Appium main script: {}", appiumMainJs.getAbsolutePath());
        } else {
            LOG.warn("Appium main script was not found in known locations; falling back to AppiumServiceBuilder defaults.");
        }

        return builder;
    }

    private static File findNodeExecutable() {
        return firstExistingFile(
                System.getProperty("nodeExecutable"),
                System.getenv("NODE_EXECUTABLE"),
                "C:\\Program Files\\nodejs\\node.exe",
                "C:\\Program Files (x86)\\nodejs\\node.exe"
        );
    }

    private static File findAppiumMainJs() {
        String appData = System.getenv("APPDATA");
        String userHome = System.getProperty("user.home");

        return firstExistingFile(
                System.getProperty("appiumMainJs"),
                System.getenv("APPIUM_MAIN_JS"),
                path(appData, "npm\\node_modules\\appium\\build\\lib\\main.js"),
                path(userHome, "AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")
        );
    }

    private static String path(String base, String child) {
        return base == null || base.isBlank() ? null : base + "\\" + child;
    }

    private static File firstExistingFile(String... candidates) {
        for (String candidate : candidates) {
            if (candidate == null || candidate.isBlank()) {
                continue;
            }
            File file = new File(candidate.trim());
            if (file.isFile()) {
                return file;
            }
        }
        return null;
    }

    public static void stopServer() {
        synchronized (LOCK) {
            if (service != null && service.isRunning()) {
                service.stop();
                LOG.info("Appium server stopped");
            }
        }
    }

    public static String getServerUrl() {
        if (service == null) {
            throw new IllegalStateException("Appium server has not been started.");
        }
        return service.getUrl().toString().replace("0.0.0.0", FrameworkConstants.APPIUM_HOST);
    }

    public static boolean isRunning() {
        return service != null && service.isRunning();
    }
}
