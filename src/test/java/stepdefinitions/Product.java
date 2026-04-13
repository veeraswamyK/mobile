package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.CartPage;
import pages.CheckoutPage;
import pages.ProductsPage;
import utils.ContextManager;


public class Product  {

    ProductsPage productsPage = new ProductsPage();
    CartPage cartPage = new CartPage();
    CheckoutPage checkoutPage=new CheckoutPage();


    @When("user adds product to cart")
    public void addProduct() {

        String productName = productsPage.getFirstProductName();

        ContextManager.getContext().set("PRODUCT_NAME", productName);

        productsPage.addFirstProductToCart();
    }

    @When("user navigates to cart")
    public void goToCart() {
        productsPage.goToCart();
    }

    @And("user proceeds to checkout")
    public void userProceedsToCheckout() {
        checkoutPage.isCheckoutPageDisplayed();
    }
}