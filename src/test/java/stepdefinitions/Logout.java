package stepdefinitions;

import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.LoginPage;
import pages.NavBarPage;
import pages.ProductsPage;

public class Logout {

    private final NavBarPage    navBarPage   = new NavBarPage();
    private final ProductsPage  productsPage = new ProductsPage();
    private final LoginPage     loginPage    = new LoginPage();

    @Given("user is logged in")
    @Step("Ensure user is logged in")
    public void userIsLoggedIn() {
        if (productsPage.isDisplayed()) {
            return;
        }
        if (loginPage.isDisplayed()) {
            loginPage.login("standard_user", "secret_sauce");
        }
        Assert.assertTrue(productsPage.isDisplayed(),
                "Products page should be visible after login");
    }

    @When("click on the navbar")
    @Step("Open navigation menu")
    public void openNavBar() {
        navBarPage.openMenu();
    }

    @Then("nav bar options are displayed")
    public void navBarOptionsAreDisplayed() {
        Assert.assertTrue(navBarPage.areNavOptionsDisplayed(),
                "Navigation menu options should be visible");
    }

    @When("user click on logout")
    @Step("Click logout")
    public void clickLogout() {
        navBarPage.clickLogout();
    }

    @Then("login page is displayed")
    public void loginPageIsDisplayed() {
        Assert.assertTrue(navBarPage.isLoginPageDisplayed(),
                "Login page should be displayed after logout");
    }

    @And("click on the qr code scanner")
    @Step("Open QR code scanner")
    public void openQrScanner() {
        navBarPage.clickQrScanner();
    }

    @And("click on the Geo location")
    @Step("Click Geo location option")
    public void clickGeoLocation() {
        navBarPage.clickGeoLocation();
    }

    @Then("About webpage is opened")
    public void aboutWebpageIsOpened() {
        // Intentionally left for implementation when 'About' page support is added
    }

    @Then("maps are opened")
    public void mapsAreOpened() {
        // Intentionally left for implementation when maps navigation is added
    }

    @When("user click on nav-bar")
    public void userClickOnNavBar() {
            navBarPage.openMenu();
        }


}

