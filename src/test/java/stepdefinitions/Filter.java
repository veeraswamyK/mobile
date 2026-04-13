package stepdefinitions;

import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.ProductsPage;

import java.util.*;

public class Filter {

    ProductsPage productsPage = new ProductsPage();

    List<String> originalNames;
    List<Double> originalPrices;

    // ---------------- VIEW CHANGE ----------------

    @Given("user is in card view")
    public void view() {
        Assert.assertTrue(productsPage.ProductPageIsDisplayed());
    }

    @Then("Product visibility is changed")
    public void productVisibilityIsChanged() {
        Assert.assertTrue(productsPage.isListView(),
                "View was not changed");
    }

    // ---------------- FILTER CLICK ----------------

    @When("user click on filter icon")
    public void userClickOnFilterIcon() {
        productsPage.clickFilter();
    }

    @Then("Sort items by pop-up is displayed")
    public void sortItemsByPopUpIsDisplayed() {
        Assert.assertTrue(productsPage.isSortPopupDisplayed(),
                "Sort popup not displayed");
    }

    // ---------------- A-Z SORT ----------------

    @Given("User able to sort the products based on the naming A-Z")
    public void userAbleToSortTheProductsBasedOnTheNamingAZ() {
        originalNames = productsPage.getProductNames();
    }

    @When("user selects the a-z")
    public void userSelectsTheAZ() {
        productsPage.selectFilterOption("Name (A to Z)");
    }

    // ---------------- Z-A SORT ----------------

    @Given("User able to sort the products based on the naming Z-A")
    public void userAbleToSortTheProductsBasedOnTheNamingZA() {
        originalNames = productsPage.getProductNames();
    }

    @When("user selects the z-a")
    public void userSelectsTheZA() {
        productsPage.selectFilterOption("Name (Z to A)");
    }

    // ---------------- PRICE LOW → HIGH ----------------

    @Given("User able to sort the products based on the price low to high")
    public void userAbleToSortTheProductsBasedOnThePriceLowToHigh() {
        originalPrices = productsPage.getProductPrices();
    }

    @When("user selects the price low to high")
    public void userSelectsThePriceLowToHigh() {
        productsPage.selectFilterOption("Price (low to high)");
    }

    // ---------------- PRICE HIGH → LOW ----------------

    @Given("User able to sort the products based on the price high to low")
    public void userAbleToSortTheProductsBasedOnThePriceHighToLow() {
        originalPrices = productsPage.getProductPrices();
    }

    @When("user selects the price high to low")
    public void userSelectsThePriceHighToLow() {
        productsPage.selectFilterOption("Price (high to low)");
    }

    // ---------------- VALIDATION ----------------

    @Then("list of products are in selected order")
    public void listOfProductsAreInSelectedOrder() {

        // Try names first
        if (originalNames != null) {
            List<String> actual = productsPage.getProductNames();
            List<String> sorted = new ArrayList<>(actual);

            if (actual.get(0).compareTo(actual.get(actual.size()-1)) < 0) {
                Collections.sort(sorted); // A-Z
            } else {
                sorted.sort(Collections.reverseOrder()); // Z-A
            }

            Assert.assertEquals(actual, sorted,
                    "Products are NOT sorted correctly");
        }

        // Try prices
        if (originalPrices != null) {
            List<Double> actual = productsPage.getProductPrices();
            List<Double> sorted = new ArrayList<>(actual);

            if (actual.get(0) < actual.get(actual.size()-1)) {
                Collections.sort(sorted); // low-high
            } else {
                sorted.sort(Collections.reverseOrder()); // high-low
            }

            Assert.assertEquals(actual, sorted,
                    "Prices are NOT sorted correctly");
        }
    }

    // ---------------- CANCEL ----------------

    @Given("User able to cancel the sorting of the products")
    public void userAbleToCancelTheSortingOfTheProducts() {
        Assert.assertTrue(productsPage.ProductPageIsDisplayed());
    }

    @When("user clicks on cancel")
    public void userClicksOnCancel() {
        productsPage.clickCancel();
    }


}