package com.playground.config;

public enum Endpoints {
    PROD_BASEURL("https://playground.tensorflow.org"),
    STAGE_BASEURL("https://playground.tensorflow.org");
    private final String baseUrl;

    Endpoints(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getEndpoint() {
        return baseUrl;
    }
}