package com.schneider.playground.tests;

import com.playground.action.PlaygroundAction;
import com.playground.config.EnvConfig;
import com.playground.config.PlaygroundConfig;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class PlaygroundScenarioTest extends InitializeTest {

    @Test
    public void assignmentScenario() {
        PlaygroundConfig cfg = framework.cfg();
        EnvConfig efg = framework.efg();

        PlaygroundAction action = new PlaygroundAction(context.driver()
                ,efg.getExplicitTimeoutSec())
                .open(efg.getBaseUrl())
                .setDataset("xor")
                .setNoisePercent(cfg.getNoisePercent())
                .selectExtraFeatures(cfg.getExtraFeatures())
                .removeOneNeuron(cfg.getRemoveNeuronLayer())
                .setLearningRate(cfg.getLearningRate()).clickRun();

        String initialLoss = action.readTestLoss();
        assertTrue(action.isDatasetSelected(cfg.getDataset()), "Dataset not applied correctly");
        assertTrue(action.isNoiseSetTo(cfg.getNoisePercent()), "Noise not applied correctly");
        assertTrue(action.ExtraFeaturesSelected(cfg.getExtraFeatures()), "Extra features mismatch");
        assertTrue(action.isLearningRateSet(cfg.getLearningRate()), "Learning rate mismatch");

        action.waitForEpochGreaterThan(cfg.getEpochThreshold(), efg.getExplicitTimeoutSec());
        String finalLoss = action.readTestLoss();

        final String LOSS_REGEX = "^-?\\d+(\\.\\d+)?$";
        assertTrue(finalLoss != null && finalLoss.matches(LOSS_REGEX),
                "Final loss should match " + LOSS_REGEX + " but was: " + finalLoss);
        double initial = Double.parseDouble(initialLoss);
        double finalVal = Double.parseDouble(finalLoss);
        assertTrue(finalVal <= initial, "Expected final loss to improve from initial.");
    }
}