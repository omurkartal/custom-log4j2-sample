package edu.omur.logging.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoggingModel {
    private String application;
    private String environment;
    private String host;
    private String ipAddress;
    private String userId;
    private String timestamp;
    private String type;
    private String level;
    private String logger;
    private String message;
    private Map<String, String> parameterList;

    public void addToParameterList(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key and value cannot be null!!!");
        } else if (value == null) {
            throw new IllegalArgumentException("key and value cannot be null!!!");
        } else {
            if (parameterList == null) {
                parameterList = new HashMap<>();
            }
            parameterList.put(key, value);
        }
    }
}
