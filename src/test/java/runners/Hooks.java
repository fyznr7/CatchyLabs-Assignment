package runners;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class Hooks {
    public static WebDriver driver;

    @Before
    public void setUp() {
        String browser = System.getProperty("browser", "chrome"); // Varsayılan tarayıcı
        driver = DriverManager.getDriver(browser);
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
