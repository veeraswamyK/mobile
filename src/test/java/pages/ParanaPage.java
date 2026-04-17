package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import static utils.WaitUtils.waitForElement;

public class parana extends BasePage {

        private By close = AppiumBy.accessibilityId("Cancel Challenge");
        private By mobile = By.xpath(
                "//*[contains(@content-desc,'Mobile OTP')]"
        );
    public void close() {
        waitForElement(close);
        click(close);
    }
    public void mobile() {
        waitForElement(mobile);
        click(mobile);
    }
}
