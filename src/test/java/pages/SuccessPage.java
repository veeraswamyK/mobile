package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import static utils.WaitUtils.waitForElement;

public class SuccessPage extends BasePage{

    //private By message = By.xpath("(//android.widget.TextView[@text='ADD TO CART'])[1]");
    private By message = AppiumBy.accessibilityId("test-CHECKOUT: COMPLETE!");
    public void messageElement() {
        waitForElement(message);
        click(message);
    }

}
