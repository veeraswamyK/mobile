package constants;

public final class MobileConstants {

    private MobileConstants() {}

    // Platform names
    public static final String PLATFORM_ANDROID = "Android";
    public static final String PLATFORM_IOS = "iOS";

    // Automation names
    public static final String AUTOMATION_ANDROID = "UiAutomator2";
    public static final String AUTOMATION_IOS = "XCUITest";

    // App contexts
    public static final String CONTEXT_NATIVE = "NATIVE_APP";
    public static final String CONTEXT_WEBVIEW = "WEBVIEW";

    // App identifiers
    public static final String APP_PACKAGE = "com.swaglabsmobileapp";
    public static final String APP_ACTIVITY = "com.swaglabsmobileapp.MainActivity";
    public static final String APP_APK_NAME = "app.apk";

    // BrowserStack defaults
    public static final String BS_PROJECT_NAME = "Swag Labs Mobile Automation";
    public static final String BS_BUILD_NAME  = "Build-1.0";
    public static final String BS_CLOUD_URL   = "https://hub-cloud.browserstack.com/wd/hub";

    // Execution modes
    public static final String EXEC_LOCAL_EMULATOR = "local-emulator";
    public static final String EXEC_LOCAL_REAL      = "local-real";
    public static final String EXEC_CLOUD           = "cloud";
    public static final String EXEC_HYBRID          = "hybrid";
}
