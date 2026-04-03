package stepdefinitions;

import io.cucumber.java.en.And;
import pages.CartPage;

public class CartSteps {

    CartPage cartPage = new CartPage();

    @And("user proceeds to checkout")
    public void checkout() {
        cartPage.clickCheckout();
    }
}