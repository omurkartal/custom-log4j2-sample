package edu.omur.logging.layout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.omur.logging.model.LoggingConfig;
import edu.omur.logging.model.LoggingModel;
import edu.omur.logging.model.LoggingType;
import edu.omur.logging.utility.Constant;
import edu.omur.logging.utility.Utility;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

@Plugin(name = "CustomJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE)
public class CustomJsonLayout extends AbstractStringLayout {
    final private static Gson gson;
    final private static GsonBuilder gsonBuilder;

    static {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public CustomJsonLayout(Configuration config, Charset charset) {
        super(config, charset, null, null);
    }

    @PluginFactory
    public static CustomJsonLayout createLayout(@PluginConfiguration final Configuration config
            , @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset) {
        return new CustomJsonLayout(config, charset);
    }

    protected CustomJsonLayout(Configuration config, Charset aCharset, Serializer headerSerializer, Serializer footerSerializer) {
        super(config, aCharset, headerSerializer, footerSerializer);
    }

    @Override
    public String toSerializable(LogEvent logEvent) {
        try {
            LoggingModel logData = new LoggingModel();

            // fields set from LoggingConfig object
            // As an alternative way ThreadContext may be used. ThreadContext data can be accessed via logEvent.getContextData()...
            logData.setApplication(LoggingConfig.getInstance().getApplication());
            logData.setEnvironment(LoggingConfig.getInstance().getEnvironment());
            logData.setHost(LoggingConfig.getInstance().getHost());
            logData.setIpAddress(LoggingConfig.getInstance().getIpAddress());
            logData.setUserId(LoggingConfig.getInstance().getUserId());

            logData.setTimestamp(Utility.getGmtTimestampAsString());
            logData.setType(LoggingType.Application.toString());
            logData.setLevel(logEvent.getLevel().toString());
            logData.setLogger(logEvent.getLoggerName());
            logData.setMessage(logEvent.getMessage().getFormattedMessage());

            // Only Map and LoggingType types are accepted as an extra parameter. (
            logData.setParameterList(null);
            Object[] logParameters = logEvent.getMessage().getParameters();
            if (logParameters != null) {
                for (Object parameter : logParameters) {
                    if (parameter != null) {
                        if (parameter instanceof Map) {
                            Map map = (Map) parameter;
                            map.forEach((key, value) -> {
                                if ((key != null) && (value != null)) {
                                    logData.addToParameterList(key.toString(), value.toString());
                                }
                            });
                        } else if (parameter instanceof LoggingType) {
                            LoggingType loggingType = (LoggingType) parameter;
                            if (loggingType == LoggingType.Audit) {
                                logData.setType(LoggingType.Audit.toString());
                            }
                        }
                    }
                }
            }

            // Stack trace data of exceptions are also added to the extra parameter section.
            final Throwable throwable = logEvent.getThrown();
            if (throwable != null) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                throwable.printStackTrace(printWriter);
                logData.addToParameterList(Constant.STACK_TRACE, stringWriter.toString());
            }

            return gson.toJson(logData) + Constant.NEW_LINE;

        } catch (Exception ex) {
            LOGGER.error("Could not write log event as JSON", ex);
        }
        return "";
    }
}
