package com.playground.utils;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class PropsHelper {
    public static final String ENV_FILE = "env.properties";
    public static final String TESTDATA_FILE = "testdata.properties";

    private PropsHelper() {
    }

    public static Properties loadMerged() throws IOException {
        Properties merged = new Properties();
        loadInto(merged, ENV_FILE);
        loadInto(merged, TESTDATA_FILE);
        return merged;
    }

    static void loadInto(Properties target, String resource) throws IOException {
        try (InputStream in = PropsHelper.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IOException("Could not find " + resource + " on classpath");
            }
            target.load(in);
        }
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String str(Properties p, String key, String def) {
        String v = p.getProperty(key);
        return isBlank(v) ? def : v.trim();
    }

    public static String toStr(String v, String def) {
        return isBlank(v) ? def : v;
    }

    public static int toInt(String v, int def) {
        if (isBlank(v)) return def;
        try {
            return Integer.parseInt(v.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public static long toLong(String v, long def) {
        if (isBlank(v)) return def;
        try {
            return Long.parseLong(v.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public static double toDbl(String v, double def) {
        if (isBlank(v)) return def;
        try {
            return Double.parseDouble(v.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public static boolean toBool(String v, boolean def) {
        if (isBlank(v)) return def;
        try {
            return Boolean.parseBoolean(v.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public static String normalizeEnv(String env) {
        if (env == null) return "production";
        String e = env.trim().toLowerCase();
        if (e.equals("prod") || e.equals("production")) return "production";
        if (e.equals("stage") || e.equals("staging")) return "stage";
        return e;
    }

    public static String envOrKey(Properties p, String env, String key) {
        String e = normalizeEnv(env);
        String v = p.getProperty(e + "." + key);
        if (!isBlank(v)) return v.trim();
        v = p.getProperty(key);
        return isBlank(v) ? null : v.trim();
    }
}