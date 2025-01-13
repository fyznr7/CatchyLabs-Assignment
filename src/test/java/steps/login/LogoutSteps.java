package steps.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class LogoutSteps {

    private static final Logger logger = LoggerFactory.getLogger(LogoutSteps.class);
    private static final By LOGOUT_BUTTON = By.xpath("//div[text()='Logout']/parent::div");
    private static final By BACK_BUTTON = By.xpath("//div[text()='Back']/parent::div");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    public static void performLogout(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        List<Runnable> steps = List.of(
                () -> clickBackButton(driver, wait),
                () -> clickLogoutButton(driver, wait),
                () -> assertLogoutSuccess(driver)
        );

        steps.forEach(step -> {
            try {
                step.run();
            } catch (Exception e) {
                logger.error("Step failed: {}", e.getMessage(), e);
            }
        });

        logger.info("Logout process completed.");
    }

    private static void clickBackButton(WebDriver driver, WebDriverWait wait) {
        logger.debug("Waiting for the back button to be clickable...");
        WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(BACK_BUTTON));
        backButton.click();
        logger.debug("Back button clicked.");
    }

    private static void clickLogoutButton(WebDriver driver, WebDriverWait wait) {
        logger.debug("Waiting for the logout button to be clickable...");
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_BUTTON));
        logoutButton.click();
        logger.info("Logout button clicked.");
    }

    private static void assertLogoutSuccess(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("signIn")) {
            logger.error("Logout assertion failed. Expected URL to contain 'signIn', but got: {}", currentUrl);
            throw new AssertionError("Logout failed. Expected URL to contain 'signIn', but got: " + currentUrl);
        }
        logger.info("Logout assertion passed. Current URL: {}", currentUrl);
    }
}
