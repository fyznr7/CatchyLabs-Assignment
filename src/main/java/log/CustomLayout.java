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
        ERROR("\u001B[31;1m"),
        WARN("\u001B[33;1m"),
        INFO("\u001B[32;1m"),
        DEBUG("\u001B[36;1m"),
        TRACE("\u001B[35;1m"),
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
        String sourceInfo = getSourceInfo(event);
        String prefix = getDynamicPrefix(level, sourceInfo);

        return String.format(
                "%s%s[%s] %s %s: %s%n%s",
                prefix, color, timestamp, sourceInfo, level, message, LogLevelColor.DEFAULT.getColorCode()
        );
    }

    private String getSourceInfo(LogEvent event) {
        if (event.getSource() != null) {
            String className = event.getSource().getClassName();
            String methodName = event.getSource().getMethodName();
            int lineNumber = event.getSource().getLineNumber();
            return String.format("%s.%s(%s.java:%d)", className, methodName, className.substring(className.lastIndexOf('.') + 1), lineNumber);
        } else {
            return "UnknownSource";
        }
    }

    private String getDynamicPrefix(String level, String sourceInfo) {
        int totalLength = sourceInfo.length() + 30;
        StringBuilder builder = new StringBuilder();
        String color = LogLevelColor.getColorForLevel(level);
        builder.append(color);
        for (int i = 0; i < totalLength; i++) {
            builder.append("━");
        }
        builder.append(LogLevelColor.DEFAULT.getColorCode()).append(System.lineSeparator());
        return builder.toString();
    }

    @PluginFactory
    public static CustomLayout createLayout() {
        return new CustomLayout(Charset.defaultCharset());
    }
}
