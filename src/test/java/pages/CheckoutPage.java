package pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME   = By.xpath("//android.widget.EditText[@content-desc='test-First Name']");
    private static final By LAST_NAME    = By.xpath("//android.widget.EditText[@content-desc='test-Last Name']");
    private static final By ZIP_CODE     = By.xpath("//android.widget.EditText[@content-desc='test-Zip/Postal Code']");
    private static final By CONTINUE_BTN = By.xpath("//android.widget.TextView[@text='CONTINUE']");
    private static final By FINISH_BTN   = By.xpath("//android.widget.TextView[@text='FINISH']");
    private static final By CANCEL_BTN   = AppiumBy.androidUIAutomator("new UiSelector().text(\"CANCEL\")");
    private static final By CHECKOUT_HDR = AppiumBy.androidUIAutomator("new UiSelector().text(\"CHECKOUT: INFORMATION\")");

    @Step("Enter checkout details: {firstName} {lastName} {zipCode}")
    public void enterDetails(String firstName, String lastName, String zipCode) {
        waitUntilVisible(FIRST_NAME);
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(ZIP_CODE, zipCode);
        hideKeyboard();
        click(CONTINUE_BTN);
    }

    @Step("Finish order")
    public void finishOrder() {
        scrollToElement(FINISH_BTN);
        click(FINISH_BTN);
    }

    @Step("Click Continue")
    public void clickContinue() {
        click(CONTINUE_BTN);
    }

    @Step("Click Cancel on checkout")
    public void clickCancel() {
        click(CANCEL_BTN);
    }

    @Step("Verify checkout page is displayed")
    public boolean isDisplayed() {
        return isVisible(CHECKOUT_HDR);
    }
}
