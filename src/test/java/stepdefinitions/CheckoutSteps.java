package stepdefinitions;

import io.cucumber.java.en.And;
import io.qameta.allure.Step;
import pages.CheckoutPage;

public class CheckoutSteps {

    private final CheckoutPage checkoutPage = new CheckoutPage();

    @And("user enters checkout details")
    @Step("Enter checkout details: John Doe 12345")
    public void enterCheckoutDetails() {
        checkoutPage.enterDetails("John", "Doe", "12345");
    }

    @And("user finishes order")
    @Step("Finish the order")
    public void finishOrder() {
        checkoutPage.finishOrder();
    }
}
