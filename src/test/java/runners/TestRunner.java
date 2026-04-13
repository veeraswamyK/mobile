package runners;

import core.TargetManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {"pretty"}
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {

        Object[][] original = super.scenarios();

        int targetCount = TargetManager.size();

        Object[][] matrix =
                new Object[original.length * targetCount][2];

        int row = 0;

        for (Object[] scenario : original) {
            for (int i = 0; i < targetCount; i++) {
                matrix[row][0] = scenario[0];
                matrix[row][1] = scenario[1];
                row++;
            }
        }

        return matrix;
    }
}