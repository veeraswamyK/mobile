package pages;

import core.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BasePage {

    protected AppiumDriver driver = DriverManager.getDriver();

    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    protected void click(By locator) {
        find(locator).click();
    }

    protected void type(By locator, String text) {
        find(locator).sendKeys(text);
    }

    protected String getText(By locator) {
        return find(locator).getText();
    }

    protected static boolean isTargetPageLoaded(By locator) {
        try {
            return DriverManager.getDriver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
        }
      protected boolean isDisplayed(By locator){
          return DriverManager.getDriver().findElement(locator).isDisplayed();
      }
    }
