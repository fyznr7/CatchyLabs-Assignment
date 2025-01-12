package steps.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;

import java.time.Duration;

public class LoginSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final By USERNAME_FIELD = By.cssSelector("input[placeholder='Username']");
    private static final By PASSWORD_FIELD = By.cssSelector("input[placeholder='Password']");
    private static final By LOGIN_BUTTON = By.xpath("//div[text()='Login']/parent::div");
    private static final By MONEY_TRANSFER_BUTTON = By.xpath("//div[text()='Open Money Transfer']//parent::div");

    public LoginSteps(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void performLogin(String userKey) {
        String loginUrl = "https://catchylabs-webclient.testinium.com/signIn";
        navigateToLoginPage(loginUrl);

        String username = ConfigManager.getNestedProperty("user", userKey, "username");
        String password = ConfigManager.getNestedProperty("user", userKey, "password");

        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        clickMoneyTransferButton();

        System.out.println("Login successful for user: " + username);
    }

    public void navigateToLoginPage(String url) {
        driver.get(url);
    }

    public void enterUsername(String username) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        loginButton.click();
    }

    public void clickMoneyTransferButton() {
        WebElement moneyTransferButton = wait.until(ExpectedConditions.elementToBeClickable(MONEY_TRANSFER_BUTTON));
        moneyTransferButton.click();
    }
}
