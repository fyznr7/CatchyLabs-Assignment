package log;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Plugin(name = "CustomLayout", category = "Core", elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomLayout extends AbstractStringLayout {

    public enum LogLevelColor {
        ERROR("\u001B[91m"),
        WARN("\u001B[93m"),
        INFO("\u001B[92m"),
        DEBUG("\u001B[96m"),
        TRACE("\u001B[94m"),
        DEFAULT("\u001B[0m");

        private final String colorCode;

        LogLevelColor(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return colorCode;
        }

        public static String getColorForLevel(String level) {
            try {
                return LogLevelColor.valueOf(level.toUpperCase()).getColorCode();
            } catch (IllegalArgumentException e) {
                return DEFAULT.getColorCode();
            }
        }
    }

    protected CustomLayout(Charset charset) {
        super(charset);
    }

    @Override
    public String toSerializable(LogEvent event) {
        String timestamp = Instant.ofEpochMilli(event.getTimeMillis())
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String level = event.getLevel().toString();
        String message = event.getMessage().getFormattedMessage();
        String color = LogLevelColor.getColorForLevel(level);
        String prefix = getPrefix(level);
        return String.format("%s%s[%s] %s: %s%n%s", prefix, color, timestamp, level, message, LogLevelColor.DEFAULT.getColorCode());
    }

    private String getPrefix(String level) {
        StringBuilder builder = new StringBuilder();
        String color = LogLevelColor.getColorForLevel(level);
        for (int i = 0; i < Thread.currentThread().getStackTrace().length - 1; i++) {
            builder.append(color).append("━").append(LogLevelColor.DEFAULT.getColorCode());
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    @PluginFactory
    public static CustomLayout createLayout() {
        return new CustomLayout(Charset.defaultCharset());
    }
}
