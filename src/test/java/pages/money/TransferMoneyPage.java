package pages.money;

import org.openqa.selenium.By;

public class TransferMoneyPage {
    public static final String pageName = "TransferMoneyPage";

    public static final By TRANSFER_MONEY_BUTTON = By.xpath("//div[text()='Transfer money']/parent::div");
    public static final By SENDER_ACCOUNT_DROPDOWN = By.xpath("(//div[@class='css-175oi2r r-1777fci']//select)[1]");
    public static final By RECEIVER_ACCOUNT_DROPDOWN = By.xpath("(//div[@class='css-175oi2r r-1777fci']//select)[2]");
    public static final By AMOUNT_FIELD = By.xpath("//input[contains(@inputmode,'numeric')]");
    public static final By SEND_BUTTON = By.xpath("//div[text()='Send']//parent::div");
    public static final By TRANSFERRED_MONEY_AMOUNT_TEXT = By.xpath("(//div[text()='Amount:']/following-sibling::div//div)[1]");
    public static final By TOTAL_AMOUNT = By.xpath("//div[text()='Amount']/following-sibling::div/div");
}
