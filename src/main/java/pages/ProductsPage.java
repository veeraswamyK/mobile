package pages;

import org.openqa.selenium.By;

import static utils.WaitUtils.waitForElement;

public class ProductsPage extends BasePage {

    private By firstProductAddToCart = By.xpath("(//android.widget.TextView[@text='ADD TO CART'])[1]");
    private By cartIcon = By.xpath("//android.view.ViewGroup[@content-desc='test-Car']");

    public void addFirstProductToCart() {
        waitForElement(firstProductAddToCart);
        click(firstProductAddToCart);
    }

    public void goToCart() {
        waitForElement(cartIcon);
        click(cartIcon);
    }
}