package steps.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.login.LogoutPage;
import runners.Hooks;
import steps.main.MainSteps;

public class LogoutSteps extends MainSteps {

    private static final Logger logger = LoggerFactory.getLogger(LogoutSteps.class);

    public LogoutSteps() {
        super(Hooks.getDriver());
        if (this.driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
        logger.info("LogoutSteps initialized with WebDriver from Hooks.");
    }


}
