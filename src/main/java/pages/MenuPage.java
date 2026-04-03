package pages;

import org.openqa.selenium.By;

import static utils.WaitUtils.waitForElement;

public class MenuPage extends BasePage {

    private By menuBtn = By.xpath("//android.view.ViewGroup[@content-desc='test-Menu']");
    private By logoutBtn = By.xpath("//android.view.ViewGroup[@content-desc='test-LOGOUT']");

    public void logout() {
        waitForElement(menuBtn);
        click(menuBtn);
        click(logoutBtn);
    }
}