package stepdefinitions;

import io.cucumber.java.en.Given;
import pages.LoginPage;

public class Login {

    LoginPage loginPage = new LoginPage();

    @Given("user logs in with username {string} and password {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
    }
}