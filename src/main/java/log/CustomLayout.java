package log;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;

@Plugin(name = "CustomLayout", category = "Core", elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomLayout extends AbstractStringLayout {

    protected CustomLayout(Charset charset) {
        super(charset);
    }

    @Override
    public String toSerializable(LogEvent event) {
        String message = event.getMessage().getFormattedMessage();
        String prefix = getPrefix();
        return prefix + message + "\n";
    }

    private String getPrefix() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Thread.currentThread().getStackTrace().length - 1; i++) {
            builder.append("——");
        }
        return builder.toString();
    }

    @PluginFactory
    public static CustomLayout createLayout() {
        return new CustomLayout(Charset.defaultCharset());
    }
}
