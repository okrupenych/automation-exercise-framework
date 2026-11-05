package framework.config;

import ch.qos.logback.core.PropertyDefinerBase;

/** Resolves logging settings through the same loader before SLF4J initializes. */
public final class LogPropertyDefiner extends PropertyDefinerBase {
    private String key;

    public void setKey(String key) { this.key = key; }

    @Override
    public String getPropertyValue() {
        return switch (key) {
            case "log.level" -> Config.getLogLevel();
            case "logs.directory" -> Config.getLogDirectory().toString();
            default -> throw new IllegalStateException("Unsupported logging property: " + key);
        };
    }
}
