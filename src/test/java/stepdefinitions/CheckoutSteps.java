package stepdefinitions;

import io.cucumber.java.en.And;
import pages.CheckoutPage;

public class CheckoutSteps {

    CheckoutPage checkoutPage = new CheckoutPage();

    @And("user enters checkout details")
    public void enterDetails() {
        checkoutPage.enterDetails("John", "Doe", "12345");
    }

    @And("user finishes order")
    public void finishOrder() {
        checkoutPage.finishOrder();
    }
}