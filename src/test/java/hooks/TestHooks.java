package hooks;


import core.AppiumServerManager;
import core.DeviceManager;
import core.DriverFactory;
import core.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.ScreenshotUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;



public class TestHooks {


    @BeforeAll
    public static void BeforeAll()throws Exception {
        System.out.println("===== Detecting Connected Devices =====");

        DeviceManager.setupDevices();
        AppiumServerManager.startServer();
    }


    @Before
    public void setUp(Scenario scenario) throws Exception {

        System.out.println("Starting Scenario: " + scenario.getName());

        AppiumDriver driver = DriverFactory.initDriver();

        DriverManager.setDriver(driver);
    }


    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {

            String screenshotName = scenario.getName().replaceAll(" ", "_");
            ScreenshotUtils.capture(screenshotName);
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", screenshotName);
        }
        DriverManager.quitDriver();
    }
    @AfterAll
    public static void AfterAll() {
        AppiumServerManager.stopServer();
    }
}


