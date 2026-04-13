package hooks;

import core.*;
import io.cucumber.java.*;
import utils.ContextManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHooks {

    private static final AtomicInteger counter =
            new AtomicInteger(0);

    @BeforeAll
    public static void beforeAll() {

        String execution =
                ConfigManager.getExecutionType();

        TargetManager.clear();

        int port = 8200;

        // LOCAL DEVICES
        if (execution.equalsIgnoreCase("local-real")
                || execution.equalsIgnoreCase("hybrid")) {

            List<String> devices =
                    DeviceManager.getConnectedDevices();

            for (String udid : devices) {

                TargetManager.addTarget(
                        new ExecutionTarget(
                                "local",
                                udid,
                                udid,
                                port++
                        )
                );
            }
        }

        // CLOUD DEVICES
        if (execution.equalsIgnoreCase("cloud")
                || execution.equalsIgnoreCase("hybrid")) {

            TargetManager.addTarget(
                    new ExecutionTarget(
                            "cloud",
                            "",
                            "Samsung Galaxy S22",
                            0
                    )
            );

            TargetManager.addTarget(
                    new ExecutionTarget(
                            "cloud",
                            "",
                            "Google Pixel 7",
                            0
                    )
            );

            TargetManager.addTarget(
                    new ExecutionTarget(
                            "cloud",
                            "",
                            "OnePlus 11",
                            0
                    )
            );
        }

        if (TargetManager.size() == 0) {
            throw new RuntimeException(
                    "No execution targets found");
        }

        System.out.println(
                "Targets Found: "
                        + TargetManager.size());
    }

    @Before
    public void setUp(Scenario scenario) {

        int index =
                counter.getAndIncrement()
                        % TargetManager.size();

        ExecutionTarget target =
                TargetManager.getTarget(index);

        System.out.println(
                "Scenario: "
                        + scenario.getName());

        System.out.println(
                "Running On: "
                        + target.deviceName);

        DriverFactory.initDriver(
                target.type,
                target.udid,
                target.deviceName,
                target.systemPort
        );
    }

    @After
    public void tearDown() {

        DriverManager.quitDriver();
        ContextManager.unload();
    }

    @AfterAll
    public static void afterAll() {

        AppiumServerManager.stopServer();
    }
}
