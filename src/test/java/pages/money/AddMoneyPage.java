package pages.money;

import org.openqa.selenium.By;

public class AddMoneyPage {
    public static final String pageName = "AddMoneyPage";

    public static final By ADD_MONEY_BUTTON = By.xpath("//div[text()='Add money']//parent::div");
    public static final By CARD_NUMBER_FIELD = By.xpath("//div[text()='Card number']//parent::div//input");
    public static final By CARD_HOLDER_FIELD = By.xpath("//div[text()='Card holder']//parent::div//input");
    public static final By EXPIRY_DATE_FIELD = By.xpath("//div[text()='Expiry date']//parent::div//input");
    public static final By CVV_FIELD = By.xpath("//div[text()='CVV']//parent::div//input");
    public static final By AMOUNT_FIELD = By.xpath("//div[text()='Amount']//parent::div//input");
    public static final By ADD_BUTTON = By.xpath("//div[text()='Add']//parent::div");
    public static final By CONFIRM_BUTTON = By.xpath("//div[@class='css-146c3p1 r-lrvibr r-1loqt21']");
    public static final By TOTAL_AMOUNT = By.xpath("//div[text()='Amount']/following-sibling::div/div");
}
