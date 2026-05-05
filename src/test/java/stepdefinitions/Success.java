package stepdefinitions;

import io.cucumber.java.en.Then;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.SuccessPage;

public class Success {

    private final SuccessPage successPage = new SuccessPage();

    @Then("Product purchase is displayed")
    @Step("Verify order success page is displayed")
    public void verifyOrderSuccess() {
        Assert.assertTrue(successPage.isDisplayed(),
                "Order success page should be displayed after completing checkout");
    }
}
