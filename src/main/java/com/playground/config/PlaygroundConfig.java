package com.playground.config;

public class PlaygroundConfig {
    private final String dataset;
    private final int noisePercent;
    private final int extraFeatures;
    private final int removeNeuronLayer;
    private final String learningRate;
    private final double epochThreshold;

    public PlaygroundConfig(String dataset, int noisePercent,
                            int extraFeatures,
                            int removeNeuronLayer,
                            String learningRate, double epochThreshold) {
        this.dataset = dataset;
        this.noisePercent = noisePercent;
        this.extraFeatures = extraFeatures;
        this.removeNeuronLayer = removeNeuronLayer;
        this.learningRate = learningRate;
        this.epochThreshold = epochThreshold;
    }

    public String getDataset() {
        return dataset;
    }

    public int getNoisePercent() {
        return noisePercent;
    }

    public int getExtraFeatures() {
        return extraFeatures;
    }

    public int getRemoveNeuronLayer() {
        return removeNeuronLayer;
    }

    public String getLearningRate() {
        return learningRate;
    }

    public double getEpochThreshold() {
        return epochThreshold;
    }
}