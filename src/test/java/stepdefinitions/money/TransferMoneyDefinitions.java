package stepdefinitions.money;

import io.cucumber.java.en.And;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.money.TransferMoneySteps;

public class TransferMoneyDefinitions {

    private static final Logger logger = LogManager.getLogger(TransferMoneyDefinitions.class);

    private final TransferMoneySteps transferMoneySteps;

    public TransferMoneyDefinitions() {
        WebDriver driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
        this.transferMoneySteps = new TransferMoneySteps();
        logger.info("TransferMoneySteps instance successfully created.");
    }

    @And("User transfers {string} from {string}")
    public void user_transfers_from(String amount, String accounts) {
        logger.info("Starting money transfer process. Amount: {}, Accounts: {}", amount, accounts);
        try {
            transferMoneySteps.performTransferMoney(amount, accounts);
            logger.info("Successfully transferred {} from accounts: {}.", amount, accounts);
        } catch (Exception e) {
            logger.error("Failed to transfer {} from accounts: {}.", amount, accounts, e);
            throw new AssertionError("Money transfer process failed. Amount: " + amount + ", Accounts: " + accounts, e);
        }
    }
}