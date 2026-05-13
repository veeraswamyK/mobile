package runners;

import core.ConfigManager;
import core.TargetManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue     = {"stepdefinitions", "hooks"},
        plugin   = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "json:target/cucumber-reports/cucumber.json",
                "html:target/cucumber-reports/cucumber.html"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        Object[][] original = super.scenarios();

        if ("matrix".equalsIgnoreCase(ConfigManager.getRunMode())) {
            int targets = TargetManager.size();
            Object[][] expanded = new Object[original.length * targets][2];
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
