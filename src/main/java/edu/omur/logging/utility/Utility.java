package edu.omur.logging.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Utility {
    public Utility() throws IllegalAccessException {
        throw new IllegalAccessException("This is a utility class!!!");
    }

    public static boolean isNullOrEmptyString(String inValue) {
        return (inValue == null) || (inValue.isEmpty());
    }

    public static String getHostname() {
        String hostName = "unknown-host";
        try {
            hostName = java.net.Inet4Address.getLocalHost().getHostName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hostName;
    }

    public static String getHostIpAddress() {
        String hostIpAddress = "unknown-host-ip";
        try {
            hostIpAddress = java.net.Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hostIpAddress;
    }

    public static String getGmtTimestampAsString() {
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Z"));
        return time.toString();
    }
}
