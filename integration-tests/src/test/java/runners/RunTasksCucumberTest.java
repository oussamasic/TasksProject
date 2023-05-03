package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(publish = true,
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty",
        "json:target/cucumber-reports/sampple-report.json",
        "rerun:target/cucumber-reports/sample-rerun.txt"
    },
    features = "src/test/resources/features",
    glue = "stepDefinition")

public class RunTasksCucumberTest {
}
