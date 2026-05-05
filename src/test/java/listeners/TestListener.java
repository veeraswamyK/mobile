package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("▶ Test started : {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("✔ Test passed  : {} ({} ms)", result.getName(), elapsedMs(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("✘ Test failed  : {} — {}", result.getName(),
                result.getThrowable() != null ? result.getThrowable().getMessage() : "no message");
        ScreenshotUtils.capture(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("⚠ Test skipped : {}", result.getName());
    }

    private long elapsedMs(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
