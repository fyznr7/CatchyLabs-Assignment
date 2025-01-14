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
import java.util.function.Consumer;

public class MainSteps {

    private static final Logger logger = LoggerFactory.getLogger(MainSteps.class);

    private static Map<String, Map<String, By>> locators;

    public static void initializeLocators(Map<String, Map<String, By>> pageLocators) {
        locators = pageLocators;
        logger.info("Locators initialized.");
    }

    public static BiConsumer<WebDriver, String[]> clickElement = (driver, params) -> {
        try {
            By locator = getLocator(params[0], params[1]);
            WebElement element = driver.findElement(locator);
            element.click();
            logger.info("Clicked element '{}' on page '{}'.", params[0], params[1]);
        } catch (Exception e) {
            logger.error("Failed to click element '{}' on page '{}'.", params[0], params[1], e);
            throw e;
        }
    };

    public static TriConsumer<WebDriver, String[], String> sendKeysToElement = (driver, params, keys) -> {
        try {
            By locator = getLocator(params[0], params[1]);
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(keys);
            logger.info("Sent keys '{}' to element '{}' on page '{}'.", keys, params[0], params[1]);
        } catch (Exception e) {
            logger.error("Failed to send keys '{}' to element '{}' on page '{}'.", keys, params[0], params[1], e);
            throw e;
        }
    };

    public static TriConsumer<WebDriver, String[], String> selectDropdownByText = (driver, params, visibleText) -> {
        try {
            By locator = getLocator(params[0], params[1]);
            Select dropdown = new Select(driver.findElement(locator));
            dropdown.selectByVisibleText(visibleText);
            logger.info("Selected '{}' from dropdown '{}' on page '{}'.", visibleText, params[0], params[1]);
        } catch (Exception e) {
            logger.error("Failed to select '{}' from dropdown '{}' on page '{}'.", visibleText, params[0], params[1], e);
            throw e;
        }
    };

    public static BiConsumer<WebDriver, String[]> verifyElementIsDisplayed = (driver, params) -> {
        try {
            By locator = getLocator(params[0], params[1]);
            WebElement element = driver.findElement(locator);
            if (element.isDisplayed()) {
                logger.info("Element '{}' is displayed on page '{}'.", params[0], params[1]);
            } else {
                throw new AssertionError("Element '" + params[0] + "' is not displayed on page '" + params[1] + "'.");
            }
        } catch (Exception e) {
            logger.error("Failed to verify element '{}' on page '{}'.", params[0], params[1], e);
            throw e;
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
