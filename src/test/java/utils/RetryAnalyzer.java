package utils;

import constants.FrameworkConstants;
import org.slf4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LoggerUtil.getLogger(RetryAnalyzer.class);

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < FrameworkConstants.MAX_RETRY_COUNT) {
            retryCount++;
            LOG.warn("Retrying '{}' — attempt {}/{}", result.getName(), retryCount, FrameworkConstants.MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
