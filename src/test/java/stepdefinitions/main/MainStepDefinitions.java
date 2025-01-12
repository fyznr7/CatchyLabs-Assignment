package stepdefinitions.main;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.main.MainSteps;

public class MainStepDefinitions {

    private final WebDriver driver;

    public MainStepDefinitions() {
        this.driver = Hooks.driver;
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class setup.");
        }
    }

    @Given("Navigate to {string}")
    public void navigate_to(String url) {
        MainSteps.navigateToUrl.accept("chrome", url);
    }


    @When("Click on {string} in page {string}")
    public void click_on_in_page(String key, String pageName) {
        try {
            MainSteps.clickElement.accept(driver, new String[]{key, pageName});
        } catch (Exception e) {
            throw new AssertionError("Failed to click on element: " + key + " in page: " + pageName, e);
        }
    }


    @And("Send keys {string} to {string} in page {string}")
    public void send_keys_to_in_page(String keys, String key, String pageName) {
        try {
            MainSteps.sendKeysToElement.accept(driver, new String[]{key, pageName}, keys);
        } catch (Exception e) {
            throw new AssertionError("Failed to send keys to element: " + key + " in page: " + pageName, e);
        }
    }


    @And("Select {string} from dropdown {string} in page {string}")
    public void select_from_dropdown_in_page(String visibleText, String key, String pageName) {
        try {
            MainSteps.selectDropdownByText.accept(driver, new String[]{key, pageName}, visibleText);
        } catch (Exception e) {
            throw new AssertionError("Failed to select dropdown option: " + visibleText
                    + " from element: " + key + " in page: " + pageName, e);
        }
    }

    @Then("Verify {string} is displayed in page {string}")
    public void verify_is_displayed_in_page(String key, String pageName) {
        try {
            MainSteps.verifyElementIsDisplayed.accept(driver, new String[]{key, pageName});
        } catch (Exception e) {
            throw new AssertionError("Element is not displayed: " + key + " in page: " + pageName, e);
        }
    }
}
