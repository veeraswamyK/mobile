package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.*;
import utils.ContextManager;


import java.util.List;

public class CartSteps extends BasePage {
    NavBarPage navBarPage=new NavBarPage();
    CartPage cartPage = new CartPage();
    ProductsPage productsPage = new ProductsPage();
    CheckoutPage checkoutPage=new CheckoutPage();
    LoginPage loginPage = new LoginPage();

    int initialCount;
    int updatedCount;
    double expectedTotal;

    @Given("User is in products page")
    public void userIsInProductsPage() {
        if (loginPage.LoginPageIsDisplayed()) {
            loginPage.login("standard_user", "secret_sauce");
            Assert.assertTrue(productsPage.ProductPageIsDisplayed(),
                    "User is NOT logged in or Products page not visible");
            Assert.assertTrue(productsPage.ProductPageIsDisplayed(),
                    "Products page is NOT displayed");
        }
    }
    @Then("Count of products in cart gets updated")
    public void countOfProductsInCartGetsUpdated() {
        Assert.assertTrue(productsPage.verifyProductCount(),
                "Cart count did NOT update");
    }

    @And("selected item is appeared")
    public void selectedItemIsAppeared() {
        String expectedProduct =
                (String) ContextManager.getContext().get("PRODUCT_NAME");

        productsPage.goToCart();

        List<String> cartItems = cartPage.getCartItems();

        Assert.assertTrue(cartItems.contains(expectedProduct),
                "Product not found in cart: " + expectedProduct);
    }


    @When("user adds product to cart multiple times")
    public void userAddsProductToCartMultipleTimes() {
        initialCount = productsPage.getCartCountSafe();

        productsPage.addFirstProductToCart();
        productsPage.addSecondProductToCart();

        updatedCount = productsPage.getCartCountSafe();

        Assert.assertTrue(updatedCount > initialCount,
                "Cart count did not increase after adding multiple products");
    }


    @When("user removes product from cart")
    public void userRemovesProductToCart() {
        int beforeRemove = productsPage.getCartCountSafe();
        cartPage.clickCCart();
        cartPage.removeProduct();

        int afterRemove = productsPage.getCartCountSafe();

        Assert.assertTrue(afterRemove < beforeRemove,
                "Product was not removed from cart");
    }
    @And("user removes product from products page")
    public void userRemovesProductFromProductsPage() {
        int beforeRemove = productsPage.getCartCountSafe();

        cartPage.removeProduct();

        int afterRemove = productsPage.getCartCountSafe();

        Assert.assertTrue(afterRemove < beforeRemove,
                "Product was not removed from cart");

    }


    @When("user adds product to cart same item multiple times")
    public void userAddsProductToCartSameItemMultipleTimes() {
        productsPage.addFirstProductToCart();
        productsPage.addFirstProductToCart();
    }

    @And("No duplication of the product is found")
    public void noDuplicationOfTheProductIsFound() {
        productsPage.goToCart();

        List<String> items = cartPage.getCartItems();

        long uniqueCount = items.stream().distinct().count();

        Assert.assertEquals(items.size(), uniqueCount,
                "Duplicate products found in cart!");
    }


    @When("click on reset app")
    public void clickOnResetApp() {
        navBarPage.navBarIcon();
        navBarPage.resetAppState();
    }

    @Then("products in cart should be empty")
    public void productsInCartShouldBeEmpty() {
        Assert.assertEquals(productsPage.getCartCountSafe(), 0,
                "Cart is NOT empty after reset");
    }

    @Given("Verify correct price calculation")
    public void verifyCorrectPriceCalculation() {

    }

    @When("user adds multiple product to cart")
    public void userAddsMultipleProductToCart() {
        List<Double> prices = productsPage.addMultipleProductsAndGetPrices();
        expectedTotal = prices.stream().mapToDouble(Double::doubleValue).sum();
    }

    @Then("calculate the total price")
    public void calculateTheTotalPrice() {
        productsPage.goToCart();

        double actualTotal = cartPage.getTotalPrice();

        Assert.assertEquals(actualTotal, expectedTotal,
                "Total price calculation is incorrect");
    }

    @When("click on cart")
    public void clickOnCart() {
        productsPage.goToCart();
    }

    @And("click on Continue shopping button")
    public void clickOnContinueShoppingButton() {
        checkoutPage.clickContinueShopping();
    }

    @Then("products page is displayed")
    public void productsPageIsDisplayed() {
        Assert.assertTrue(productsPage.ProductPageIsDisplayed(),
                "Did not navigate back to Products page");
    }

    // -------------------- CHECKOUT FLOW --------------------

    @Given("Verify Checkout button navigates correctly")
    public void verifyCheckoutButtonNavigatesCorrectly() {
        Assert.assertTrue(productsPage.ProductPageIsDisplayed());
    }

    @And("click on checkout")
    public void clickOnCheckout() {
        cartPage.clickCheckout();
    }

    @Then("Address page is displayed")
    public void addressPageIsDisplayed() {
        Assert.assertTrue(checkoutPage.isCheckoutPageDisplayed(),
                "Checkout page is NOT displayed");
    }


}