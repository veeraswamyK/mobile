package pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;


public class NavBarPage extends BasePage{
    private By navbarIcon = AppiumBy.accessibilityId("test-Menu");
    private By logout = AppiumBy.accessibilityId("test-LOGOUT");
    private By Reset = AppiumBy.accessibilityId("test-RESET APP STATE");
    private By loginPage = By.xpath("//android.widget.TextView[@text='LOGIN']");
    private By qrScanner = By.xpath("//android.widget.TextView[@text='QR CODE SCANNER']");
    private By navOptions = By.xpath("//android.widget.TextView");
    private By closeIcon = AppiumBy.accessibilityId("test-Close");

    public void navBarIcon() {
        click(navbarIcon);
    }
    public void resetAppState(){
        click(Reset);
    }
    public void closeNavbar(){
        click(closeIcon);
    }

    public void logoutButton() {
        click(logout);
    }

    public void clickQrScanner() {
        click(qrScanner);
    }
    public boolean isNavBarOptionsDisplayed() {
        return driver.findElements(navOptions).size() > 0;
    }
    public boolean isLoginPageDisplayed() {
        return driver.findElements(loginPage).size() > 0;
    }
    public boolean isScannerOpened() {
        return driver.findElements(
                By.xpath("//android.widget.TextView[contains(@text,'Scan')]")
        ).size() > 0;
    }
}
