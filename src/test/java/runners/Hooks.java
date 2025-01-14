package runners;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void captureScreenshot(String stepTiming, Scenario scenario) {
        if (driver == null) {
            return;
        }
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

    public void setBrowserAndInitialize(String browser, boolean isMobile, String deviceName, String dimensions) {
        if (browser == null || browser.isEmpty()) {
            browser = DriverManager.Browser.CHROME.name();
        }

        if (isMobile) {
            String[] dimensionParts = dimensions.split("x");
            int width = Integer.parseInt(dimensionParts[0]);
            int height = Integer.parseInt(dimensionParts[1]);
            driver = DriverManager.getDriver(browser, true, deviceName, width, height);
        } else {
            driver = DriverManager.getDriver(browser, false, null, 0, 0);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            DriverManager.quitDriver();
        }
    }
}
