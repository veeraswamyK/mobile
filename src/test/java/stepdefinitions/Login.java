package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.LoginPage;
import pages.ProductsPage;

public class Login {

    private final LoginPage loginPage     = new LoginPage();
    private final ProductsPage productsPage = new ProductsPage();

    @Given("user logs in with username {string} and password {string}")
    @Step("Login with username='{0}' and password='***'")
    public void login(String username, String password) {
        if (productsPage.isDisplayed()) {
            return;
        }
        loginPage.login(username, password);
    }

    @Then("products page is displayed")
    public void productsPageIsDisplayed() {
        Assert.assertTrue(productsPage.isDisplayed(),
                "Products page not displayed after login");
    }

    @Then("products page is result {string}")
    public void productsPageIsResult(String expectedResult) {
        if (expectedResult.equalsIgnoreCase("Displayed")) {
            Assert.assertTrue(productsPage.isDisplayed(),
                    "Expected Products page to be displayed but it was not");
        } else {
            Assert.assertTrue(loginPage.isDisplayed(),
                    "Expected user to remain on the Login page");
        }
    }

    @Then("Error message should display")
    public void errorMessageShouldDisplay() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Expected error message to be displayed for invalid login");
    }
}
