package pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SuccessPage extends BasePage {

    private static final By SUCCESS_CONTAINER = AppiumBy.accessibilityId("test-CHECKOUT: COMPLETE!");
    private static final By SUCCESS_TITLE     = By.xpath("//android.widget.TextView[@text='THANK YOU FOR YOUR ORDER']");
    private static final By BACK_HOME_BTN     = By.xpath("//android.widget.TextView[@text='BACK HOME']");

    @Step("Verify order success page is displayed")
    public boolean isDisplayed() {
        return isVisible(SUCCESS_CONTAINER);
    }

    @Step("Verify thank-you message is present")
    public boolean isThankyouMessageDisplayed() {
        return isVisible(SUCCESS_TITLE);
    }

    @Step("Get success message text")
    public String getSuccessMessage() {
        return getText(SUCCESS_TITLE);
    }

    @Step("Click Back Home")
    public void clickBackHome() {
        click(BACK_HOME_BTN);
    }
}
