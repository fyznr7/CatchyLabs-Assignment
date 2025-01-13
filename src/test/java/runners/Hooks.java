package runners;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.ConfigManager;
import utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {
    public static WebDriver driver;
    public static Scenario currentScenario;

    @Before
    public void setUp(Scenario scenario) {
        String browser = ConfigManager.getProperty("browserName");
        driver = DriverManager.getDriver(browser);
        currentScenario = scenario;
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public static void captureScreenshot(String stepTiming, Scenario scenario) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] imageBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            scenario.attach(imageBytes, "image/png", stepTiming + " Screenshot");

            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotName = "screenshots/" + scenario.getName().replace(" ", "_") + "_" + stepTiming + "_" + timestamp + ".png";
            Files.createDirectories(Paths.get("screenshots"));
            Files.copy(srcFile.toPath(), Paths.get(screenshotName));
        } catch (IOException e) {
            System.err.println("Failed to capture and save screenshot: " + e.getMessage());
        }
    }

    public static void updateBrowserConfig(String browser) {
        ConfigManager.setProperty("browser", browser);
    }
}
