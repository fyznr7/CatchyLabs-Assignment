package stepdefinitions.login;

import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.login.LogoutSteps;

public class LogoutDefinitions {

    private final WebDriver driver;

    public LogoutDefinitions() {
        this.driver = Hooks.driver;
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class setup.");
        }
    }

    @Then("User logs out")
    public void user_logs_out() {
        LogoutSteps.performLogout(driver);
    }
}
