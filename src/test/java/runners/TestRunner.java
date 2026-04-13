package runners;

import core.ConfigManager;
import core.TargetManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {"pretty"}
)
public class TestRunner
        extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {

        Object[][] original = super.scenarios();

        String mode =
                ConfigManager.getRunMode();

        if (mode.equalsIgnoreCase("matrix")) {

            int targets =
                    TargetManager.size();

            Object[][] expanded =
                    new Object[original.length * targets][2];

            int row = 0;

            for (Object[] scenario : original) {
                for (int i = 0; i < targets; i++) {
                    expanded[row][0] = scenario[0];
                    expanded[row][1] = scenario[1];
                    row++;
                }
            }

            return expanded;
        }

        return original;
    }
}