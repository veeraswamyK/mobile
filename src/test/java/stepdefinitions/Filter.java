package stepdefinitions;

import io.cucumber.java.en.*;
import io.qameta.allure.Step;
import org.testng.Assert;
import pages.ProductsPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filter {

    private final ProductsPage productsPage = new ProductsPage();

    private List<String> capturedNames;
    private List<Double> capturedPrices;

    // ─── View toggle ──────────────────────────────────────────────────────────

    @Given("user is in card view")
    public void userIsInCardView() {
        Assert.assertTrue(productsPage.isDisplayed(),
                "Products page should be visible");
    }

    @When("user click on the view button")
    @Step("Click the view toggle button")
    public void clickViewButton() {
        productsPage.clickViewToggle();
    }

    @Then("Product visibility is changed")
    @Step("Verify product list view is active")
    public void productVisibilityChanged() {
        Assert.assertTrue(productsPage.isListView(),
                "View was not toggled to list view");
    }

    // ─── Filter dialog ────────────────────────────────────────────────────────

    @When("user click on filter icon")
    @Step("Open the filter/sort dialog")
    public void clickFilterIcon() {
        productsPage.clickFilter();
    }

    @Then("Sort items by pop-up is displayed")
    @Step("Verify sort popup is displayed")
    public void sortPopupIsDisplayed() {
        Assert.assertTrue(productsPage.isSortPopupDisplayed(),
                "Sort popup was not displayed");
    }

    // ─── A-Z sort ─────────────────────────────────────────────────────────────

    @Given("User able to sort the products based on the naming A-Z")
    @Step("Capture product names before A-Z sort")
    public void captureNamesBeforeAzSort() {
        capturedNames  = productsPage.getProductNames();
        capturedPrices = null;
    }

    @When("user selects the a-z")
    @Step("Select 'Name (A to Z)' sort option")
    public void selectAzSort() {
        productsPage.selectFilterOption("Name (A to Z)");
    }

    // ─── Z-A sort ─────────────────────────────────────────────────────────────

    @Given("User able to sort the products based on the naming Z-A")
    @Step("Capture product names before Z-A sort")
    public void captureNamesBeforeZaSort() {
        capturedNames  = productsPage.getProductNames();
        capturedPrices = null;
    }

    @When("user selects the z-a")
    @Step("Select 'Name (Z to A)' sort option")
    public void selectZaSort() {
        productsPage.selectFilterOption("Name (Z to A)");
    }

    // ─── Price low → high ─────────────────────────────────────────────────────

    @Given("User able to sort the products based on the price low to high")
    @Step("Capture product prices before low-to-high sort")
    public void capturePricesBeforeLowHigh() {
        capturedPrices = productsPage.getProductPrices();
        capturedNames  = null;
    }

    @When("user selects the price low to high")
    @Step("Select 'Price (low to high)' sort option")
    public void selectPriceLowHigh() {
        productsPage.selectFilterOption("Price (low to high)");
    }

    // ─── Price high → low ─────────────────────────────────────────────────────

    @Given("User able to sort the products based on the price high to low")
    @Step("Capture product prices before high-to-low sort")
    public void capturePricesBeforeHighLow() {
        capturedPrices = productsPage.getProductPrices();
        capturedNames  = null;
    }

    @When("user selects the price high to low")
    @Step("Select 'Price (high to low)' sort option")
    public void selectPriceHighLow() {
        productsPage.selectFilterOption("Price (high to low)");
    }

    // ─── Shared sort validation ───────────────────────────────────────────────

    @Then("list of products are in selected order")
    @Step("Verify products are sorted in the selected order")
    public void verifyProductsAreSorted() {
        if (capturedNames != null) {
            verifyNameSort();
        } else if (capturedPrices != null) {
            verifyPriceSort();
        }
    }

    private void verifyNameSort() {
        List<String> actual = productsPage.getProductNames();
        List<String> expected = new ArrayList<>(actual);

        boolean isAscending = actual.get(0).compareTo(actual.get(actual.size() - 1)) <= 0;
        if (isAscending) {
            Collections.sort(expected);
        } else {
            expected.sort(Collections.reverseOrder());
        }

        Assert.assertEquals(actual, expected,
                "Product names are not in the expected sorted order");
    }

    private void verifyPriceSort() {
        List<Double> actual = productsPage.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);

        boolean isAscending = actual.get(0) <= actual.get(actual.size() - 1);
        if (isAscending) {
            Collections.sort(expected);
        } else {
            expected.sort(Collections.reverseOrder());
        }

        Assert.assertEquals(actual, expected,
                "Product prices are not in the expected sorted order");
    }

    // ─── Cancel sort ──────────────────────────────────────────────────────────

    @Given("User able to cancel the sorting of the products")
    public void userCanCancelSorting() {
        Assert.assertTrue(productsPage.isDisplayed(),
                "Products page should be visible before testing cancel");
    }

    @When("user clicks on cancel")
    @Step("Cancel the sort dialog")
    public void clickCancel() {
        productsPage.clickCancel();
    }
}
