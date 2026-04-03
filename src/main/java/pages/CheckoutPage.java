package pages;

import org.openqa.selenium.By;
import utils.GestureUtils;

import static utils.WaitUtils.waitForElement;

public class CheckoutPage extends BasePage {

    private By firstName = By.xpath("//android.widget.EditText[@content-desc='test-First Name']");
    private By lastName = By.xpath("//android.widget.EditText[@content-desc='test-Last Name']");
    private By zip = By.xpath("//android.widget.EditText[@content-desc='test-Zip/Postal Code']");
    private By continueBtn = By.xpath("//android.widget.TextView[@text='CONTINUE']");
    private By finishBtn = By.xpath("//android.widget.TextView[@text='FINISH']");

    public void enterDetails(String fName, String lName, String zipCode) {
        waitForElement(firstName);
        type(firstName, fName);
        type(lastName, lName);
        type(zip, zipCode);
        click(continueBtn);
    }

    public void finishOrder() {
        GestureUtils.scrollToElement(finishBtn);
        click(finishBtn);
    }
}