package core;

import org.openqa.selenium.WebDriver;
import com.playground.config.PlaygroundConfig;
import com.playground.config.EnvConfig;
import com.playground.config.FrameworkConfiguration;
import java.time.Duration;

public class DriverSession implements AutoCloseable {
    private final WebDriver driver;
    private final PlaygroundConfig cfg;
    private final EnvConfig efg;

    private DriverSession(WebDriver driver, PlaygroundConfig cfg, EnvConfig efg) {
        this.driver = driver;
        this.cfg = cfg;
        this.efg = efg;
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(efg.getImplicitTimeoutSec()));
    }

    public static DriverSession create(FrameworkConfiguration frameworkCfg) {
        PlaygroundConfig c = frameworkCfg.cfg();
        EnvConfig e = frameworkCfg.efg();
        WebDriver drv = BrowserManager.create(e.getBrowser(), e.isHeadless());
        return new DriverSession(drv, c, e);
    }

    public WebDriver driver() {
        return driver;
    }

    @Override
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}