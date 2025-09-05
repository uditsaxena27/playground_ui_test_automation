package com.playground.config;

public class EnvConfig {
    private final String baseUrl;
    private final String browser;
    private final boolean headless;
    private final long implicitTimeoutSec;
    private final long explicitTimeoutSec;
    private final String environment;

    public EnvConfig(String baseUrl, String browser, boolean headless, long implicitTimeoutSec, long explicitTimeoutSec, String environment) {
        this.baseUrl = baseUrl;
        this.browser = browser;
        this.headless = headless;
        this.implicitTimeoutSec = implicitTimeoutSec;
        this.explicitTimeoutSec = explicitTimeoutSec;
        this.environment = environment;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean isHeadless() {
        return headless;
    }

    public long getImplicitTimeoutSec() {
        return implicitTimeoutSec;
    }

    public long getExplicitTimeoutSec() {
        return explicitTimeoutSec;
    }

    public String getEnvironment() {
        return environment;
    }
}