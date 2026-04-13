package pages;

import core.DriverManager;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static utils.WaitUtils.*;


public class ProductsPage extends BasePage {

    private By firstProductAddToCart = By.xpath("(//android.widget.TextView[@text='ADD TO CART'])[1]");
    private By cartIcon = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']");
    private By productpage = By.xpath("//android.widget.TextView[@text='PRODUCTS']");
    private By viewButton = AppiumBy.accessibilityId("test-Toggle");
    private By filterButton = AppiumBy.accessibilityId("test-Modal Selector Button");
    private By cartCount = By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/child::android.view.ViewGroup/descendant::android.widget.TextView");
    private By secondProductAddToCart = By.xpath("(//android.widget.TextView[@text='ADD TO CART'])[1]");


    public void addFirstProductToCart() {
        waitForElement(firstProductAddToCart);
        click(firstProductAddToCart);
    }
    public  void addSecondProductToCart(){
        waitForElement(secondProductAddToCart);
        click(secondProductAddToCart);
    }
    public List<Double> addMultipleProductsAndGetPrices(){
        List<Double> prices = new ArrayList<>();

        // Locate all product price elements
        List<WebElement> priceElements = driver.findElements(
                By.xpath("//android.widget.TextView[@content-desc='test-Price']")
        );

        // Locate all Add to Cart buttons
        List<WebElement> addButtons = driver.findElements(
                By.xpath("//android.widget.TextView[@text='ADD TO CART']")
        );

        for (int i = 0; i < addButtons.size(); i++) {

            // 🎯 Capture price BEFORE clicking
            String priceText = priceElements.get(i).getText();

            double price = Double.parseDouble(priceText.replace("$", "").trim());

            prices.add(price);

            // 🛒 Add product to cart
            addButtons.get(i).click();
        }

        return prices;
    }
    public int getCartCountSafe() {
        if (DriverManager.getDriver().findElements(cartCount).size() > 0) {
            String text = new CartPage().countOfProducts().trim();
            return Integer.parseInt(text.replaceAll("[^0-9]", ""));
        }
        return 0;
    }
    public boolean verifyProductCount() {

        int initialCount = getCartCountSafe();

        click(firstProductAddToCart);


        boolean isUpdated = getWait().until(driver ->
                getCartCountSafe() > initialCount
        );

        int finalCount = getCartCountSafe();

        return isUpdated && finalCount > initialCount;
    }

    public void goToCart() {
        waitForElement(cartIcon);
        click(cartIcon);
    }
    public  boolean ProductPageIsDisplayed() {
       return isTargetPageLoaded(productpage);
    }


    public String pageName(){
        return getText(productpage);
    }
    public void clickView(){
        click(viewButton);
    }
    public void clickFilter(){
        click(filterButton);
    }
    public String getFirstProductName() {
        return getText(By.xpath("(//android.widget.TextView[@content-desc='test-Item title'])[1]"));
    }

    public List<String> getProductNames() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//android.widget.TextView[@content-desc='test-Item title']")
        );

        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
    // Get product prices
    public List<Double> getProductPrices() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//android.widget.TextView[@content-desc='test-Price']")
        );

        return elements.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }
    public boolean isSortPopupDisplayed() {
        return isDisplayed(By.xpath("//android.widget.TextView[@text='Sort Items']"));
    }



    public void selectFilterOption(String option) {
        click(By.xpath("//android.widget.TextView[@text='" + option + "']"));
    }
    public void clickCancel() {
        click(By.xpath("//android.widget.TextView[@text='Cancel']"));
    }
    public boolean isListView() {
        return driver.findElements(By.xpath("//android.widget.ImageView")).size() > 0;
    }
}

