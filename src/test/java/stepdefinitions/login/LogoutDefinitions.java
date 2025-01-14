package stepdefinitions.login;

import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.login.LogoutSteps;

public class LogoutDefinitions {

    private static final Logger logger = LogManager.getLogger(LogoutDefinitions.class);

    private final LogoutSteps logoutSteps;

    public LogoutDefinitions() {
        WebDriver driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
        this.logoutSteps = new LogoutSteps();
        logger.info("LogoutSteps instance successfully created.");
    }


}
