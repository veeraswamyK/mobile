package runners;
import core.TestNGGenerator;
import org.testng.TestNG;
import java.util.Collections;

public class MainRunner {

    public static void main(String[] args) {

        // Step 1: Generate XML
        TestNGGenerator.generateXML();

        // Step 2: Run TestNG
        TestNG testng = new TestNG();
        testng.setTestSuites(Collections.singletonList("dynamic-testng.xml"));
        testng.run();
    }
}
