package stepdefinitions.money;

import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import runners.Hooks;
import steps.money.TransferMoneySteps;

public class TransferMoneyDefinitions {

    private TransferMoneySteps transferMoneySteps;

    public TransferMoneyDefinitions() {
        WebDriver driver = Hooks.driver;
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class setup.");
        }
        this.transferMoneySteps = new TransferMoneySteps(driver);
    }

    @And("User transfers {string} from {string}")
    public void user_transfers_from(String amount, String accounts) {
        transferMoneySteps.performTransferMoney(amount, accounts);
    }
}
