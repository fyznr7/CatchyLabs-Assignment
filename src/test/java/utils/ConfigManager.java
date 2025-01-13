package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    public static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    public static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE_PATH);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getNestedProperty(String category, String subKey, String field) {
        String key = String.format("%s.%s.%s", category, subKey, field);
        return getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
            properties.store(fos, null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save configuration to file.");
        }
    }
}
