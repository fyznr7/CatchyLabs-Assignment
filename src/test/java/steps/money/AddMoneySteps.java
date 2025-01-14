package steps.money;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.money.AddMoneyPage;
import runners.Hooks;
import steps.main.MainSteps;
import utils.ConfigManager;

public class AddMoneySteps extends MainSteps {

    private static final Logger logger = LoggerFactory.getLogger(AddMoneySteps.class);

    public AddMoneySteps() {
        super(Hooks.getDriver());
        if (this.driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }
        logger.info("AddMoneySteps initialized with WebDriver.");
    }

    private boolean isInvalidCard(String cardKey) {
        return cardKey.toLowerCase().contains("invalid");
    }

    public void performAddMoney(String cardKey, String amount) {
        logger.info("Starting to add money with card '{}'", cardKey);
        try {
            double initialTotal = getTotalAmount();
            logger.info("Initial total amount: {}", initialTotal);
            initiateAddMoney();
            enterCardDetails(cardKey);
            enterAmount(amount);
            confirmAddMoney();
            double finalTotal = getTotalAmount();
            logger.info("Final total amount: {}", finalTotal);

            validateTotalAmount(initialTotal, finalTotal, Double.parseDouble(amount), isInvalidCard(cardKey));
            logger.info("Money added successfully using card '{}' with amount '{}'.", cardKey, amount);
        } catch (Exception e) {
            logger.error("Failed to add money with card '{}'", cardKey, e);
            throw e;
        }
    }

    private void initiateAddMoney() {
        logger.info("Initiating add money process...");
        clickElement(AddMoneyPage.ADD_MONEY_BUTTON);
    }

    private void enterCardDetails(String cardKey) {
        logger.info("Entering card details for card '{}'", cardKey);
        try {
            enterCardNumber(cardKey);
            enterCardHolder(cardKey);
            enterExpiryDate(cardKey);
            enterCVV(cardKey);
            logger.info("Card details entered successfully for card '{}'.", cardKey);
        } catch (Exception e) {
            logger.error("Failed to enter card details for card '{}'.", cardKey, e);
            throw e;
        }
    }

    private void enterCardNumber(String cardKey) {
        String cardNumber = getValidatedProperty("card", cardKey, "number");
        logger.info("Entering card number for cardKey: {}", cardKey);
        sendKeysToElement(AddMoneyPage.CARD_NUMBER_FIELD, cardNumber);
        logger.info("Card number '{}' entered successfully.", maskSensitiveData(cardNumber));
    }

    private void enterCardHolder(String cardKey) {
        String cardHolder = getValidatedProperty("card", cardKey, "holder");
        logger.info("Entering card holder for cardKey: {}", cardKey);
        sendKeysToElement(AddMoneyPage.CARD_HOLDER_FIELD, cardHolder);
        logger.info("Card holder '{}' entered successfully.", cardHolder);
    }

    private void enterExpiryDate(String cardKey) {
        String expiryDate = getValidatedProperty("card", cardKey, "expireDate");
        logger.info("Entering expiry date for cardKey: {}", cardKey);
        sendKeysToElement(AddMoneyPage.EXPIRY_DATE_FIELD, expiryDate);
        logger.info("Expiry date '{}' entered successfully.", expiryDate);
    }

    private void enterCVV(String cardKey) {
        String cvv = getValidatedProperty("card", cardKey, "cvv");
        logger.info("Entering CVV for cardKey: {}", cardKey);
        sendKeysToElement(AddMoneyPage.CVV_FIELD, cvv);
        logger.info("CVV '{}' entered successfully.", maskSensitiveData(cvv));
    }

    private void enterAmount(String amount) {
        sendKeysToElement(AddMoneyPage.AMOUNT_FIELD, amount);
        logger.info("Amount '{}' entered successfully.", amount);
    }

    private void confirmAddMoney() {
        clickElement(AddMoneyPage.ADD_BUTTON);
        clickElement(AddMoneyPage.CONFIRM_BUTTON);
    }

    private double getTotalAmount() {
        logger.info("Retrieving total amount...");
        try {
            String totalText = getTextFromElement(AddMoneyPage.TOTAL_AMOUNT);
            double totalAmount = Double.parseDouble(totalText);
            logger.info("Total amount retrieved: {}", totalAmount);
            return totalAmount;
        } catch (Exception e) {
            logger.error("Failed to retrieve total amount.", e);
            throw e;
        }
    }

    private void validateTotalAmount(double initialTotal, double finalTotal, double amount, boolean isInvalidCard) {
        boolean isTotalChanged = Double.compare(initialTotal, finalTotal) != 0;
        if (isInvalidCard && isTotalChanged) {
            throw new AssertionError("Total amount should not change for invalid card! Initial: " + initialTotal + ", Final: " + finalTotal);
        }
        if (!isInvalidCard && Double.compare(initialTotal + amount, finalTotal) != 0) {
            throw new AssertionError("Transaction validation failed! Expected total: " + (initialTotal + amount) + ", but got: " + finalTotal);
        }
        logger.info("Validation passed. Final total: {}", finalTotal);
    }

    private String getValidatedProperty(String category, String key, String property) {
        String value = ConfigManager.getNestedProperty(category, key, property);
        if (value == null || value.isEmpty()) {
            String errorMessage = String.format("%s is not configured for %s key: %s", property, category, key);
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    private String maskSensitiveData(String data) {
        if (data == null || data.length() <= 4) {
            return "****";
        }
        return "****" + data.substring(data.length() - 4);
    }
}
