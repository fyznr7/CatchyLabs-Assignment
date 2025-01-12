package steps.money;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;

import java.time.Duration;

public class AddMoneySteps {

    private WebDriver driver;
    private WebDriverWait wait;

    // Element locators
    private static final By ADD_MONEY_BUTTON = By.xpath("//div[text()='Add money']//parent::div");
    private static final By CARD_NUMBER_FIELD = By.xpath("//div[text()='Card number']//parent::div//input");
    private static final By CARD_HOLDER_FIELD = By.xpath("//div[text()='Card holder']//parent::div//input");
    private static final By EXPIRY_DATE_FIELD = By.xpath("//div[text()='Expiry date']//parent::div//input");
    private static final By CVV_FIELD = By.xpath("//div[text()='CVV']//parent::div//input");
    private static final By AMOUNT_FIELD = By.xpath("//div[text()='Amount']//parent::div//input");
    private static final By ADD_BUTTON = By.xpath("//div[text()='Add']//parent::div");
    private static final By CONFIRM_BUTTON = By.xpath("//div[@class='css-146c3p1 r-lrvibr r-1loqt21']");

    // Constructor
    public AddMoneySteps(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Perform add money operation using a card.
     * @param cardKey The card key to fetch card details from ConfigManager
     * @param amount The amount to add
     */
    public void performAddMoney(String cardKey, String amount) {
        // Retrieve card details from configuration
        String cardNumber = ConfigManager.getNestedProperty("card", cardKey, "number");
        String cardHolder = ConfigManager.getNestedProperty("card", cardKey, "holder");
        String expireDate = ConfigManager.getNestedProperty("card", cardKey, "expireDate");
        String cvv = ConfigManager.getNestedProperty("card", cardKey, "cvv");

        // Perform add money steps
        clickAddMoneyButton();
        enterCardDetails(cardNumber, cardHolder, expireDate, cvv);
        enterAmount(amount);
        clickAddButton();
        clickConfirmButton();

        System.out.println("Money added using card: " + cardKey + ", amount: " + amount);
    }

    // Click "Add Money" button
    public void clickAddMoneyButton() {
        WebElement addMoneyButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_MONEY_BUTTON));
        addMoneyButton.click();
    }

    // Enter card details
    public void enterCardDetails(String cardNumber, String cardHolder, String expireDate, String cvv) {
        WebElement cardNumberField = wait.until(ExpectedConditions.visibilityOfElementLocated(CARD_NUMBER_FIELD));
        cardNumberField.clear();
        cardNumberField.sendKeys(cardNumber);

        WebElement cardHolderField = wait.until(ExpectedConditions.visibilityOfElementLocated(CARD_HOLDER_FIELD));
        cardHolderField.clear();
        cardHolderField.sendKeys(cardHolder);

        WebElement expiryDateField = wait.until(ExpectedConditions.visibilityOfElementLocated(EXPIRY_DATE_FIELD));
        expiryDateField.clear();
        expiryDateField.sendKeys(expireDate);

        WebElement cvvField = wait.until(ExpectedConditions.visibilityOfElementLocated(CVV_FIELD));
        cvvField.clear();
        cvvField.sendKeys(cvv);
    }

    // Enter amount
    public void enterAmount(String amount) {
        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(AMOUNT_FIELD));
        amountField.clear();
        amountField.sendKeys(amount);
    }

    // Click "Add" button
    public void clickAddButton() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_BUTTON));
        addButton.click();
    }

    // Click confirm button
    public void clickConfirmButton() {
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(CONFIRM_BUTTON));
        confirmButton.click();
    }
}
