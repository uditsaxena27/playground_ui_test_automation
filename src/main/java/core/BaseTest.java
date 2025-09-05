package core;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    protected final Properties env = new Properties();
    protected final Properties data = new Properties();

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        try (InputStream inEnv = Files.newInputStream(Paths.get("src/test/resources/config/env.properties"));
             InputStream inData = Files.newInputStream(Paths.get("src/test/resources/config/testdata.properties"))) {
            env.load(inEnv);
            data.load(inData);
        }

        String browser = System.getProperty("browser", env.getProperty("browser", "chrome"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", env.getProperty("headless", "true")));
        long implicit = Long.parseLong(env.getProperty("implicitTimeoutSec", "0"));

        driver = BrowserManager.create(browser, headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}