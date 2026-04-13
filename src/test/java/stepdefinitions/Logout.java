package stepdefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.LoginPage;
import pages.NavBarPage;
import pages.ProductsPage;

public class Logout {

    NavBarPage navbarPage = new NavBarPage();
    ProductsPage productsPage = new ProductsPage();
    LoginPage loginPage = new LoginPage();

    @Given("user is logged in")
    public void userIsLoggedIn() {
        if (loginPage.LoginPageIsDisplayed()) {
            loginPage.login("standard_user", "secret_sauce");
            Assert.assertTrue(productsPage.ProductPageIsDisplayed(),
                    "User is NOT logged in or Products page not visible");
        }
    }
    @When("user click on nav-bar$")
    public void NavBar() {
        navbarPage.navBarIcon();
    }
    @When("click on the navbar")
    public void clickOnTheNavbar() {
        navbarPage.navBarIcon();
    }
    @Then("nav bar options are displayed")
    public void navBarOptionsAreDisplayed() {
        Assert.assertTrue(navbarPage.isNavBarOptionsDisplayed(),
                "Navbar options are NOT displayed");
    }

    @When("user click on logout")
    public void userClickOnLogout() {
        navbarPage.logoutButton();
    }

    @Then("login page is displayed")
    public void loginPageIsDisplayed() {
        Assert.assertTrue(navbarPage.isLoginPageDisplayed(),
                "Login page is NOT displayed after logout");
    }

    @And("click on the qr code scanner")
    public void clickOnTheQrGeoLocation() {
        navbarPage.clickQrScanner();

        Assert.assertTrue(navbarPage.isScannerOpened(),
                "QR Scanner is NOT opened");
    }

    @And("click on the Geo location")
    public void clickOnTheGeoLocation() {
        navbarPage.clickQrScanner();
        Assert.assertTrue(navbarPage.isScannerOpened(),
                "QR Scanner is NOT opened");
    }

    @Then("About webpage is opened")
    public void aboutWebpageIsOpened() {

    }

    @Then("maps are opened")
    public void mapsAreOpened() {

    }
}