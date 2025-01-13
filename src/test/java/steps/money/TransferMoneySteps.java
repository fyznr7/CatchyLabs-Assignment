package steps.money;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public TransferMoneySteps(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void performTransferMoney(String amount, String accounts) {
        String[] accountDetails = accounts.split("->");
        String senderAccount = accountDetails[0];
        String receiverAccount = accountDetails[1];

        double initialTotal = getTotalAmount();

        logger.info("Starting transfer of {} from {} to {}", amount, senderAccount, receiverAccount);

        List<Runnable> steps = List.of(
                this::clickTransferMoneyButton,
                () -> selectSenderAccount(senderAccount),
                () -> selectReceiverAccount(receiverAccount),
                () -> enterAmount(amount),
                this::clickSendButton
        );

        steps.forEach(Runnable::run);

        logger.info("Successfully transferred {} from {} to {}", amount, senderAccount, receiverAccount);

        assertTransferredAmount(amount);
        assertTotalAmount(initialTotal, Double.parseDouble(amount));
    }

    public void clickTransferMoneyButton() {
        logger.debug("Clicking Transfer Money button");
        WebElement transferMoneyButton = wait.until(ExpectedConditions.elementToBeClickable(TRANSFER_MONEY_BUTTON));
        transferMoneyButton.click();
    }

    public void selectSenderAccount(String senderAccount) {
        logger.debug("Selecting sender account: {}", senderAccount);
        WebElement senderDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SENDER_ACCOUNT_DROPDOWN));
        new Select(senderDropdown).selectByVisibleText(senderAccount);
    }

    public void selectReceiverAccount(String receiverAccount) {
        logger.debug("Selecting receiver account: {}", receiverAccount);
        WebElement receiverDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(RECEIVER_ACCOUNT_DROPDOWN));
        new Select(receiverDropdown).selectByVisibleText(receiverAccount);
    }

    public void enterAmount(String amount) {
        logger.debug("Entering transfer amount: {}", amount);
        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(AMOUNT_FIELD));
        amountField.clear();
        amountField.sendKeys(amount);
    }

    public void clickSendButton() {
        logger.debug("Clicking Send button");
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
    }

    private void assertTransferredAmount(String expectedAmount) {
        logger.debug("Asserting transferred amount: {}", expectedAmount);
        WebElement transferredAmountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TRANSFERRED_MONEY_AMOUNT_TEXT));
        String actualAmount = transferredAmountElement.getText();
        if (!actualAmount.equals(expectedAmount)) {
            throw new AssertionError("Transferred amount mismatch! Expected: " + expectedAmount + ", but got: " + actualAmount);
        }
        logger.info("Transferred amount assertion passed. Expected: {}, Actual: {}", expectedAmount, actualAmount);
    }

    private void assertTotalAmount(double initialTotal, double transferredAmount) {
        double finalTotal = getTotalAmount();
        double expectedTotal = initialTotal - transferredAmount;
        if (Double.compare(expectedTotal, finalTotal) != 0) {
            throw new AssertionError("Total amount mismatch after transfer! Expected: " + expectedTotal + ", but got: " + finalTotal);
        }
        logger.info("Total amount assertion passed. Expected: {}, Actual: {}", expectedTotal, finalTotal);
    }

    private double getTotalAmount() {
        String totalText = wait.until(ExpectedConditions.visibilityOfElementLocated(TOTAL_AMOUNT)).getText();
        logger.debug("Retrieved total amount text: {}", totalText);
        return Double.parseDouble(totalText);
    }
}
