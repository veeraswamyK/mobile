package hooks;

import constants.MobileConstants;
import core.*;
import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import utils.ContextManager;
import utils.ScreenshotUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHooks {

    private static final Logger LOG = LoggerFactory.getLogger(TestHooks.class);
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @BeforeAll
    public static void globalSetup() {
        String execution = ConfigManager.getExecutionType();
        LOG.info("Execution type: {}", execution);

        TargetManager.clear();
        int port = 8200;

        // Register local (real or emulator) devices
        if (execution.equalsIgnoreCase(MobileConstants.EXEC_LOCAL_REAL)
                || execution.equalsIgnoreCase(MobileConstants.EXEC_HYBRID)) {

            List<String> devices = DeviceManager.getConnectedDevices();
            if (devices.isEmpty()) {
                LOG.warn("No real devices detected via ADB.");
            }
            for (String udid : devices) {
                TargetManager.addTarget(new ExecutionTarget("local", udid, udid, port++));
            }
        }

        if (execution.equalsIgnoreCase(MobileConstants.EXEC_LOCAL_EMULATOR)) {
            String udid       = ConfigManager.getUdid();
            String deviceName = ConfigManager.getDeviceName();
            int systemPort    = ConfigManager.getSystemPort();
            TargetManager.addTarget(new ExecutionTarget("local", udid, deviceName, systemPort));
        }

        // Register cloud devices
        if (execution.equalsIgnoreCase(MobileConstants.EXEC_CLOUD)
                || execution.equalsIgnoreCase(MobileConstants.EXEC_HYBRID)) {

            TargetManager.addTarget(new ExecutionTarget("cloud", "", "Samsung Galaxy S22", 0));
            TargetManager.addTarget(new ExecutionTarget("cloud", "", "Google Pixel 7",     0));
            TargetManager.addTarget(new ExecutionTarget("cloud", "", "OnePlus 9",          0));
        }

        if (TargetManager.size() == 0) {
            throw new RuntimeException("No execution targets registered. Check your executionType configuration.");
        }
        LOG.info("Registered {} execution target(s).", TargetManager.size());
    }

    @Before
    public void setUp(Scenario scenario) {
        ExecutionTarget target = resolveTarget();

        LOG.info("▶ Scenario : {}", scenario.getName());
        LOG.info("  Device   : {} ({})", target.getDeviceName(), target.getUdid());
        LOG.info("  Tags     : {}", scenario.getSourceTagNames());

        DriverFactory.initDriver(
                target.getType(),
                target.getUdid(),
                target.getDeviceName(),
                target.getSystemPort()
        );
    }

    private ExecutionTarget resolveTarget() {
        ITestResult currentResult = Reporter.getCurrentTestResult();
        if (currentResult != null && currentResult.getTestContext() != null) {
            Map<String, String> params = currentResult.getTestContext()
                    .getCurrentXmlTest()
                    .getAllParameters();

            String udid = params.get("udid");
            if (udid != null && !udid.isBlank()) {
                String deviceName = params.getOrDefault("deviceName", ConfigManager.getDeviceName());
                int systemPort = Integer.parseInt(params.getOrDefault("systemPort",
                        String.valueOf(ConfigManager.getSystemPort())));
                return new ExecutionTarget("local", udid.trim(), deviceName.trim(), systemPort);
            }
        }

        int index = COUNTER.getAndIncrement() % TargetManager.size();
        return TargetManager.getTarget(index);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.warn("✘ Scenario FAILED: {}", scenario.getName());
            try {
                ScreenshotUtils.capture(scenario.getName());
            } catch (Exception e) {
                LOG.error("Screenshot capture failed", e);
            }
        } else {
            LOG.info("✔ Scenario PASSED: {}", scenario.getName());
        }

        DriverManager.quitDriver();
        ContextManager.unload();
    }

    @AfterAll
    public static void globalTeardown() {
        AppiumServerManager.stopServer();
        LOG.info("Test suite complete.");
    }
}
