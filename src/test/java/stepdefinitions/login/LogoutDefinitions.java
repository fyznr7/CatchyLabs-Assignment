package stepdefinitions.login;

import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.login.LogoutSteps;

public class LogoutDefinitions {

    private static final Logger logger = LogManager.getLogger(LogoutDefinitions.class);

    private final WebDriver driver;

    public LogoutDefinitions() {
        this.driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
    }

    @Then("User logs out")
    public void user_logs_out() {
        logger.info("Starting logout process.");
        try {
            LogoutSteps.performLogout();
            logger.info("Logout process completed successfully.");
        } catch (Exception e) {
            logger.error("An error occurred during the logout process.", e);
            throw e;
        }
    }
}