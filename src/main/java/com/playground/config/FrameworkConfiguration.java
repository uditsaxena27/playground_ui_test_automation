package com.playground.config;

import java.io.IOException;
import java.util.Properties;

public final class FrameworkConfiguration {

    private final PlaygroundConfig cfg;
    private final EnvConfig envConfig;


    private FrameworkConfiguration(PlaygroundConfig cfg, EnvConfig envConfig) {
        this.cfg = cfg;
        this.envConfig = envConfig;
    }

    public static FrameworkConfiguration load() throws IOException {
        PlaygroundConfig base = PlaygroundConfiguration.getBaseConfiguration();
        EnvConfig envBase = PlaygroundConfiguration.getBaseEnvConfig();
        Properties sys = System.getProperties();

        String baseUrl = sys.getProperty("baseUrl");
        if (baseUrl == null || baseUrl.trim().isEmpty()) baseUrl = envBase.getBaseUrl();

        String browser = sys.getProperty("browser");
        if (browser == null || browser.trim().isEmpty()) browser = envBase.getBrowser();

        String headlessStr = sys.getProperty("headless");
        boolean headless = (headlessStr != null) ? Boolean.parseBoolean(headlessStr) : envBase.isHeadless();

        String impStr = sys.getProperty("implicitTimeoutSec");
        long implicit = (impStr != null) ? Long.parseLong(impStr) : envBase.getImplicitTimeoutSec();

        String expStr = sys.getProperty("explicitTimeoutSec");
        long explicit = (expStr != null) ? Long.parseLong(expStr) : envBase.getExplicitTimeoutSec();

        String env = sys.getProperty("environment");
        if (env != null && env.trim().equalsIgnoreCase("null")) env = null;
        if (env == null || env.trim().isEmpty()) env = envBase.getEnvironment();

        String dataset = sys.getProperty("dataset");
        if (dataset == null || dataset.trim().isEmpty()) dataset = base.getDataset();

        String noiseStr = sys.getProperty("noisePercent");
        int noise = (noiseStr != null) ? Integer.parseInt(noiseStr) : base.getNoisePercent();

        String extraStr = sys.getProperty("extraFeatures");
        int extra = (extraStr != null) ? Integer.parseInt(extraStr) : base.getExtraFeatures();

        String layerStr = sys.getProperty("removeNeuronLayer");
        int layer = (layerStr != null) ? Integer.parseInt(layerStr) : base.getRemoveNeuronLayer();

        String lr = sys.getProperty("learningRate");
        if (lr == null || lr.trim().isEmpty()) lr = base.getLearningRate();

        String epochStr = sys.getProperty("epochThreshold");
        double epoch = (epochStr != null) ? Double.parseDouble(epochStr) : base.getEpochThreshold();

        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = "STAGE".equalsIgnoreCase(env) ? Endpoints.STAGE_BASEURL.getEndpoint() : Endpoints.PROD_BASEURL.getEndpoint();
        }

        EnvConfig envCfg = new EnvConfig(baseUrl, browser, headless, implicit, explicit, env);

        PlaygroundConfig pgCfg = new PlaygroundConfig(dataset, noise, extra, layer, lr, epoch);
        return new FrameworkConfiguration(pgCfg, envCfg);
    }

    public PlaygroundConfig cfg() {
        return cfg;
    }

    public EnvConfig efg() {
        return envConfig;
    }
}