package stepdefinitions;

import io.cucumber.java.en.When;
import pages.ProductsPage;

public class Product  {

    ProductsPage productsPage = new ProductsPage();

    @When("user adds product to cart")
    public void addProduct() {
        productsPage.addFirstProductToCart();
    }

    @When("user navigates to cart")
    public void goToCart() {
        productsPage.goToCart();
    }
}