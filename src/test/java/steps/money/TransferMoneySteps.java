package steps.money;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferMoneySteps {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final By TRANSFER_MONEY_BUTTON = By.xpath("//div[text()='Transfer money']/parent::div");
    private static final By SENDER_ACCOUNT_DROPDOWN = By.xpath("(//div[@class='css-175oi2r r-1777fci']//select)[1]");
    private static final By RECEIVER_ACCOUNT_DROPDOWN = By.xpath("(//div[@class='css-175oi2r r-1777fci']//select)[2]");
    private static final By AMOUNT_FIELD = By.xpath("//input[contains(@inputmode,'numeric')]");
    private static final By SEND_BUTTON = By.xpath("//div[text()='Send']//parent::div");
    private static final By TRANSFERRED_MONEY_AMOUNT_TEXT = By.xpath("(//div[text()='Amount:']/following-sibling::div//div)[1]");

    public TransferMoneySteps(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void performTransferMoney(String amount, String accounts) {
        String[] accountDetails = accounts.split("->");
        String senderAccount = accountDetails[0];
        String receiverAccount = accountDetails[1];

        clickTransferMoneyButton();
        selectSenderAccount(senderAccount);
        selectReceiverAccount(receiverAccount);
        enterAmount(amount);
        clickSendButton();

        System.out.println("Transferred " + amount + " from " + senderAccount + " to " + receiverAccount);
    }

    public void clickTransferMoneyButton() {
        WebElement transferMoneyButton = wait.until(ExpectedConditions.elementToBeClickable(TRANSFER_MONEY_BUTTON));
        transferMoneyButton.click();
    }

    public void selectSenderAccount(String senderAccount) {
        WebElement senderDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SENDER_ACCOUNT_DROPDOWN));
        new Select(senderDropdown).selectByVisibleText(senderAccount);
    }

    public void selectReceiverAccount(String receiverAccount) {
        WebElement receiverDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(RECEIVER_ACCOUNT_DROPDOWN));
        new Select(receiverDropdown).selectByVisibleText(receiverAccount);
    }

    public void enterAmount(String amount) {
        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(AMOUNT_FIELD));
        amountField.clear();
        amountField.sendKeys(amount);
    }

    public void clickSendButton() {
        WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(SEND_BUTTON));
        sendButton.click();
    }
}
