package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    public static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    public static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            logger.info("Loading configuration file from path: {}", CONFIG_FILE_PATH);
            properties.load(fis);
            logger.info("Configuration file loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load configuration file from path: {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE_PATH, e);
        }
    }

    public static String getProperty(String key) {
        logger.info("Retrieving property for key: {}", key);
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found for key: {}", key);
        } else {
            logger.debug("Property value for key {}: {}", key, value);
        }
        return value;
    }

    public static String getNestedProperty(String category, String subKey, String field) {
        String key = String.format("%s.%s.%s", category, subKey, field);
        logger.info("Retrieving nested property for key: {}", key);
        return getProperty(key);
    }

    public static void setProperty(String key, String value) {
        logger.info("Setting property. Key: {}, Value: {}", key, value);
        properties.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fos, null);
            logger.info("Configuration saved successfully to file: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to save configuration to file: {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Failed to save configuration to file.", e);
        }
    }
}
