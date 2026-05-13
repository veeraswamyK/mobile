package pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final By CHECKOUT_BTN  = By.xpath("//android.view.ViewGroup[@content-desc='test-CHECKOUT']");
    private static final By CART_ICON     = AppiumBy.accessibilityId("test-Cart");
    private static final By CART_COUNT    = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/child::android.view.ViewGroup/descendant::android.widget.TextView");
    private static final By REMOVE_BTN    = By.xpath("//android.widget.TextView[@text='REMOVE']");
    private static final By TOTAL_PRICE   = By.xpath("//android.widget.TextView[contains(@text,'$')]");
    private static final By CART_ITEMS    = By.xpath("//android.widget.TextView[@content-desc='test-Item title']");
    private static final By CONTINUE_SHOP = By.xpath("//android.widget.TextView[@text='CONTINUE SHOPPING']");

    @Step("Click Checkout button")
    public void clickCheckout() {
        waitUntilVisible(CHECKOUT_BTN);
        click(CHECKOUT_BTN);
    }

    @Step("Click Cart icon")
    public void clickCartIcon() {
        click(CART_ICON);
    }

    @Step("Get total price from cart")
    public double getTotalPrice() {
        return findAllVisible(TOTAL_PRICE).stream()
                .map(e -> e.getText().replaceAll("[^0-9.]", ""))
                .filter(text -> !text.isBlank())
                .mapToDouble(Double::parseDouble)
                .sum();
    }

    @Step("Get cart item count (0 if badge absent)")
    public int getCartCount() {
        List<WebElement> badge = findAll(CART_COUNT);
        if (badge.isEmpty()) return 0;
        try {
            return Integer.parseInt(badge.get(0).getText().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Step("Remove first product from cart")
    public void removeFirstProduct() {
        click(REMOVE_BTN);
    }

    @Step("Click Continue Shopping")
    public void clickContinueShopping() {
        click(CONTINUE_SHOP);
    }

    @Step("Get all cart item names")
    public List<String> getCartItems() {
        return findAll(CART_ITEMS).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Check cart contains product: {productName}")
    public boolean containsProduct(String productName) {
        return getCartItems().contains(productName);
    }
}
