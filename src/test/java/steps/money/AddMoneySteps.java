package steps.money;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigManager;

import java.time.Duration;
import java.util.function.Consumer;

public class AddMoneySteps {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(AddMoneySteps.class);

    private static final By ADD_MONEY_BUTTON = By.xpath("//div[text()='Add money']//parent::div");
    private static final By CARD_NUMBER_FIELD = By.xpath("//div[text()='Card number']//parent::div//input");
    private static final By CARD_HOLDER_FIELD = By.xpath("//div[text()='Card holder']//parent::div//input");
    private static final By EXPIRY_DATE_FIELD = By.xpath("//div[text()='Expiry date']//parent::div//input");
    private static final By CVV_FIELD = By.xpath("//div[text()='CVV']//parent::div//input");
    private static final By AMOUNT_FIELD = By.xpath("//div[text()='Amount']//parent::div//input");
    private static final By ADD_BUTTON = By.xpath("//div[text()='Add']//parent::div");
    private static final By CONFIRM_BUTTON = By.xpath("//div[@class='css-146c3p1 r-lrvibr r-1loqt21']");
    private static final By TOTAL_AMOUNT = By.xpath("//div[text()='Amount']/following-sibling::div/div");

    public enum CardDetails {
        CARD_NUMBER("number"),
        CARD_HOLDER("holder"),
        EXPIRY_DATE("expireDate"),
        CVV("cvv");

        private final String key;

        CardDetails(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public AddMoneySteps(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void performAddMoney(String cardKey, String amount) {
        logger.info("Starting add money process for card: {} with amount: {}", cardKey, amount);

        double initialTotal = getTotalAmount();
        logger.debug("Initial total amount: {}", initialTotal);

        executeSteps(
                this::clickAddMoneyButton,
                () -> enterCardDetails(cardKey),
                () -> enterAmount(amount),
                this::clickAddButton,
                this::clickConfirmButton
        );

        double finalTotal = getTotalAmount();
        validateTransaction(cardKey, initialTotal, finalTotal, amount);

        logger.info("Final total amount after operation: {}", finalTotal);
    }

    private void validateTransaction(String cardKey, double initialTotal, double finalTotal, String amount) {
        if (cardKey.equals("invalidCard")) {
            logger.debug("Invalid card detected: {}. Expecting no change in total amount.", cardKey);
            assertTotalAmount(initialTotal, finalTotal);
        } else {
            double expectedTotal = initialTotal + Double.parseDouble(amount);
            logger.debug("Valid card used. Expecting updated total amount: {}", expectedTotal);
            assertTotalAmount(expectedTotal, finalTotal);
        }
    }

    private void executeSteps(Runnable... steps) {
        for (Runnable step : steps) {
            step.run();
        }
    }

    private double getTotalAmount() {
        String totalText = wait.until(ExpectedConditions.visibilityOfElementLocated(TOTAL_AMOUNT)).getText();
        logger.debug("Retrieved total amount text: {}", totalText);
        return Double.parseDouble(totalText);
    }

    private void assertTotalAmount(double expected, double actual) {
        if (Double.compare(expected, actual) != 0) {
            throw new AssertionError("Total amount mismatch! Expected: " + expected + ", but got: " + actual);
        }
        logger.info("Total amount assertion passed. Expected: {}, Actual: {}", expected, actual);
    }

    private void enterCardDetails(String cardKey) {
        for (CardDetails detail : CardDetails.values()) {
            String value = ConfigManager.getNestedProperty("card", cardKey, detail.getKey());
            enterField(getFieldLocator(detail), value);
        }
    }

    private By getFieldLocator(CardDetails detail) {
        return switch (detail) {
            case CARD_NUMBER -> CARD_NUMBER_FIELD;
            case CARD_HOLDER -> CARD_HOLDER_FIELD;
            case EXPIRY_DATE -> EXPIRY_DATE_FIELD;
            case CVV -> CVV_FIELD;
        };
    }

    private void clickAddMoneyButton() {
        performAction(ADD_MONEY_BUTTON, WebElement::click);
    }

    private void enterAmount(String amount) {
        enterField(AMOUNT_FIELD, amount);
    }

    private void clickAddButton() {
        performAction(ADD_BUTTON, WebElement::click);
    }

    private void clickConfirmButton() {
        performAction(CONFIRM_BUTTON, WebElement::click);
    }

    private void enterField(By locator, String value) {
        performAction(locator, field -> {
            field.clear();
            field.sendKeys(value);
        });
    }

    private void performAction(By locator, Consumer<WebElement> action) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        action.accept(element);
    }
}
