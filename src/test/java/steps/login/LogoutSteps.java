package steps.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runners.Hooks;

import java.time.Duration;

public class LogoutSteps {

    private static final Logger logger = LoggerFactory.getLogger(LogoutSteps.class);
    private static final By LOGOUT_BUTTON = By.xpath("//div[text()='Logout']/parent::div");
    private static final By BACK_BUTTON = By.xpath("//div[text()='Back']/parent::div");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private static final WebDriver driver = Hooks.getDriver();
    private static final WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

    public static void performLogout() {
        if (driver == null) {
            logger.error("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
            throw new IllegalStateException("WebDriver is not initialized. Ensure the Hooks class is correctly set up.");
        }

        logger.info("Starting the logout process.");
        try {
            clickBackButton();
            clickLogoutButton();
            assertLogoutSuccess();
            logger.info("Logout process completed successfully.");
        } catch (Exception e) {
            logger.error("Logout process failed: {}", e.getMessage(), e);
            throw new AssertionError("Logout process failed.", e);
        }
    }

    private static void clickBackButton() {
        logger.info("Attempting to click the back button...");
        try {
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(BACK_BUTTON));
            backButton.click();
            logger.info("Back button clicked successfully.");
        } catch (Exception e) {
            logger.error("Failed to click the back button: {}", e.getMessage(), e);
            throw new AssertionError("Back button click failed.", e);
        }
    }

    private static void clickLogoutButton() {
        logger.info("Attempting to click the logout button...");
        try {
            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_BUTTON));
            logoutButton.click();
            logger.info("Logout button clicked successfully.");
        } catch (Exception e) {
            logger.error("Failed to click the logout button: {}", e.getMessage(), e);
            throw new AssertionError("Logout button click failed.", e);
        }
    }

    private static void assertLogoutSuccess() {
        logger.info("Verifying logout success by checking the URL...");
        try {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("signIn")) {
                logger.error("Logout assertion failed. Expected URL to contain 'signIn', but got: {}", currentUrl);
                throw new AssertionError("Logout failed. Expected URL to contain 'signIn', but got: " + currentUrl);
            }
            logger.info("Logout assertion passed. Current URL: {}", currentUrl);
        } catch (Exception e) {
            logger.error("Logout assertion failed: {}", e.getMessage(), e);
            throw new AssertionError("Logout assertion failed.", e);
        }
    }
}
