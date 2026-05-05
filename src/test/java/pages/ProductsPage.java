package pages;

import constants.FrameworkConstants;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsPage extends BasePage {

    private static final By PAGE_TITLE       = By.xpath("//android.widget.TextView[@text='PRODUCTS']");
    private static final By FIRST_ADD_CART   = By.xpath("(//android.widget.TextView[@text='ADD TO CART'])[1]");
    private static final By SECOND_ADD_CART  = By.xpath("(//android.widget.TextView[@text='ADD TO CART'])[2]");
    private static final By ALL_ADD_CART     = By.xpath("//android.widget.TextView[@text='ADD TO CART']");
    private static final By CART_ICON        = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']");
    private static final By CART_COUNT       = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/child::android.view.ViewGroup/descendant::android.widget.TextView");
    private static final By VIEW_TOGGLE      = AppiumBy.accessibilityId("test-Toggle");
    private static final By FILTER_BTN       = AppiumBy.accessibilityId("test-Modal Selector Button");
    private static final By SORT_POPUP_TITLE = By.xpath("//android.widget.TextView[@text='Sort Items']");
    private static final By PRODUCT_NAMES    = By.xpath("//android.widget.TextView[@content-desc='test-Item title']");
    private static final By PRODUCT_PRICES   = By.xpath("//android.widget.TextView[@content-desc='test-Price']");
    private static final By LIST_VIEW_IMGS   = By.xpath("//android.widget.ImageView");

    @Step("Verify Products page is displayed")
    public boolean isDisplayed() {
        return isVisible(PAGE_TITLE);
    }

    @Step("Get Products page title")
    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    @Step("Add first product to cart")
    public void addFirstProductToCart() {
        waitUntilVisible(FIRST_ADD_CART);
        click(FIRST_ADD_CART);
    }

    @Step("Add second product to cart")
    public void addSecondProductToCart() {
        waitUntilVisible(SECOND_ADD_CART);
        click(SECOND_ADD_CART);
    }

    /**
     * Adds all visible products to cart and returns their prices captured before clicking.
     */
    @Step("Add all visible products to cart and return their prices")
    public List<Double> addAllProductsAndGetPrices() {
        List<WebElement> priceElements = findAllVisible(PRODUCT_PRICES);
        List<Double> prices = priceElements.stream()
                .map(e -> parsePrice(e.getText()))
                .collect(Collectors.toList());

        findAll(ALL_ADD_CART).forEach(WebElement::click);
        return prices;
    }

    @Step("Get current cart item count (0 if badge absent)")
    public int getCartCount() {
        List<WebElement> badge = findAll(CART_COUNT);
        if (badge.isEmpty()) return 0;
        try {
            return Integer.parseInt(badge.get(0).getText().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Clicks "ADD TO CART" on the first product and waits for the cart badge to increment.
     */
    @Step("Add first product to cart and verify badge increments")
    public boolean addProductAndVerifyCount() {
        int before = getCartCount();
        click(FIRST_ADD_CART);
        return WaitUtils.getFluentWait(FrameworkConstants.EXPLICIT_WAIT_SECONDS)
                .until(d -> getCartCount() > before);
    }

    @Step("Navigate to cart")
    public void goToCart() {
        click(CART_ICON);
    }

    @Step("Click view toggle button")
    public void clickViewToggle() {
        click(VIEW_TOGGLE);
    }

    @Step("Open filter / sort dialog")
    public void clickFilter() {
        click(FILTER_BTN);
    }

    @Step("Check sort popup is displayed")
    public boolean isSortPopupDisplayed() {
        return isVisible(SORT_POPUP_TITLE);
    }

    @Step("Select filter/sort option: {option}")
    public void selectFilterOption(String option) {
        click(By.xpath("//android.widget.TextView[@text='" + option + "']"));
    }

    @Step("Cancel sort dialog")
    public void clickCancel() {
        click(By.xpath("//android.widget.TextView[@text='Cancel']"));
    }

    @Step("Get name of the first product")
    public String getFirstProductName() {
        return findAll(PRODUCT_NAMES).get(0).getText();
    }

    @Step("Get all product names")
    public List<String> getProductNames() {
        return findAll(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Get all product prices as doubles")
    public List<Double> getProductPrices() {
        return findAll(PRODUCT_PRICES).stream()
                .map(e -> parsePrice(e.getText()))
                .collect(Collectors.toList());
    }

    @Step("Check list view is active")
    public boolean isListView() {
        return !findAll(LIST_VIEW_IMGS).isEmpty();
    }

    private double parsePrice(String priceText) {
        return Double.parseDouble(priceText.replace("$", "").trim());
    }
}
