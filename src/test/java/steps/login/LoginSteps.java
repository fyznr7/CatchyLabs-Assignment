package steps.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigManager;

import java.time.Duration;

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
    }

    public void performLogin(String userKey) {
        logger.info("Starting login process for user key: {}", userKey);
        navigateToLoginPageAndAssert(LOGIN_URL);

        String username = ConfigManager.getNestedProperty("user", userKey, "username");
        String password = ConfigManager.getNestedProperty("user", userKey, "password");

        Runnable[] steps = {
                () -> enterUsername(username),
                () -> enterPassword(password),
                this::clickLoginButton,
                () -> assertCurrentUrlContains("dashboard"),
                this::clickMoneyTransferButton
        };

        for (Runnable step : steps) {
            step.run();
        }

        logger.info("Login successful for user: {}", username);
    }

    public void navigateToLoginPageAndAssert(String url) {
        navigateToLoginPage(url);
        assertCurrentUrl(url);
    }

    private void navigateToLoginPage(String url) {
        logger.debug("Navigating to login page: {}", url);
        driver.get(url);
    }

    private void assertCurrentUrl(String expectedUrl) {
        String actualUrl = driver.getCurrentUrl();
        if (!actualUrl.equals(expectedUrl)) {
            logger.error("URL mismatch! Expected: {}, but got: {}", expectedUrl, actualUrl);
            throw new AssertionError("URL mismatch! Expected: " + expectedUrl + ", but got: " + actualUrl);
        }
        logger.info("URL assertion passed. Current URL: {}", actualUrl);
    }

    private void assertCurrentUrlContains(String expectedSubstring) {
        String actualUrl = driver.getCurrentUrl();
        if (!actualUrl.contains(expectedSubstring)) {
            logger.error("URL does not contain expected substring! Expected to contain: {}, but got: {}", expectedSubstring, actualUrl);
            throw new AssertionError("URL does not contain expected substring! Expected to contain: " + expectedSubstring + ", but got: " + actualUrl);
        }
        logger.info("URL substring assertion passed. Current URL: {}", actualUrl);
    }

    private void enterUsername(String username) {
        logger.debug("Entering username: {}", username);
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    private void enterPassword(String password) {
        logger.debug("Entering password");
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    private void clickLoginButton() {
        logger.debug("Clicking login button");
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        loginButton.click();
    }

    private void clickMoneyTransferButton() {
        logger.debug("Clicking money transfer button");
        WebElement moneyTransferButton = wait.until(ExpectedConditions.elementToBeClickable(MONEY_TRANSFER_BUTTON));
        moneyTransferButton.click();
    }
}
