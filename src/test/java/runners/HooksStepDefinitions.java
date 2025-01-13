package runners;

import io.cucumber.java.en.Given;

public class HooksStepDefinitions {

    @Given("Set browser to {string}")
    public void setBrowser(String browser) {
        Hooks.updateBrowserConfig(browser);
    }
}
