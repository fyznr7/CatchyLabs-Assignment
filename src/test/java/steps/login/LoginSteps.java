package steps.login;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;

public class LoginSteps {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);

    private static final By USERNAME_FIELD = By.cssSelector("input[placeholder='Username']");
    private static final By PASSWORD_FIELD = By.cssSelector("input[placeholder='Password']");
    private static final By LOGIN_BUTTON = By.xpath("//div[text()='Login']/parent::div");
    private static final By MONEY_TRANSFER_BUTTON = By.xpath("//div[text()='Open Money Transfer']//parent::div");
    private static final String LOGIN_URL = "https://catchylabs-webclient.testinium.com/signIn";

    public LoginSteps(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.info("LoginSteps initialized with WebDriver.");
    }

    public void performLogin(String userKey) {
        logger.info("Starting login process for user: {}", userKey);
        try {
            navigateToLoginPageAndAssert(LOGIN_URL);
            waitForPageToLoad();

            String username = ConfigManager.getNestedProperty("user", userKey, "username");
            String password = ConfigManager.getNestedProperty("user", userKey, "password");

            enterUsername(username);
            enterPassword(password);
            clickLoginButton();
            waitForDashboardToLoad();
            clickMoneyTransferButton();

            logger.info("Login process completed successfully for user: {}", username);
        } catch (Exception e) {
            logger.error("Login process failed for user: {}", userKey, e);
            throw new RuntimeException("Login process failed.", e);
        }
    }

    private void navigateToLoginPageAndAssert(String url) {
        logger.info("Navigating to login page: {}", url);
        try {
            driver.get(url);
            String actualUrl = driver.getCurrentUrl();
            if (!actualUrl.equals(url)) {
                logger.error("URL mismatch! Expected: {}, but got: {}", url, actualUrl);
                throw new AssertionError("URL mismatch! Expected: " + url + ", but got: " + actualUrl);
            }
            logger.info("Successfully navigated to: {}", actualUrl);
        } catch (Exception e) {
            logger.error("Failed to navigate to login page: {}", url, e);
            throw e;
        }
    }

    private void waitForPageToLoad() {
        logger.info("Waiting for the page to load...");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(webDriver -> {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
                return jsExecutor.executeScript("return document.readyState").equals("complete");
            });
            logger.info("Page loaded successfully.");
        } catch (Exception e) {
            logger.error("Failed to wait for the page to load.", e);
            throw e;
        }
    }

    private void waitForDashboardToLoad() {
        logger.info("Waiting for the dashboard to load with expected URL: {}", LOGIN_URL);
        try {
            wait.until(ExpectedConditions.urlContains(LOGIN_URL));
            logger.info("Dashboard page loaded successfully with URL containing: {}", LOGIN_URL);
        } catch (Exception e) {
            logger.error("Failed to wait for the dashboard to load with URL: {}", LOGIN_URL, e);
            throw e;
        }
    }

    private void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
            usernameField.clear();
            usernameField.sendKeys(username);
            logger.info("Username '{}' entered successfully.", username);
        } catch (Exception e) {
            logger.error("Failed to enter username: {}", username, e);
            throw e;
        }
    }

    private void enterPassword(String password) {
        String hashedPassword = hashPassword(password);
        logger.info("Entering password with hash: {}", hashedPassword);
        try {
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
            passwordField.clear();
            passwordField.sendKeys(password);
            logger.info("Password entered successfully.");
        } catch (Exception e) {
            logger.error("Failed to enter password.", e);
            throw e;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to hash password.", e);
            throw new RuntimeException("Hashing algorithm not available.", e);
        }
    }

    private void clickLoginButton() {
        logger.info("Clicking login button...");
        try {
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
            loginButton.click();
            logger.info("Login button clicked successfully.");
        } catch (Exception e) {
            logger.error("Failed to click login button.", e);
            throw e;
        }
    }

    private void clickMoneyTransferButton() {
        logger.info("Clicking money transfer button...");
        try {
            WebElement moneyTransferButton = wait.until(ExpectedConditions.elementToBeClickable(MONEY_TRANSFER_BUTTON));
            moneyTransferButton.click();
            logger.info("Money transfer button clicked successfully.");
        } catch (Exception e) {
            logger.error("Failed to click money transfer button.", e);
            throw e;
        }
    }
}
