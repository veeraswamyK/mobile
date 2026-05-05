package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.CartPage;
import pages.CheckoutPage;
import pages.ProductsPage;
import utils.ContextManager;

public class Product {

    private static final String PRODUCT_NAME_KEY = "PRODUCT_NAME";

    private final ProductsPage  productsPage  = new ProductsPage();
    private final CartPage      cartPage      = new CartPage();
    private final CheckoutPage  checkoutPage  = new CheckoutPage();

    @When("user adds product to cart")
    @Step("Add first product to cart and store its name")
    public void addProductToCart() {
        String productName = productsPage.getFirstProductName();
        ContextManager.getContext().set(PRODUCT_NAME_KEY, productName);
        productsPage.addFirstProductToCart();
    }

    @When("user navigates to cart")
    @Step("Navigate to the cart")
    public void navigateToCart() {
        productsPage.goToCart();
    }

    @And("user proceeds to checkout")
    @Step("Click Checkout")
    public void proceedToCheckout() {
        cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isDisplayed(),
                "Checkout page should be visible after clicking Checkout");
    }
}
