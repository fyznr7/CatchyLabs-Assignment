package pages.main;

import org.openqa.selenium.By;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageLoader {
    private static final Map<String, Map<String, By>> dynamicLocators = new HashMap<>();

    static {
        try {
            initializePages("pages");
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize PageLoader: " + e.getMessage(), e);
        }
    }

    private static void initializePages(String packageName) throws ClassNotFoundException, IllegalAccessException {
        for (Class<?> clazz : getClasses(packageName)) {
            if (!clazz.isInterface()) {
                Map<String, By> elements = getLocatorsFromClass(clazz);
                dynamicLocators.put(clazz.getSimpleName(), elements);
            }
        }
    }

    private static Map<String, By> getLocatorsFromClass(Class<?> clazz) throws IllegalAccessException {
        Map<String, By> elements = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(By.class)) {
                field.setAccessible(true);
                By locator = (By) field.get(null);
                elements.put(field.getName(), locator);
            }
        }
        return elements;
    }

    public static By getLocator(String pageName, String key) {
        Map<String, By> pageLocators = dynamicLocators.get(pageName);
        if (pageLocators == null || !pageLocators.containsKey(key)) {
            throw new IllegalArgumentException("Locator not found for key: " + key + " on page: " + pageName);
        }
        return pageLocators.get(key);
    }

    private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException {
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("No resources found for package: " + packageName);
        }

        File directory = new File(resource.getFile());
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Directory does not exist for package: " + packageName);
        }

        List<Class<?>> classes = new ArrayList<>();
        findClasses(directory, packageName, classes);
        return classes.toArray(new Class<?>[0]);
    }

    private static void findClasses(File directory, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
    }
}
