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
        String value = System.getProperty(key);
        if (value == null) value = System.getenv(key.toUpperCase(java.util.Locale.ROOT).replace('.', '_'));
        if (value == null) value = properties.getProperty(key);

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
        return url("base.url");
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

    public static String getApiBaseUrl() {
        return url("api.base.url");
    }

    public static int getApiTimeout() {
        int timeout = getRequiredInt("api.timeout");

        if (timeout < 0) {
            throw new IllegalStateException(
                    "Invalid api.timeout: value cannot be negative (" + timeout + ")"
            );
        }

        return timeout;
    }


    public static int getBrowserRecoveryTimeout() {
        int value = getRequiredInt("browser.recovery.timeout");

        if (value <= 0) {
            throw new IllegalStateException(
                    "Invalid browser.recovery.timeout: value must be positive (" + value + ")"
            );
        }

        return value;
    }

    public static int getTransientTextTimeout() {
        int value = getRequiredInt("browser.transient.text.timeout");

        if (value <= 0) {
            throw new IllegalStateException(
                    "Invalid browser.transient.text.timeout: value must be positive (" + value + ")"
            );
        }

        return value;
    }

    public static int getLogNameTimeout() {
        int value = getRequiredInt("browser.log.name.timeout");

        if (value <= 0) {
            throw new IllegalStateException(
                    "Invalid browser.log.name.timeout: value must be positive (" + value + ")"
            );
        }

        return value;
    }

    public static int getLogArchiveRetentionDays() {
        int value = getRequiredInt("logs.archive.retention.days");

        if (value <= 0) {
            throw new IllegalStateException(
                    "Invalid logs.archive.retention.days: value must be positive (" + value + ")"
            );
        }

        return value;
    }

    public static int getLogArchiveMaxSizeMb() {
        int value = getRequiredInt("logs.archive.max.size.mb");

        if (value <= 0) {
            throw new IllegalStateException(
                    "Invalid logs.archive.max.size.mb: value must be positive (" + value + ")"
            );
        }

        return value;
    }

    private static int number(String key, int minimum) {
        int value = getRequiredInt(key);
        if (value < minimum) throw new IllegalStateException(key + " must be >= " + minimum);
        return value;
    }
    private static String choice(String key, String... allowed) {
        String value = getRequiredProperty(key).toLowerCase(java.util.Locale.ROOT);
        if (!java.util.Set.of(allowed).contains(value)) throw new IllegalStateException("Invalid " + key + "; expected " + String.join(", ", allowed));
        return value;
    }
    private static String url(String key) {
        String value = getRequiredProperty(key);
        try {
            java.net.URI uri = java.net.URI.create(value);
            if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
                    || uri.getHost() == null || uri.getUserInfo() != null || uri.getFragment() != null
                    || uri.getQuery() != null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(key + " must be an absolute HTTP(S) URL without credentials, query or fragment", e);
        }
        if (key.equals("api.base.url") && !value.endsWith("/")) throw new IllegalStateException("api.base.url must end with /");
        return value;
    }
    public static String getBrowserType() { return choice("browser.type", "chromium", "firefox", "webkit"); }
    public static int getLaunchTimeout() { return number("browser.launch.timeout", 0); }
    public static int getNavigationTimeout() { return number("browser.navigation.timeout", 0); }
    public static int getAssertionTimeout() { return number("browser.assertion.timeout", 1); }
    public static int getSlowMo() { return number("browser.slow.mo", 0); }
    public static int getViewportWidth() { return number("browser.viewport.width", 1); }
    public static int getViewportHeight() { return number("browser.viewport.height", 1); }
    public static String getLocale() {
        String value = getRequiredProperty("browser.locale");
        try { return new java.util.Locale.Builder().setLanguageTag(value).build().toLanguageTag(); }
        catch (java.util.IllformedLocaleException e) { throw new IllegalStateException("Invalid browser.locale", e); }
    }
    public static String getTimezone() {
        String value = getRequiredProperty("browser.timezone");
        try { return java.time.ZoneId.of(value).getId(); }
        catch (java.time.DateTimeException e) { throw new IllegalStateException("Invalid browser.timezone", e); }
    }
    public static String getColorScheme() { return choice("browser.color.scheme", "light", "dark", "no-preference"); }
    public static String getLogLevel() { return choice("log.level", "off", "error", "warn", "info", "debug", "trace", "all").toUpperCase(java.util.Locale.ROOT); }
    public static java.nio.file.Path getLogDirectory() { return java.nio.file.Path.of(getRequiredProperty("logs.directory")); }
}
