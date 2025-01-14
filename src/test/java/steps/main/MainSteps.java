package steps.main;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.main.PageLoader;

import java.time.Duration;
import java.util.Map;

public class MainSteps {

    private static final Logger logger = LoggerFactory.getLogger(MainSteps.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static Map<String, Map<String, By>> locators;
    protected final WebDriver driver;

    public MainSteps(WebDriver driver) {
        if (driver == null) {
            logger.error("WebDriver is not initialized.");
            throw new IllegalStateException("WebDriver is not initialized.");
        }
        this.driver = driver;
        logger.info("MainSteps initialized with WebDriver.");
    }

    public By getLocator(String pageName, String key) {
        logger.info("Retrieving locator for key '{}' on page '{}'.", key, pageName);
        try {
            By locator = PageLoader.getLocator(pageName, key);
            WebElement element = waitForElementToBeVisible(locator);
            scrollToElement(element);
            return locator;
        } catch (IllegalArgumentException e) {
            logger.error("Locator not found for key: {} on page: {}", key, pageName, e);
            throw e;
        }
    }

    public void goToUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        try {
            driver.get(url);
            waitForPageToLoad();
            logger.info("Successfully navigated to URL: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", url, e);
            throw new RuntimeException("Error navigating to URL: " + url, e);
        }
    }

    public void clickElement(By locator) {
        logger.info("Entering method: clickElement with locator: {}", locator);
        try {
            WebElement element = waitForElementToBeClickable(locator);
            element.click();
            logger.info("Clicked element located by: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element located by: {}", locator, e);
            throw new RuntimeException("Error clicking element located by: " + locator, e);
        }
    }

    public void jsClickElement(By locator) {
        logger.info("Entering method: jsClickElement with locator: {}", locator);
        try {
            WebElement element = waitForElementToBeVisible(locator);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].click();", element);
            logger.info("Performed JavaScript click on element located by: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to perform JavaScript click on element located by: {}", locator, e);
            throw new RuntimeException("Error performing JavaScript click on element located by: " + locator, e);
        }
    }

    private void scrollToElement(WebElement element) {
        logger.info("Scrolling to element located by: {}", element);
        try {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Successfully scrolled to the element.");
        } catch (Exception e) {
            logger.error("Failed to scroll to element located by: {}", element, e);
            throw new RuntimeException("Error scrolling to element.", e);
        }
    }

    public void scrollToElement(By locator) {
        logger.info("Entering method: scrollToElement with locator: {}", locator);
        try {
            WebElement element = waitForElementToBeVisible(locator);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element located by: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element located by: {}", locator, e);
            throw new RuntimeException("Error scrolling to element located by: " + locator, e);
        }
    }

    public String getTextFromElement(By locator) {
        logger.info("Retrieving text from element located by: {}", locator);
        WebElement element = waitForElementToBeVisible(locator);
        String text = element.getText();
        logger.info("Text retrieved from element: {}", text);
        return text;
    }

    public void sendKeysToElement(By locator, String text) {
        logger.info("Entering method: sendKeysToElement with locator: {} and text: '{}'", locator, text);
        try {
            WebElement element = waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Sent keys '{}' to element located by: {}", text, locator);
        } catch (Exception e) {
            logger.error("Failed to send keys '{}' to element located by: {}", text, locator, e);
            throw new RuntimeException("Error sending keys to element located by: " + locator, e);
        }
    }

    public void waitForPageToLoad() {
        logger.info("Entering method: waitForPageToLoad");
        try {
            new WebDriverWait(driver, TIMEOUT)
                    .until(webDriver -> "complete".equals(
                            ((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
            logger.info("Page loaded successfully.");
        } catch (Exception e) {
            logger.error("Failed to wait for page load.", e);
            throw new RuntimeException("Error waiting for page to load.", e);
        }
    }

    private WebElement waitForElementToBeVisible(By locator) {
        logger.info("Waiting for element to be visible located by: {}", locator);
        return new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForElementToBeClickable(By locator) {
        logger.info("Waiting for element to be clickable located by: {}", locator);
        return new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void selectDropdownByText(By locator, String visibleText) {
        logger.info("Selecting '{}' from dropdown located by: {}", visibleText, locator);
        try {
            WebElement dropdownElement = waitForElementToBeVisible(locator);
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByVisibleText(visibleText);
            logger.info("Successfully selected '{}' from dropdown.", visibleText);
        } catch (Exception e) {
            logger.error("Failed to select '{}' from dropdown located by: {}", visibleText, locator, e);
            throw new RuntimeException("Error selecting dropdown option.", e);
        }
    }

    public void verifyElementIsDisplayed(By locator) {
        logger.info("Verifying element located by: {}", locator);
        try {
            WebElement element = waitForElementToBeVisible(locator);
            if (!element.isDisplayed()) {
                throw new AssertionError("Element located by " + locator + " is not displayed.");
            }
            logger.info("Element located by: {} is displayed.", locator);
        } catch (Exception e) {
            logger.error("Failed to verify element located by: {}", locator, e);
            throw new RuntimeException("Error verifying element visibility.", e);
        }
    }

}
