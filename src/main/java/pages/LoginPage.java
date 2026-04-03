package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import static utils.WaitUtils.waitForElement;

public class LoginPage extends BasePage  {

    private By username = By.xpath("//android.widget.EditText[@text='Username']");
    private By password = AppiumBy.accessibilityId("test-Password");
    private By loginBtn = AppiumBy.accessibilityId("test-LOGIN");

    public void login(String user, String pass) {
        waitForElement(username);
        type(username, user);
        type(password, pass);
        click(loginBtn);
    }
}