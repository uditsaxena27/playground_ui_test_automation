package com.playground.config;

import com.playground.utils.PropsHelper;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaygroundConfiguration {
    protected static final Logger log = LoggerFactory.getLogger(PlaygroundConfiguration.class);

    public static PlaygroundConfig getBaseConfiguration() throws IOException {
        Properties props = PropsHelper.loadMerged();

        String rawEnv = System.getProperty("environment");
        if (rawEnv != null && rawEnv.trim().equalsIgnoreCase("null")) rawEnv = null;

        String env = PropsHelper.toStr(
                rawEnv,
                PropsHelper.str(props, "framework.environment", "PROD")
        );
       String rawDataset = PropsHelper.envOrKey(props, env, "playground.dataset");
        if ("null".equalsIgnoreCase(rawDataset)) {
            rawDataset = null;
            log.info("Entered Dataset is null. Using default: \"Exclusive or\"");
        }

        String dataset         = PropsHelper.toStr(rawDataset, "Exclusive or");
        int noisePercent       = PropsHelper.toInt(PropsHelper.envOrKey(props, env, "playground.noisePercent"), 5);
        int extraFeatures      = PropsHelper.toInt(PropsHelper.envOrKey(props, env, "playground.extraFeatures"), 2);
        int removeNeuronLayer  = PropsHelper.toInt(PropsHelper.envOrKey(props, env, "playground.removeNeuronLayer"), 1);
        String learningRate    = PropsHelper.toStr(PropsHelper.envOrKey(props, env, "playground.learningRate"), "0.1");
        double epochThreshold  = PropsHelper.toDbl(PropsHelper.envOrKey(props, env, "playground.epochThreshold"), 0.3);

        return new PlaygroundConfig(
                dataset, noisePercent, extraFeatures, removeNeuronLayer, learningRate, epochThreshold
        );
    }
    public static EnvConfig getBaseEnvConfig() throws IOException {

        Properties props = PropsHelper.loadMerged();

        String rawEnv = System.getProperty("environment");
        if (rawEnv != null && rawEnv.trim().equalsIgnoreCase("null")) rawEnv = null;

        String env = PropsHelper.toStr(
                rawEnv,
                PropsHelper.str(props, "framework.environment", "PROD")
        );
        String baseUrl = PropsHelper.envOrKey(props, env, "framework.baseUrl");
        if (PropsHelper.isBlank(baseUrl)) {
            baseUrl = "STAGE".equalsIgnoreCase(env)
                    ? Endpoints.STAGE_BASEURL.getEndpoint()
                    : Endpoints.PROD_BASEURL.getEndpoint();
        }
        String browser   = PropsHelper.toStr (PropsHelper.envOrKey(props, env, "framework.browser"), "chrome");
        boolean headless = PropsHelper.toBool(PropsHelper.envOrKey(props, env, "framework.headless"), true);
        long implicitSec = PropsHelper.toLong(PropsHelper.envOrKey(props, env, "framework.implicitTimeoutSec"), 0);
        long explicitSec = PropsHelper.toLong(PropsHelper.envOrKey(props, env, "framework.explicitTimeoutSec"), 20);
        return new EnvConfig(
                baseUrl, browser, headless, implicitSec, explicitSec, env
        );
    }
}