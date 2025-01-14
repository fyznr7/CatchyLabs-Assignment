package runners;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HooksStepDefinitions {
    private static final Logger logger = LogManager.getLogger(HooksStepDefinitions.class);

    Hooks hooks = new Hooks();

    @Given("Setup Driver")
    public void setBrowser(DataTable dataTable) {
        String dataTableContent = dataTableToString(dataTable);

        logger.info("Initializing driver setup with the following configuration:\n{}", dataTableContent);

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        rows.forEach(row -> {
            String browser = row.get("Browser");
            String deviceName = row.getOrDefault("DeviceName", "");
            String dimensions = row.getOrDefault("Dimensions", "");

            boolean isMobile = browser.startsWith("MWEB_");
            if (isMobile) {
                if (deviceName.isEmpty() || dimensions.isEmpty()) {
                    logger.error("DeviceName and Dimensions are required for mobile browsers but are missing. Browser: {}", browser);
                    throw new IllegalArgumentException("DeviceName and Dimensions are required for mobile browsers.");
                }
                hooks.setBrowserAndInitialize(browser, true, deviceName, dimensions);
            } else {
                hooks.setBrowserAndInitialize(browser, false, null, null);
            }
        });

        logger.info("Driver setup completed successfully.");
    }

    private String dataTableToString(DataTable dataTable) {
        logger.debug("Converting DataTable to string format.");
        List<List<String>> table = dataTable.asLists(String.class);

        String formattedTable = table.stream()
                .map(row -> String.join(" | ", row))
                .collect(Collectors.joining("\n"));

        logger.debug("DataTable converted successfully:\n{}", formattedTable);
        return formattedTable;
    }
}
