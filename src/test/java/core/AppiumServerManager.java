package core;

import constants.FrameworkConstants;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            service = new AppiumServiceBuilder()
                    .withIPAddress(FrameworkConstants.APPIUM_HOST)
                    .usingPort(FrameworkConstants.APPIUM_PORT)
                    .build();
            service.start();
            LOG.info("Appium server started at {}", getServerUrl());
        }
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
