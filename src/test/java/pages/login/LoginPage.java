package pages.login;

import org.openqa.selenium.By;

public class LoginPage {

    public static final String pageName = "LoginPage";
    public static final By USERNAME_FIELD = By.cssSelector("input[placeholder='Username']");
    public static final By PASSWORD_FIELD = By.cssSelector("input[placeholder='Password']");
    public static final By LOGIN_BUTTON = By.xpath("//div[text()='Login']/parent::div");
    public static final By MONEY_TRANSFER_BUTTON = By.xpath("//div[text()='Open Money Transfer']//parent::div");

    public static class LogoutPage {
        public static final By LOGOUT_BUTTON = By.xpath("//div[text()='Logout']/parent::div");
        public static final By BACK_BUTTON = By.xpath("//div[text()='Back']/parent::div");
    }
}
