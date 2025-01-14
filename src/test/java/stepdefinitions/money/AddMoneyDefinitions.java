package stepdefinitions.money;

import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.money.AddMoneySteps;

public class AddMoneyDefinitions {

    private static final Logger logger = LogManager.getLogger(AddMoneyDefinitions.class);

    private final AddMoneySteps addMoneySteps;

    public AddMoneyDefinitions() {
        WebDriver driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
        this.addMoneySteps = new AddMoneySteps();
        logger.info("AddMoneySteps instance successfully created.");
    }

    @And("User adds money using card {string} with amount {string}")
    public void user_adds_money_using_card_with_amount(String cardKey, String amount) {
        logger.info("Starting add money process using card '{}' with amount '{}'.", cardKey, amount);
        try {
            addMoneySteps.performAddMoney(cardKey, amount);
            logger.info("Successfully added money using card '{}' with amount '{}'.", cardKey, amount);
        } catch (Exception e) {
            logger.error("Failed to add money using card '{}' with amount '{}'.", cardKey, amount, e);
            throw new AssertionError("Add money process failed for card: " + cardKey + " with amount: " + amount, e);
        }
    }
}