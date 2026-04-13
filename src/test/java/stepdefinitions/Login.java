package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.ProductsPage;
import utils.LoggerUtil;

public class Login {

    LoginPage loginPage = new LoginPage();
    ProductsPage productPage=new ProductsPage();
    SoftAssert soft=new SoftAssert();

    @Given("user logs in with username {string} and password {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }



    @Then("Error message should display")
    public void errorMessageShouldDisplay() {
        String a=productPage.pageName();
        LoggerUtil.info("will execute");
        Assert.assertEquals(a,"PRODUCTS","wrong page displayed");
        LoggerUtil.info("soft assertion passed this line prints ");
        Assert.assertTrue(productPage.ProductPageIsDisplayed(),"products page is not displayed");
    }

    @Then("products page is result {string}")
    public void productsPageIsResult(String result) {

            if (result.equalsIgnoreCase("Displayed")) {
                Assert.assertTrue(productPage.ProductPageIsDisplayed(),
                        "Expected Products page, but not displayed");
            } else {
                Assert.assertTrue(loginPage.LoginPageIsDisplayed(),
                        "User should remain on login page");
            }
        }
    }

