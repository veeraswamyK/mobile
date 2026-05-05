package utils;

import constants.FrameworkConstants;
import core.DriverManager;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final Logger LOG = LoggerUtil.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {}

    /**
     * Captures a screenshot, saves it to disk, and attaches it to the Allure report.
     *
     * @param name logical name for the screenshot (scenario name, test name, etc.)
     */
    public static void capture(String name) {
        try {
            byte[] screenshotBytes = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);

            // Attach to Allure
            Allure.addAttachment(name, new ByteArrayInputStream(screenshotBytes));

            // Save to disk
            String timestamp = LocalDateTime.now().format(TIMESTAMP);
            String safeName  = name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            File dest = new File(FrameworkConstants.SCREENSHOT_DIR + safeName + "_" + timestamp + ".png");
            FileUtils.forceMkdirParent(dest);
            FileUtils.writeByteArrayToFile(dest, screenshotBytes);

            LOG.info("Screenshot saved: {}", dest.getAbsolutePath());

        } catch (IOException e) {
            LOG.error("Failed to save screenshot for: {}", name, e);
        } catch (Exception e) {
            LOG.error("Screenshot capture failed for: {}", name, e);
        }
    }

    /**
     * Returns raw screenshot bytes without saving to disk (useful for inline Allure attachment).
     */
    public static byte[] captureAsBytes() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            LOG.error("Failed to capture screenshot bytes", e);
            return new byte[0];
        }
    }
}
