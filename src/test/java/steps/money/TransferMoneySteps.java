package steps.money;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runners.Hooks;

import java.time.Duration;
import java.util.List;

public class TransferMoneySteps {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(TransferMoneySteps.class);

    private static final By TRANSFER_MONEY_BUTTON = By.xpath("//div[text()='Transfer money']/parent::div");
    private static final By SENDER_ACCOUNT_DROPDOWN = By.xpath("(//div[@class='css-175oi2r r-1777fci']//select)[1]");
    private static final By RECEIVER_ACCOUNT_DROPDOWN = By.xpath("(//div[@class='css-175oi2r r-1777fci']//select)[2]");
    private static final By AMOUNT_FIELD = By.xpath("//input[contains(@inputmode,'numeric')]");
    private static final By SEND_BUTTON = By.xpath("//div[text()='Send']//parent::div");
    private static final By TRANSFERRED_MONEY_AMOUNT_TEXT = By.xpath("(//div[text()='Amount:']/following-sibling::div//div)[1]");
    private static final By TOTAL_AMOUNT = By.xpath("//div[text()='Amount']/following-sibling::div/div");

    public TransferMoneySteps() {
        this.driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class.");
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.info("TransferMoneySteps initialized with WebDriver from Hooks.");
    }

    public void performTransferMoney(String amount, String accounts) {
        logger.info("Starting transfer money process with amount: {} and accounts: {}", amount, accounts);

        String[] accountDetails = accounts.split("->");
        if (accountDetails.length != 2) {
            throw new IllegalArgumentException("Invalid account format. Expected 'sender->receiver'. Provided: " + accounts);
        }

        String senderAccount = accountDetails[0];
        String receiverAccount = accountDetails[1];

        double initialTotal = getTotalAmount();
        String transferAmount = amount.equalsIgnoreCase("balance") ? String.valueOf(initialTotal) : amount;

        logger.info("Calculated transfer amount: {}. Sender: {}, Receiver: {}", transferAmount, senderAccount, receiverAccount);

        List<Runnable> steps = List.of(
                this::clickTransferMoneyButton,
                () -> selectSenderAccount(senderAccount),
                () -> selectReceiverAccount(receiverAccount),
                () -> enterAmount(transferAmount),
                this::clickSendButton
        );

        steps.forEach(step -> {
            try {
                step.run();
            } catch (Exception e) {
                logger.error("Step execution failed during transfer money process.", e);
                throw e;
            }
        });

        logger.info("Transfer process completed. Verifying transferred amount and final total...");

        assertTransferredAmount(transferAmount);
        assertTotalAmount(initialTotal, Double.parseDouble(transferAmount));

        logger.info("Transfer process successfully verified.");
    }

    private void clickTransferMoneyButton() {
        logger.info("Clicking 'Transfer Money' button...");
        WebElement transferMoneyButton = wait.until(ExpectedConditions.elementToBeClickable(TRANSFER_MONEY_BUTTON));
        transferMoneyButton.click();
        logger.info("'Transfer Money' button clicked successfully.");
    }

    private void selectSenderAccount(String senderAccount) {
        logger.info("Selecting sender account: {}", senderAccount);
        WebElement senderDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SENDER_ACCOUNT_DROPDOWN));
        new Select(senderDropdown).selectByVisibleText(senderAccount);
        logger.info("Sender account '{}' selected successfully.", senderAccount);
    }

    private void selectReceiverAccount(String receiverAccount) {
        logger.info("Selecting receiver account: {}", receiverAccount);
        WebElement receiverDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(RECEIVER_ACCOUNT_DROPDOWN));
        new Select(receiverDropdown).selectByVisibleText(receiverAccount);
        logger.info("Receiver account '{}' selected successfully.", receiverAccount);
    }

    private void enterAmount(String amount) {
        logger.info("Entering transfer amount: {}", amount);
        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(AMOUNT_FIELD));
        amountField.clear();
        amountField.sendKeys(amount);
        logger.info("Transfer amount '{}' entered successfully.", amount);
    }

    private void clickSendButton() {
        logger.info("Clicking 'Send' button...");
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
        logger.info("'Send' button clicked successfully.");
    }

    private void assertTransferredAmount(String expectedAmount) {
        logger.info("Asserting transferred amount: {}", expectedAmount);
        WebElement transferredAmountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TRANSFERRED_MONEY_AMOUNT_TEXT));
        String actualAmount = transferredAmountElement.getText();
        if (!actualAmount.equals(expectedAmount)) {
            logger.error("Transferred amount mismatch! Expected: {}, but got: {}", expectedAmount, actualAmount);
            throw new AssertionError("Transferred amount mismatch! Expected: " + expectedAmount + ", but got: " + actualAmount);
        }
        logger.info("Transferred amount assertion passed. Expected: {}, Actual: {}", expectedAmount, actualAmount);
    }

    private void assertTotalAmount(double initialTotal, double transferredAmount) {
        logger.info("Asserting total amount after transfer...");
        double finalTotal = getTotalAmount();
        double expectedTotal = initialTotal - transferredAmount;
        if (Double.compare(expectedTotal, finalTotal) != 0) {
            logger.error("Total amount mismatch after transfer! Expected: {}, but got: {}", expectedTotal, finalTotal);
            throw new AssertionError("Total amount mismatch after transfer! Expected: " + expectedTotal + ", but got: " + finalTotal);
        }
        logger.info("Total amount assertion passed. Expected: {}, Actual: {}", expectedTotal, finalTotal);
    }

    private double getTotalAmount() {
        logger.info("Retrieving total amount...");
        String totalText = wait.until(ExpectedConditions.visibilityOfElementLocated(TOTAL_AMOUNT)).getText();
        logger.debug("Retrieved total amount text: {}", totalText);
        double totalAmount = Double.parseDouble(totalText);
        if (totalAmount < 0) {
            logger.error("Total amount is negative: {}", totalAmount);
            throw new AssertionError("Total amount is negative: " + totalAmount);
        }
        logger.info("Total amount retrieved successfully: {}", totalAmount);
        return totalAmount;
    }
}
