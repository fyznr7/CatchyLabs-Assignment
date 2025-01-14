package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static WebDriver driver;

    public enum Browser {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI,
        MWEB_CHROME,
        MWEB_FIREFOX,
        MWEB_EDGE,
        MWEB_SAFARI
    }

    public static WebDriver getDriver(String browser, boolean isMobile, String deviceName, int width, int height) {
        if (driver != null) {
            logger.info("Reusing existing WebDriver instance.");
            return driver;
        }

        logger.info("Initializing WebDriver for browser: {} (isMobile: {}, deviceName: {}, width: {}, height: {})", browser, isMobile, deviceName, width, height);

        try {
            Browser browserEnum = Browser.valueOf(browser.toUpperCase());
            driver = isMobile ? getMobileDriver(browserEnum, deviceName, width, height) : getWebDriver(browserEnum);
            logger.info("WebDriver initialized successfully for browser: {}", browser);
        } catch (IllegalArgumentException e) {
            logger.error("Unsupported browser: {}", browser, e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: {}", browser, e);
            throw e;
        }

        return driver;
    }

    private static WebDriver getWebDriver(Browser browser) {
        logger.info("Setting up WebDriver for browser: {}", browser);
        try {
            switch (browser) {
                case CHROME -> {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized", "--disable-popup-blocking", "--incognito");
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    driver = new ChromeDriver(chromeOptions);
                }
                case FIREFOX -> driver = new FirefoxDriver(new FirefoxOptions().addArguments("-private").addPreference("dom.webnotifications.enabled", false));
                case EDGE -> driver = new EdgeDriver(new EdgeOptions().addArguments("--start-maximized", "--disable-popup-blocking"));
                case SAFARI -> {
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        driver = new SafariDriver(new SafariOptions());
                    } else {
                        throw new IllegalArgumentException("Safari is supported only on macOS.");
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            logger.info("WebDriver setup completed for browser: {}", browser);
        } catch (Exception e) {
            logger.error("Failed to set up WebDriver for browser: {}", browser, e);
            throw e;
        }
        return driver;
    }

    private static WebDriver getMobileDriver(Browser browser, String deviceName, int width, int height) {
        logger.info("Setting up mobile WebDriver for browser: {}, deviceName: {}, width: {}, height: {}", browser, deviceName, width, height);
        try {
            switch (browser) {
                case MWEB_CHROME -> {
                    ChromeOptions mobileChromeOptions = new ChromeOptions();
                    mobileChromeOptions.setExperimentalOption("mobileEmulation", configureMobileEmulation(deviceName, width, height));
                    mobileChromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    driver = new ChromeDriver(mobileChromeOptions);
                }
                case MWEB_FIREFOX, MWEB_EDGE, MWEB_SAFARI -> driver = setupDimensionedDriver(browser, width, height);
                default -> throw new IllegalArgumentException("Unsupported mobile browser: " + browser);
            }
            logger.info("Mobile WebDriver setup completed for browser: {}", browser);
        } catch (Exception e) {
            logger.error("Failed to set up mobile WebDriver for browser: {}", browser, e);
            throw e;
        }
        return driver;
    }

    private static WebDriver setupDimensionedDriver(Browser browser, int width, int height) {
        logger.info("Setting up dimensioned WebDriver for browser: {}, width: {}, height: {}", browser, width, height);
        try {
            driver = switch (browser) {
                case MWEB_FIREFOX -> new FirefoxDriver(new FirefoxOptions());
                case MWEB_EDGE -> new EdgeDriver(new EdgeOptions());
                case MWEB_SAFARI -> new SafariDriver(new SafariOptions());
                default -> throw new IllegalArgumentException("Unsupported browser for dimensions: " + browser);
            };
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
            logger.info("Dimensioned WebDriver setup completed for browser: {}", browser);
        } catch (Exception e) {
            logger.error("Failed to set up dimensioned WebDriver for browser: {}", browser, e);
            throw e;
        }
        return driver;
    }

    private static Map<String, Object> configureMobileEmulation(String deviceName, int width, int height) {
        if (!deviceName.isEmpty()) {
            logger.debug("Configuring mobile emulation with deviceName: {}", deviceName);
            return Map.of("deviceName", deviceName);
        }
        logger.debug("Configuring mobile emulation with custom dimensions: width={}, height={}", width, height);
        return Map.of(
                "deviceMetrics", Map.of("width", width, "height", height, "pixelRatio", 3.0),
                "userAgent", "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36"
        );
    }

    public static void quitDriver() {
        if (driver != null) {
            logger.info("Closing and quitting WebDriver.");
            try {
                driver.quit();
                driver = null;
                logger.info("WebDriver quit successfully.");
            } catch (Exception e) {
                logger.error("Failed to quit WebDriver.", e);
                throw e;
            }
        } else {
            logger.warn("No WebDriver instance to quit.");
        }
    }
}
