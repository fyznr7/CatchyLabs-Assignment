package steps.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runners.Hooks;

import java.util.Map;
import java.util.function.BiConsumer;

public class MainSteps {
    private static final Logger logger = LoggerFactory.getLogger(MainSteps.class);

    private static Map<String, Map<String, By>> locators;

    public static void initializeLocators(Map<String, Map<String, By>> pageLocators) {
        locators = pageLocators;
        logger.info("Locators initialized.");
    }

    public static BiConsumer<String, String> navigateToUrl = (browser, url) -> {
        WebDriver driver = Hooks.driver; // Access WebDriver from Hooks
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Please check Hooks class.");
        }
        driver.get(url);
        logger.info("Navigated to URL: {}", url);
    };

    public static BiConsumer<WebDriver, String[]> clickElement = (driver, params) -> {
        By locator = getLocator(params[0], params[1]);
        WebElement element = driver.findElement(locator);
        element.click();
        logger.info("Clicked element: {} on page: {}", params[0], params[1]);
    };

    public static TriConsumer<WebDriver, String[], String> sendKeysToElement = (driver, params, keys) -> {
        By locator = getLocator(params[0], params[1]);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(keys);
        logger.info("Sent keys to element: {} on page: {} with value: {}", params[0], params[1], keys);
    };

    public static TriConsumer<WebDriver, String[], String> selectDropdownByText = (driver, params, visibleText) -> {
        By locator = getLocator(params[0], params[1]);
        Select dropdown = new Select(driver.findElement(locator));
        dropdown.selectByVisibleText(visibleText);
        logger.info("Selected dropdown option: {} from element: {} on page: {}", visibleText, params[0], params[1]);
    };

    public static BiConsumer<WebDriver, String[]> verifyElementIsDisplayed = (driver, params) -> {
        By locator = getLocator(params[0], params[1]);
        WebElement element = driver.findElement(locator);
        if (element.isDisplayed()) {
            logger.info("Element is displayed: {} on page: {}", params[0], params[1]);
        } else {
            logger.error("Element is not displayed: {} on page: {}", params[0], params[1]);
            throw new AssertionError("Element is not displayed: " + params[0]);
        }
    };

    private static By getLocator(String key, String pageName) {
        if (locators == null || !locators.containsKey(pageName)) {
            throw new IllegalArgumentException("Locators not initialized or page not found: " + pageName);
        }
        Map<String, By> pageLocators = locators.get(pageName);
        if (!pageLocators.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key + " in page: " + pageName);
        }
        return pageLocators.get(key);
    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
