package pages;

import org.openqa.selenium.By;

import static utils.WaitUtils.waitForElement;

public class CartPage extends BasePage {

    private By checkoutBtn = By.xpath("//android.view.ViewGroup[@content-desc='test-CHECKOUT']");

    public void clickCheckout() {
        waitForElement(checkoutBtn);
        click(checkoutBtn);
    }
}