package steps.money;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runners.Hooks;
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

    public AddMoneySteps() {
        this.driver = Hooks.getDriver();
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Check Hooks class.");
        }
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.info("AddMoneySteps initialized with WebDriver from Hooks.");
    }

    public void performAddMoney(String cardKey, String amount) {
        logger.info("Starting add money process for card: {} with amount: {}", cardKey, amount);

        try {
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

            logger.info("Add money process completed successfully. Final total amount: {}", finalTotal);
        } catch (Exception e) {
            logger.error("Add money process failed for card: {} with amount: {}", cardKey, amount, e);
            throw e;
        }
    }

    private void validateTransaction(String cardKey, double initialTotal, double finalTotal, String amount) {
        logger.info("Validating transaction for card: {}", cardKey);
        try {
            if (cardKey.equals("invalidCard")) {
                logger.debug("Invalid card detected. Expecting no change in total amount.");
                assertTotalAmount(initialTotal, finalTotal);
            } else {
                double expectedTotal = initialTotal + Double.parseDouble(amount);
                logger.debug("Valid card used. Expected total amount: {}", expectedTotal);
                assertTotalAmount(expectedTotal, finalTotal);
            }
            logger.info("Transaction validation passed for card: {}", cardKey);
        } catch (Exception e) {
            logger.error("Transaction validation failed for card: {}", cardKey, e);
            throw e;
        }
    }

    private void executeSteps(Runnable... steps) {
        for (Runnable step : steps) {
            try {
                step.run();
            } catch (Exception e) {
                logger.error("Step execution failed: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    private double getTotalAmount() {
        logger.info("Retrieving total amount...");
        try {
            String totalText = wait.until(ExpectedConditions.visibilityOfElementLocated(TOTAL_AMOUNT)).getText();
            logger.debug("Retrieved total amount text: {}", totalText);
            return Double.parseDouble(totalText);
        } catch (Exception e) {
            logger.error("Failed to retrieve total amount.", e);
            throw e;
        }
    }

    private void assertTotalAmount(double expected, double actual) {
        logger.info("Asserting total amount. Expected: {}, Actual: {}", expected, actual);
        if (Double.compare(expected, actual) != 0) {
            logger.error("Total amount mismatch! Expected: {}, but got: {}", expected, actual);
            throw new AssertionError("Total amount mismatch! Expected: " + expected + ", but got: " + actual);
        }
        logger.info("Total amount assertion passed. Expected: {}, Actual: {}", expected, actual);
    }

    private void enterCardDetails(String cardKey) {
        logger.info("Entering card details for card: {}", cardKey);
        try {
            for (CardDetails detail : CardDetails.values()) {
                String value = ConfigManager.getNestedProperty("card", cardKey, detail.getKey());
                enterField(getFieldLocator(detail), value);
            }
            logger.info("Card details entered successfully for card: {}", cardKey);
        } catch (Exception e) {
            logger.error("Failed to enter card details for card: {}", cardKey, e);
            throw e;
        }
    }

    private By getFieldLocator(CardDetails detail) {
        logger.info("Getting locator for card detail: {}", detail);
        return switch (detail) {
            case CARD_NUMBER -> CARD_NUMBER_FIELD;
            case CARD_HOLDER -> CARD_HOLDER_FIELD;
            case EXPIRY_DATE -> EXPIRY_DATE_FIELD;
            case CVV -> CVV_FIELD;
        };
    }

    private void clickAddMoneyButton() {
        logger.info("Clicking 'Add Money' button...");
        performAction(ADD_MONEY_BUTTON, WebElement::click);
        logger.info("'Add Money' button clicked successfully.");
    }

    private void enterAmount(String amount) {
        logger.info("Entering amount: {}", amount);
        enterField(AMOUNT_FIELD, amount);
        logger.info("Amount entered successfully.");
    }

    private void clickAddButton() {
        logger.info("Clicking 'Add' button...");
        performAction(ADD_BUTTON, WebElement::click);
        logger.info("'Add' button clicked successfully.");
    }

    private void clickConfirmButton() {
        logger.info("Clicking 'Confirm' button...");
        performAction(CONFIRM_BUTTON, WebElement::click);
        logger.info("'Confirm' button clicked successfully.");
    }

    private void enterField(By locator, String value) {
        logger.info("Entering value '{}' in field located by {}", value, locator);
        performAction(locator, field -> {
            field.clear();
            field.sendKeys(value);
        });
        logger.info("Value '{}' entered successfully in field located by {}", value, locator);
    }

    private void performAction(By locator, Consumer<WebElement> action) {
        logger.info("Performing action on element located by {}", locator);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            action.accept(element);
            logger.info("Action performed successfully on element located by {}", locator);
        } catch (Exception e) {
            logger.error("Action failed on element located by {}: {}", locator, e.getMessage(), e);
            throw e;
        }
    }
}
