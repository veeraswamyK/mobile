package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static utils.WaitUtils.waitForElement;

public class CartPage extends BasePage {


    private By checkoutBtn = By.xpath("//android.view.ViewGroup[@content-desc='test-CHECKOUT']");
    private By cartIcon = AppiumBy.accessibilityId("test-Cart");
    private By cartCount = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/child::android.view.ViewGroup/descendant::android.widget.TextView");
    private By remove=AppiumBy.androidUIAutomator("new UiSelector().text('REMOVE')");
    private By totalPrice= AppiumBy.accessibilityId("test-Price");

    public void clickCheckout() {
        waitForElement(checkoutBtn);
        click(checkoutBtn);
    }


    public double getTotalPrice() {
        try {
            waitForElement(totalPrice);

            String totalText = getText(totalPrice).trim();

            return Double.parseDouble(totalText.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch total price", e);
        }
    }
    public void clickCCart() {
        waitForElement(cartIcon);
        click(cartIcon);
    }
    public void removeProduct(){
        click(remove);
    }

    public String countOfProducts() {
        return getText(cartCount);
    }
    public boolean isProductDisplayed() {
        return new ProductsPage().ProductPageIsDisplayed();
    }

    public List<String> getCartItems() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//android.widget.TextView[@content-desc='test-Item title']")
        );

        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


}