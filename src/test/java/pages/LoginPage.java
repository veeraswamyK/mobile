package pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By USERNAME  = AppiumBy.accessibilityId("test-Username");
    private static final By PASSWORD  = AppiumBy.accessibilityId("test-Password");
    private static final By LOGIN_BTN = AppiumBy.accessibilityId("test-LOGIN");
    private static final By ERROR_MSG = AppiumBy.accessibilityId("test-Error message");

    @Step("Login with username='{username}'")
    public void login(String username, String password) {
        waitUntilVisible(USERNAME);
        type(USERNAME, username);
        type(PASSWORD, password);
        hideKeyboard();
        click(LOGIN_BTN);
    }

    @Step("Check login page is displayed")
    public boolean isDisplayed() {
        return isVisible(LOGIN_BTN);
    }

    @Step("Get login error message text")
    public String getErrorMessage() {
        return getText(ERROR_MSG);
    }

    @Step("Check error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isVisible(ERROR_MSG);
    }
}
