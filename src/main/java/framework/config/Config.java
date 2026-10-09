package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException("config.properties not found");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private Config() {

    }


//----Internal implementation----

    private static String getRequiredProperty(String key) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing or empty configuration property: " + key
            );
        }

        return value.trim();
    }

    private static int getRequiredInt(String key) {
        String value = getRequiredProperty(key);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Invalid integer configuration property: "
                            + key + " = " + value, e
            );
        }
    }


//----Public interface----

    public static String getBaseUrl() {
        return getRequiredProperty("base.url");
    }

    public static boolean isHeadless() {
        String value = getRequiredProperty("browser.headless");

        if (!value.equalsIgnoreCase("true")
                && !value.equalsIgnoreCase("false")) {
            throw new IllegalStateException(
                    "Invalid boolean configuration property: " + "browser.headless = " + value
            );
        }

        return Boolean.parseBoolean(value);
    }

    public static int getBrowserTimeout() {
        int timeout = getRequiredInt("browser.timeout");

        if (timeout < 0) {
            throw new IllegalStateException(
                    "Invalid browser.timeout: value cannot be negative (" + timeout + ")"
            );
        }

        return timeout;
    }

}