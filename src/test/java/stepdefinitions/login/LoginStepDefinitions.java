package stepdefinitions.login;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.login.LoginSteps;

public class LoginStepDefinitions {

    private final LoginSteps loginSteps;
    private final WebDriver driver;

    public LoginStepDefinitions() {
        this.driver = Hooks.driver;
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class setup.");
        }
        this.loginSteps = new LoginSteps(driver);
    }

    @Given("Login process with user {string}")
    public void performLoginForUser(String userKey) {
        Scenario scenario = Hooks.currentScenario;
        Hooks.captureScreenshot("BeforeLogin", scenario);
        loginSteps.performLogin(userKey);
        Hooks.captureScreenshot("AfterLogin", scenario);
    }
}
