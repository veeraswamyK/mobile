package constants;

public final class FrameworkConstants {

    private FrameworkConstants() {}

    // Config paths (relative to classpath)
    public static final String CONFIG_FILE = "config/config.properties";
    public static final String TESTDATA_DIR = "testdata/";

    // Screenshot output dir (relative to project root)
    public static final String SCREENSHOT_DIR = "test-output/screenshots/";

    // Allure results
    public static final String ALLURE_RESULTS_DIR = "target/allure-results";

    // Wait timeouts (seconds)
    public static final int EXPLICIT_WAIT_SECONDS = 15;
    public static final int SHORT_WAIT_SECONDS = 5;
    public static final int LONG_WAIT_SECONDS = 30;
    public static final int POLLING_INTERVAL_MS = 500;

    // Appium server
    public static final String APPIUM_HOST = "127.0.0.1";
    public static final int APPIUM_PORT = 4723;

    // App wait
    public static final int APP_WAIT_SECONDS = 60;

    // Retry
    public static final int MAX_RETRY_COUNT = 2;

    // Swipe ratios
    public static final double SWIPE_START_RATIO = 0.8;
    public static final double SWIPE_END_RATIO = 0.2;
    public static final int SWIPE_DURATION_MS = 600;
}
