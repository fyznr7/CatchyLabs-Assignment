package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json",
                "junit:target/cucumber-reports.xml"
        },
        features = "classpath:features",
        glue = {"stepdefinitions", "runners"},
        publish = true
)
public class TestRunner {
}
