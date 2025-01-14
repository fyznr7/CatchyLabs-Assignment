package pages.login;

import org.openqa.selenium.By;

public class LogoutPage {

    public static final By LOGOUT_BUTTON = By.xpath("//div[text()='Logout']/parent::div");
    public static final By BACK_BUTTON = By.xpath("//div[text()='Back']/parent::div");

    public static String getPageName() {
        return "LogoutPage";
    }
}
