package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.SuccessPage;

public class Success {

    SuccessPage successPage = new SuccessPage();

    @Then("Product purchase is displayed")
    public void SuccessEle() {
        successPage.messageElement();
    }
}