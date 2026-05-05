package pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class NavBarPage extends BasePage {

    private static final By MENU_ICON    = AppiumBy.accessibilityId("test-Menu");
    private static final By LOGOUT_BTN   = AppiumBy.accessibilityId("test-LOGOUT");
    private static final By RESET_BTN    = AppiumBy.accessibilityId("test-RESET APP STATE");
    private static final By CLOSE_ICON   = AppiumBy.accessibilityId("test-Close");
    private static final By QR_SCANNER   = By.xpath("//android.widget.TextView[@text='QR CODE SCANNER']");
    private static final By LOGIN_BTN    = By.xpath("//android.widget.TextView[@text='LOGIN']");
    private static final By NAV_OPTIONS  = By.xpath("//android.widget.TextView[@content-desc]");
    private static final By SCAN_TEXT    = By.xpath("//android.widget.TextView[contains(@text,'Scan')]");

    @Step("Open navigation menu")
    public void openMenu() {
        click(MENU_ICON);
    }

    @Step("Click Logout")
    public void clickLogout() {
        click(LOGOUT_BTN);
    }

    @Step("Click Reset App State")
    public void clickResetAppState() {
        click(RESET_BTN);
    }

    @Step("Close navigation menu")
    public void closeMenu() {
        click(CLOSE_ICON);
    }

    @Step("Click QR Code Scanner")
    public void clickQrScanner() {
        click(QR_SCANNER);
    }

    @Step("Check navigation menu options are displayed")
    public boolean areNavOptionsDisplayed() {
        return !findAll(NAV_OPTIONS).isEmpty();
    }

    @Step("Check login page is displayed after logout")
    public boolean isLoginPageDisplayed() {
        return isVisible(LOGIN_BTN);
    }

    @Step("Check QR scanner is opened")
    public boolean isQrScannerOpened() {
        return !findAll(SCAN_TEXT).isEmpty();
    }
}
