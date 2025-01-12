package stepdefinitions.money;

import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.money.AddMoneySteps;

public class AddMoneyDefinitions {

    private AddMoneySteps addMoneySteps;

    public AddMoneyDefinitions() {
        WebDriver driver = Hooks.driver;
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class setup.");
        }
        this.addMoneySteps = new AddMoneySteps(driver);
    }

    @And("User adds money using card {string} with amount {string}")
    public void user_adds_money_using_card_with_amount(String cardKey, String amount) {
        addMoneySteps.performAddMoney(cardKey, amount);
    }
}
