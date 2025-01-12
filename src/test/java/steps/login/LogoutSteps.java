package steps.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LogoutSteps {

    private static final Logger logger = LoggerFactory.getLogger(LogoutSteps.class);
    private static final By LOGOUT_BUTTON = By.xpath("//div[text()='Logout']/parent::div");
    private static final By BACK_BUTTON = By.xpath("//div[text()='Back']/parent::div\n");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);


    public static void performLogout(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        try {
            logger.info("Waiting for the logout button to be clickable...");
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(BACK_BUTTON));
            backButton.click();
            WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_BUTTON));
            logoutButton.click();
            logger.info("User logged out successfully.");
        } catch (Exception e) {
            logger.warn("Logout button not visible. Navigating back...");
            driver.navigate().back();
        }
    }
}
