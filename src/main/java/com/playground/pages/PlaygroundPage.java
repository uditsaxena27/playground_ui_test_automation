package com.playground.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class PlaygroundPage {
    private final WebDriver driver;

    public PlaygroundPage(WebDriver driver) {
        this.driver = driver;
    }

    public static final By PLAY_PAUSE_BTN   = By.id("play-pause-button");
        public static final By TEST_LOSS        = By.id("loss-test");
        public static final By NOISE_SLIDER     = By.id("noise");
        public static final By LEARNING_RATE    = By.id("learningRate");
        public static final By EPOCH_COUNTER    = By.id("iter-number");

        public static final By INACTIVE_FEATURES         = By.cssSelector("g.node:not(.active)");
        public static final By NEURON_GROUPS    = By.cssSelector(".plus-minus-neurons");
        public static final By ACTIVE_FEATURES   = By.cssSelector("g.node.active");

        public  By datasetThumb(String data) {
            return By.cssSelector("canvas.data-thumbnail[data-dataset='" + data + "']");
        }
    public WebElement getNoiseSlider() { return driver.findElement(NOISE_SLIDER); }
    public WebElement getLearningRateDropdown() { return driver.findElement(LEARNING_RATE); }
    public List<WebElement> getActiveFeatures()   { return driver.findElements(ACTIVE_FEATURES); }
}