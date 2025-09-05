package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserManager {
    public static WebDriver create(String browser, boolean headless) {
        if (browser == null) {
            browser = "chrome";
        }
        if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefox = new FirefoxOptions();
            if (headless) {
                firefox.addArguments("-headless");
            }
            return new FirefoxDriver(firefox);
        } else {
            ChromeOptions chrome = new ChromeOptions();
            if (headless) {
                chrome.addArguments("--headless=new");
            }
            chrome.addArguments("--window-size=1600,1200");
            return new ChromeDriver(chrome);
        }
    }
}