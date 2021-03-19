package edu.omur.logging.model;

import java.util.Arrays;
import java.util.Optional;

public enum LoggingType {
    Application("Application"),
    Audit("Audit");

    private final String text;

    LoggingType(String loggingTypeText) {
        this.text = loggingTypeText;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public static LoggingType convertFromString(final String inText) {
        Optional<LoggingType> loggingType = Arrays
                .stream(LoggingType.values())
                .filter(logType -> logType.text.equalsIgnoreCase(inText.trim()))
                .findFirst();
        if (!loggingType.isPresent()) {
            throw new IllegalArgumentException("Invalid LoggingType value:" + inText);
        }
        return loggingType.get();
    }
}
