package steps.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.login.LoginPage;
import pages.login.LogoutPage;
import runners.Hooks;
import steps.main.MainSteps;
import utils.ConfigManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class LoginSteps extends MainSteps {

    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    private final String loginUrl;

    public LoginSteps() {
        super(Hooks.getDriver());
        this.loginUrl = ConfigManager.getProperty("login.url");
        validateLoginUrl();
        goToUrl(loginUrl);
    }

    private void validateLoginUrl() {
        if (loginUrl == null || loginUrl.isEmpty()) {
            throw new IllegalStateException("Login URL not configured.");
        }
    }

    public void performLogin(String userKey) {
        logger.info("Starting login for user: {}", userKey);
        String username = getUsername(userKey);
        String password = getPassword(userKey);
        login(username, password);
        navigateToMoneyTransfer();
    }

    public void performLogout() {
        logger.info("Starting logout process.");
        logout();
        verifyLogoutSuccess();
    }

    private String getUsername(String userKey) {
        String username = ConfigManager.getNestedProperty("user", userKey, "username");
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username not configured for userKey: " + userKey);
        }
        return username;
    }

    private String getPassword(String userKey) {
        String password = ConfigManager.getNestedProperty("user", userKey, "password");
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password not configured for userKey: " + userKey);
        }
        return password;
    }

    private void login(String username, String password) {
        logger.info("Logging in with username: {}, hashPassword: {}", username, hashPassword(password));
        sendKeysToElement(LoginPage.USERNAME_FIELD, username);
        sendKeysToElement(LoginPage.PASSWORD_FIELD, password);
        clickElement(LoginPage.LOGIN_BUTTON);
    }

    private void navigateToMoneyTransfer() {
        logger.info("Navigating to money transfer.");
        clickElement(LoginPage.MONEY_TRANSFER_BUTTON);
    }

    private void logout() {
        clickElement(LogoutPage.BACK_BUTTON);
        clickElement(LogoutPage.LOGOUT_BUTTON);
    }

    private void verifyLogoutSuccess() {
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("signIn")) {
            throw new AssertionError("Logout failed. Current URL: " + currentUrl);
        }
        logger.info("Logout successful. Current URL: {}", currentUrl);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed.", e);
        }
    }
}
