package stepdefinitions.main;

import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.main.MainSteps;

public class MainStepDefinitions {

    private static final Logger logger = LogManager.getLogger(MainStepDefinitions.class);

    private final WebDriver driver;

    public MainStepDefinitions() {
        this.driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver instance is null. Ensure Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver instance is null. Check Hooks class setup.");
        }
        logger.info("WebDriver instance successfully initialized.");
    }

    @Given("Navigate to {string}")
    public void navigate_to(String url) {
        logger.info("Navigating to URL: {}", url);
        try {
            driver.get(url);
            logger.info("Successfully navigated to URL: {}", url);
        } catch (Exception e) {
            logger.error("Error occurred while navigating to URL: {}", url, e);
            throw new AssertionError("Navigation to URL failed: " + url, e);
        }
    }

    @When("Click on {string} in page {string}")
    public void click_on_in_page(String key, String pageName) {
        logger.info("Attempting to click on element '{}' in page '{}'.", key, pageName);
        try {
            MainSteps.clickElement.accept(driver, new String[]{key, pageName});
            logger.info("Successfully clicked on element '{}' in page '{}'.", key, pageName);
        } catch (Exception e) {
            logger.error("Failed to click on element '{}' in page '{}'.", key, pageName, e);
            throw new AssertionError("Failed to click on element: " + key + " in page: " + pageName, e);
        }
    }

    @And("Send keys {string} to {string} in page {string}")
    public void send_keys_to_in_page(String keys, String key, String pageName) {
        logger.info("Attempting to send keys '{}' to element '{}' in page '{}'.", keys, key, pageName);
        try {
            MainSteps.sendKeysToElement.accept(driver, new String[]{key, pageName}, keys);
            logger.info("Successfully sent keys '{}' to element '{}' in page '{}'.", keys, key, pageName);
        } catch (Exception e) {
            logger.error("Failed to send keys '{}' to element '{}' in page '{}'.", keys, key, pageName, e);
            throw new AssertionError("Failed to send keys to element: " + key + " in page: " + pageName, e);
        }
    }

    @And("Select {string} from dropdown {string} in page {string}")
    public void select_from_dropdown_in_page(String visibleText, String key, String pageName) {
        logger.info("Attempting to select '{}' from dropdown '{}' in page '{}'.", visibleText, key, pageName);
        try {
            MainSteps.selectDropdownByText.accept(driver, new String[]{key, pageName}, visibleText);
            logger.info("Successfully selected '{}' from dropdown '{}' in page '{}'.", visibleText, key, pageName);
        } catch (Exception e) {
            logger.error("Failed to select '{}' from dropdown '{}' in page '{}'.", visibleText, key, pageName, e);
            throw new AssertionError("Failed to select dropdown option: " + visibleText + " from element: " + key + " in page: " + pageName, e);
        }
    }

    @Then("Verify {string} is displayed in page {string}")
    public void verify_is_displayed_in_page(String key, String pageName) {
        logger.info("Verifying that element '{}' is displayed in page '{}'.", key, pageName);
        try {
            MainSteps.verifyElementIsDisplayed.accept(driver, new String[]{key, pageName});
            logger.info("Element '{}' is displayed in page '{}'.", key, pageName);
        } catch (Exception e) {
            logger.error("Failed to verify element '{}' in page '{}'.", key, pageName, e);
            throw new AssertionError("Element is not displayed: " + key + " in page: " + pageName, e);
        }
    }
}
