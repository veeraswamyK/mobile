package stepdefinitions;

import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.*;
import utils.ContextManager;

import java.util.List;

public class CartSteps {

    private static final String PRODUCT_NAME_KEY = "PRODUCT_NAME";

    private final LoginPage    loginPage    = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final CartPage     cartPage     = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();
    private final NavBarPage   navBarPage   = new NavBarPage();

    private int    initialCount;
    private double expectedTotal;

    // ─── Setup ────────────────────────────────────────────────────────────────

    @Given("User is in products page")
    @Step("Ensure user is on Products page")
    public void userIsInProductsPage() {
        if (loginPage.isDisplayed()) {
            loginPage.login("standard_user", "secret_sauce");
            Assert.assertTrue(productsPage.isDisplayed(),
                    "Products page should be visible after login");
        }
    }

    // ─── Count verification ───────────────────────────────────────────────────

    @Then("Count of products in cart gets updated")
    @Step("Verify cart badge count increased")
    public void cartCountGetsUpdated() {
        Assert.assertTrue(productsPage.addProductAndVerifyCount(),
                "Cart badge count did not update after adding a product");
    }

    // ─── Single product flow ──────────────────────────────────────────────────

    @And("selected item is appeared")
    @Step("Verify selected product appears in cart")
    public void selectedItemAppearsInCart() {
        String expected = ContextManager.getContext().get(PRODUCT_NAME_KEY, String.class);
        productsPage.goToCart();
        Assert.assertTrue(cartPage.containsProduct(expected),
                "Product '" + expected + "' not found in cart");
    }

    // ─── Multiple products ────────────────────────────────────────────────────

    @When("user adds product to cart multiple times")
    @Step("Add two different products to cart")
    public void addMultipleProductsToCart() {
        initialCount = productsPage.getCartCount();
        productsPage.addFirstProductToCart();
        productsPage.addSecondProductToCart();
        int updated = productsPage.getCartCount();
        Assert.assertTrue(updated > initialCount,
                "Cart count did not increase after adding multiple products");
    }

    @When("user removes product from cart")
    @Step("Navigate to cart and remove a product")
    public void removeProductFromCart() {
        int before = productsPage.getCartCount();
        cartPage.clickCartIcon();
        cartPage.removeFirstProduct();
        int after = productsPage.getCartCount();
        Assert.assertTrue(after < before,
                "Cart count should decrease after removing a product");
    }

    @And("user removes product from products page")
    @Step("Remove product via REMOVE button on products page")
    public void removeProductFromProductsPage() {
        int before = productsPage.getCartCount();
        cartPage.removeFirstProduct();
        int after = productsPage.getCartCount();
        Assert.assertTrue(after < before,
                "Cart count should decrease after removing a product");
    }

    @When("user adds product to cart same item multiple times")
    @Step("Attempt to add the same product twice")
    public void addSameProductTwice() {
        productsPage.addFirstProductToCart();
        productsPage.addFirstProductToCart();
    }

    @And("No duplication of the product is found")
    @Step("Verify no duplicate products exist in the cart")
    public void noDuplicatesInCart() {
        productsPage.goToCart();
        List<String> items = cartPage.getCartItems();
        long uniqueCount = items.stream().distinct().count();
        Assert.assertEquals(items.size(), uniqueCount,
                "Duplicate products found in cart: " + items);
    }

    // ─── Reset app ────────────────────────────────────────────────────────────

    @When("click on reset app")
    @Step("Reset app state via navigation menu")
    public void resetAppState() {
        navBarPage.openMenu();
        navBarPage.clickResetAppState();
    }

    @Then("products in cart should be empty")
    @Step("Verify cart is empty after reset")
    public void cartShouldBeEmpty() {
        Assert.assertEquals(productsPage.getCartCount(), 0,
                "Cart should be empty after resetting app state");
    }

    // ─── Price calculation ────────────────────────────────────────────────────

    @Given("Verify correct price calculation")
    public void verifyCorrectPriceCalculation() {
        // precondition assertion handled by userIsInProductsPage step
    }

    @When("user adds multiple product to cart")
    @Step("Add all visible products and capture expected total")
    public void addMultipleProductsAndCaptureTotal() {
        List<Double> prices = productsPage.addAllProductsAndGetPrices();
        expectedTotal = prices.stream().mapToDouble(Double::doubleValue).sum();
    }

    @Then("calculate the total price")
    @Step("Verify cart total matches sum of product prices")
    public void verifyTotalPrice() {
        productsPage.goToCart();
        double actual = cartPage.getTotalPrice();
        Assert.assertEquals(actual, expectedTotal, 0.01,
                String.format("Expected total %.2f but was %.2f", expectedTotal, actual));
    }

    // ─── Continue shopping ────────────────────────────────────────────────────

    @When("click on cart")
    @Step("Navigate to cart")
    public void clickOnCart() {
        productsPage.goToCart();
    }

    @And("click on Continue shopping button")
    @Step("Click Continue Shopping")
    public void clickContinueShopping() {
        cartPage.clickContinueShopping();
    }

    // ─── Checkout navigation ──────────────────────────────────────────────────

    @Given("Verify Checkout button navigates correctly")
    public void verifyCheckoutButtonPrecondition() {
        Assert.assertTrue(productsPage.isDisplayed(),
                "Products page should be visible before checkout flow");
    }

    @And("click on checkout")
    @Step("Click Checkout button in cart")
    public void clickOnCheckout() {
        cartPage.clickCheckout();
    }

    @Then("Address page is displayed")
    @Step("Verify checkout (address) page is displayed")
    public void addressPageIsDisplayed() {
        Assert.assertTrue(checkoutPage.isDisplayed(),
                "Checkout page should be displayed");
    }
}
