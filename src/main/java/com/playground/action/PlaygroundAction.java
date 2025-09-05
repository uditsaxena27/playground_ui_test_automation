package com.playground.action;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.playground.pages.PlaygroundPage;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaygroundAction {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final PlaygroundPage page;

    public PlaygroundAction(WebDriver driver, long timeoutSec) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        this.page = new PlaygroundPage(driver);
    }

    public PlaygroundAction open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(PlaygroundPage.PLAY_PAUSE_BTN));
        return this;
    }

    public String readTestLoss() {
        return driver.findElement(PlaygroundPage.TEST_LOSS).getText().trim();
    }

    private static final Map<String, String> DATASET_MAP = new HashMap<String, String>() {{
        put("exclusive or", "xor");
        put("exclusive", "xor");
        put("xor", "xor");
        put("circle", "circle");
        put("gauss", "gauss");
        put("gaussian", "gauss");
        put("spiral", "spiral");
    }};
    public boolean isDatasetSelected(String datasetKey) {
        if (datasetKey == null) {
            throw new IllegalArgumentException("datasetKey cannot be null");
        }
        String key  = datasetKey.trim().toLowerCase();
        String data = DATASET_MAP.getOrDefault(key, key);

        WebElement thumb = wait.until(
                ExpectedConditions.presenceOfElementLocated(page.datasetThumb(data))
        );
        return thumb.getAttribute("class").contains("selected");
    }

    public PlaygroundAction setDataset(String datasetName) {
        if (datasetName == null) {
            throw new IllegalArgumentException("dataset cannot be null");
        }
        String key = datasetName.trim().toLowerCase();
        String data = DATASET_MAP.get(key);
        WebElement thumb = wait.until(ExpectedConditions.elementToBeClickable(page.datasetThumb(data)));
        jsClick(thumb);
        return this;
    }

    private int parseIntOr(String s, int def) {
        try {
            return (s == null) ? def : Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public boolean isNoiseSetTo(int expected) {
        return Integer.parseInt(page.getNoiseSlider().getAttribute("value")) == expected;
    }
    public PlaygroundAction setNoisePercent(int percent) {
        WebElement noise = wait.until(ExpectedConditions.visibilityOfElementLocated(PlaygroundPage.NOISE_SLIDER));
        int min = parseIntOr(noise.getAttribute("min"), 0);
        int max = parseIntOr(noise.getAttribute("max"), 50);
        int step = parseIntOr(noise.getAttribute("step"), 1);
        if (percent < min || percent > max) {
            throw new IllegalArgumentException("noise out of range: " + percent + " (allowed " + min + ".." + max + ")");
        }
        int offset = percent - min;
        if (step > 1 && (offset % step) != 0) {
            throw new IllegalArgumentException("noise must align to step " + step + " from " + min + " (got " + percent + ")");
        }
        String script = "const el=arguments[0],val=arguments[1];" +
                "el.value=val;" +
                "el.dispatchEvent(new Event('input',{bubbles:true}));" +
                "el.dispatchEvent(new Event('change',{bubbles:true}));";
        ((JavascriptExecutor) driver).executeScript(script, noise, percent);
        return this;
    }
    public boolean isLearningRateSet(String expected) {
        WebElement selectEl = wait.until(ExpectedConditions.visibilityOf(page.getLearningRateDropdown()));
        Select select = new Select(selectEl);
        String current = select.getFirstSelectedOption().getText().trim();
        return expected.equals(current);
    }
    public boolean ExtraFeaturesSelected(int expected) {
        return page.getActiveFeatures().size() == expected;
    }
    public PlaygroundAction selectExtraFeatures(int howMany) {
        List<WebElement> nodes = driver.findElements(PlaygroundPage.INACTIVE_FEATURES);

        if (howMany > nodes.size()) {
            throw new IllegalArgumentException("Requested " + howMany + " extra features, but only " + nodes.size() + " available.");
        }
        int picked = 0;
        for (WebElement n : nodes) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new MouseEvent('click',{bubbles:true}))", n);
            if (++picked >= howMany) break;
        }
        return this;
    }

    public PlaygroundAction removeOneNeuron(int layerIndex) {
        if (layerIndex < 0 || layerIndex > 6) {
            throw new IllegalArgumentException("Layer index must be between 0 and 5 (got " + layerIndex + ")");
        }
        List<WebElement> groups = driver.findElements(PlaygroundPage.NEURON_GROUPS);
        if (layerIndex >= groups.size()) return this;

        List<WebElement> buttons = groups.get(layerIndex).findElements(By.cssSelector("button.mdl-button"));
        if (buttons.size() >= 2) {
            WebElement minus = buttons.get(1);
            jsClick(minus);
        }
        return this;
    }

    private static final List<String> ALLOWED_LR = Arrays.asList("0.00001", "0.0001", "0.001", "0.003", "0.01", "0.03", "0.1", "0.3", "1", "3", "10");

    public PlaygroundAction setLearningRate(String value) {
        if (value == null) throw new IllegalArgumentException("learningRate cannot be null");
        if (!ALLOWED_LR.contains(value)) {
            throw new IllegalArgumentException("learningRate '" + value + "' not allowed. Allowed: " + ALLOWED_LR);
        }
        WebElement sel = wait.until(ExpectedConditions.elementToBeClickable(PlaygroundPage.LEARNING_RATE));
        sel.click();
        sel.findElement(By.xpath(".//option[normalize-space(.)='" + value + "']")).click();
        return this;
    }

    public PlaygroundAction clickRun() {
        driver.findElement(PlaygroundPage.PLAY_PAUSE_BTN).click();
        return this;
    }

    public PlaygroundAction waitForEpochGreaterThan(double threshold, long timeoutSec) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
        customWait.until(d -> {
            String raw = d.findElement(PlaygroundPage.EPOCH_COUNTER).getText().trim();
            double val = parseEpoch(raw);
            return val > threshold;
        });
        return this;
    }

    private void jsClick(WebElement el) {
        String script = "if(arguments[0].click) {"
                + "   arguments[0].click();" + "} "
                + "else {" + "   arguments[0].dispatchEvent(new MouseEvent('click',{bubbles:true}));" +
                "}";
        ((JavascriptExecutor) driver).executeScript(script, el);
    }

    private double parseEpoch(String raw) {
        if (raw == null || raw.isEmpty()) return 0.0;
        String normalized = raw.contains(",") ? raw.replace(",", ".") : raw;
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}