package edu.omur.logging.model;

import edu.omur.logging.utility.Constant;
import edu.omur.logging.utility.Utility;
import lombok.Getter;
import lombok.Setter;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Getter
@Setter
public class LoggingConfig {
    private static final String LOGGING_CONFIG_FILE = "logging-context";
    private static LoggingConfig instance = null;

    private String application;
    private String environment;
    private String host;
    private String ipAddress;
    private String userId;

    // private constructor prevents any other class from instantiating.
    private LoggingConfig() {
        buildConfigData();
    }

    public synchronized static LoggingConfig getInstance() {
        if (instance == null) {
            instance = new LoggingConfig();
        }
        return instance;
    }

    private void buildConfigData() {
        PropertyResourceBundle bundle = (PropertyResourceBundle) ResourceBundle.getBundle(LOGGING_CONFIG_FILE);
        application = bundle.getString("application");
        environment = bundle.getString("environment");
        host = bundle.getString("host");
        ipAddress = bundle.getString("ipAddress");
        userId = bundle.getString("userId");
        if (Utility.isNullOrEmptyString(host)) {
            host = Utility.getHostname();
        }
        if (Utility.isNullOrEmptyString(ipAddress)) {
            ipAddress = Utility.getHostIpAddress();
        }
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s : %s%s", "application", application, Constant.NEW_LINE));
        sb.append(String.format("%-20s : %s%s", "environment", environment, Constant.NEW_LINE));
        sb.append(String.format("%-20s : %s%s", "host", host, Constant.NEW_LINE));
        sb.append(String.format("%-20s : %s%s", "ipAddress", ipAddress, Constant.NEW_LINE));
        sb.append(String.format("%-20s : %s%s", "userId", userId, Constant.NEW_LINE));
        return sb.toString();
    }
}
