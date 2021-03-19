package edu.omur.logging.app;

import edu.omur.logging.model.LoggingConfig;
import edu.omur.logging.model.LoggingType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TestApplication {
    private static final Logger logger = LogManager.getLogger(TestApplication.class);

    public static void main(String[] args) {
        try {
            System.out.println(LoggingConfig.getInstance().getContent());
            logger.info("sample message 1");

            Map<String, String> loggingParameters = new HashMap<>();
            loggingParameters.put("key1", "value1");
            loggingParameters.put("key2", "value2");

            logger.log(Level.INFO, "will be logged with extra parameters", loggingParameters);

            logger.log(Level.WARN, "user opened abc page...", LoggingType.Audit);

            try {
                LoggingType.convertFromString("this call will fail");
            } catch (Exception ex) {
                logger.error("LoggingType conversion error is created for test purpose", ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
