package stepdefinitions.login;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.login.LoginSteps;

public class LoginStepDefinitions {

    private LoginSteps loginSteps;

    public LoginStepDefinitions() {
        WebDriver driver = Hooks.driver;
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class setup.");
        }
        this.loginSteps = new LoginSteps(driver);
    }

    @Given("Login process with user {string}")
    public void performLoginForUser(String userKey) {
        loginSteps.performLogin(userKey);
    }
}
