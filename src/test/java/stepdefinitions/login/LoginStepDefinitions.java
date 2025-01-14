package stepdefinitions.login;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.login.LoginSteps;

public class LoginStepDefinitions {

    private static final Logger logger = LogManager.getLogger(LoginStepDefinitions.class);

    private final LoginSteps loginSteps;
    private final WebDriver driver;

    public LoginStepDefinitions() {
        this.driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
        this.loginSteps = new LoginSteps();
    }

    @Given("Login process with user {string}")
    public void performLoginForUser(String userKey) {
        logger.info("Starting login process for user: {}", userKey);
        try {
            loginSteps.performLogin(userKey);
            logger.info("Login process completed successfully for user: {}", userKey);
        } catch (Exception e) {
            logger.error("An error occurred during the login process for user: {}", userKey, e);
            throw e;
        }
    }

    @Then("User logs out")
    public void user_logs_out() {
        logger.info("Starting logout process.");
        try {
            loginSteps.performLogout();
            logger.info("Logout process completed successfully.");
        } catch (Exception e) {
            logger.error("An error occurred during the logout process.", e);
            throw new AssertionError("Logout process failed.", e);
        }
    }
}
