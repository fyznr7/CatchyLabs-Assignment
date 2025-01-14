package steps.money;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.money.TransferMoneyPage;
import runners.Hooks;
import steps.main.MainSteps;

public class TransferMoneySteps extends MainSteps {

    private static final Logger logger = LoggerFactory.getLogger(TransferMoneySteps.class);

    public TransferMoneySteps() {
        super(Hooks.getDriver());
        if (this.driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class.");
        }
        logger.info("TransferMoneySteps initialized with WebDriver from Hooks.");
    }

    public void performTransferMoney(String amount, String accounts) {
        logger.info("Starting transfer money process with amount: '{}' and accounts: '{}'", amount, accounts);

        String[] accountDetails = validateAndSplitAccounts(accounts);
        String senderAccount = accountDetails[0];
        String receiverAccount = accountDetails[1];

        double initialTotal = getTotalAmount();
        String transferAmount = resolveTransferAmount(amount, initialTotal);

        executeTransfer(senderAccount, receiverAccount, transferAmount);
        validateTransaction(initialTotal, Double.parseDouble(transferAmount));
        logger.info("Transfer money process completed successfully.");
    }

    private String[] validateAndSplitAccounts(String accounts) {
        logger.info("Validating and splitting accounts: '{}'", accounts);
        String[] accountDetails = accounts.split("->");
        if (accountDetails.length != 2) {
            logger.error("Invalid account format. Expected 'sender->receiver'. Provided: {}", accounts);
            throw new IllegalArgumentException("Invalid account format. Expected 'sender->receiver'. Provided: " + accounts);
        }
        logger.info("Accounts split successfully into sender: '{}' and receiver: '{}'", accountDetails[0], accountDetails[1]);
        return accountDetails;
    }

    private String resolveTransferAmount(String amount, double initialTotal) {
        logger.info("Resolving transfer amount with input '{}', Initial total '{}'", amount, initialTotal);
        return amount.equalsIgnoreCase("balance") ? String.valueOf(initialTotal) : amount;
    }

    private void executeTransfer(String senderAccount, String receiverAccount, String transferAmount) {
        logger.info("Executing transfer steps for sender '{}', receiver '{}', amount '{}'", senderAccount, receiverAccount, transferAmount);
        clickTransferMoneyButton();
        selectSenderAccount(senderAccount);
        selectReceiverAccount(receiverAccount);
        enterAmount(transferAmount);
        submitTransfer();
    }

    private void clickTransferMoneyButton() {
        logger.info("Clicking on 'Transfer Money' button.");
        clickElement(TransferMoneyPage.TRANSFER_MONEY_BUTTON);
    }

    private void selectSenderAccount(String senderAccount) {
        logger.info("Selecting sender account: '{}'", senderAccount);
        selectDropdownByText(TransferMoneyPage.SENDER_ACCOUNT_DROPDOWN, senderAccount);
    }

    private void selectReceiverAccount(String receiverAccount) {
        logger.info("Selecting receiver account: '{}'", receiverAccount);
        selectDropdownByText(TransferMoneyPage.RECEIVER_ACCOUNT_DROPDOWN, receiverAccount);
    }

    private void enterAmount(String amount) {
        logger.info("Entering transfer amount: '{}'", amount);
        sendKeysToElement(TransferMoneyPage.AMOUNT_FIELD, amount);
    }

    private void submitTransfer() {
        logger.info("Submitting the transfer.");
        clickElement(TransferMoneyPage.SEND_BUTTON);
    }

    private void validateTransaction(double initialTotal, double transferredAmount) {
        validateTransferredAmount(transferredAmount);
        validateTotalAmount(initialTotal, transferredAmount);
    }

    private void validateTransferredAmount(double transferredAmount) {
        String expectedAmount = String.valueOf(transferredAmount);
        String actualAmount = getTextFromElement(TransferMoneyPage.TRANSFERRED_MONEY_AMOUNT_TEXT);
        if (!actualAmount.equals(expectedAmount)) {
            logger.error("Transferred amount mismatch! Expected: '{}', but got: '{}'", expectedAmount, actualAmount);
            throw new AssertionError("Transferred amount mismatch! Expected: " + expectedAmount + ", but got: " + actualAmount);
        }
        logger.info("Transferred amount validated successfully. Actual: '{}'", actualAmount);
    }

    private void validateTotalAmount(double initialTotal, double transferredAmount) {
        double finalTotal = getTotalAmount();
        double expectedTotal = initialTotal - transferredAmount;
        if (Double.compare(expectedTotal, finalTotal) != 0) {
            logger.error("Total amount mismatch after transfer! Expected: '{}', but got: '{}'", expectedTotal, finalTotal);
            throw new AssertionError("Total amount mismatch after transfer! Expected: " + expectedTotal + ", but got: " + finalTotal);
        }
        logger.info("Total amount validated successfully. Final total: '{}'", finalTotal);
    }

    private double getTotalAmount() {
        logger.info("Retrieving total amount...");
        String totalText = getTextFromElement(TransferMoneyPage.TOTAL_AMOUNT);
        double totalAmount = Double.parseDouble(totalText);
        logger.info("Total amount retrieved: '{}'", totalAmount);
        return totalAmount;
    }
}