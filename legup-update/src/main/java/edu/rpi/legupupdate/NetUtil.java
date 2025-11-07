package edu.rpi.legupupdate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class NetUtil {

    public static final String ARIS_NAME = "Aris-Java";
    public static final String AUTH_BAN = "AUTH BAN";
    public static final String AUTH_FAIL = "AUTH FAIL";
    public static final String AUTH_OK = "AUTH OK";
    public static final String AUTH_ERR = "AUTH UNKNOWN_ERROR";
    public static final String AUTH_INVALID = "AUTH INVALID";
    public static final String AUTH = "AUTH";
    public static final String AUTH_PASS = "PASS";
    public static final String AUTH_ACCESS_TOKEN = "TOKEN";

    public static final String USER_STUDENT = "student";
    public static final String USER_INSTRUCTOR = "instructor";

    public static final long MAX_FILE_SIZE = 5242880; // 5MiB
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String ERROR = "UNKNOWN_ERROR";
    public static final String OK = "OK";
    public static final String INVALID = "INVALID";

    public static final String INVALID_VERSION = "INVALID VERSION";
    public static final String VERSION_OK = "VERSION OK";
    public static final String DONE = "DONE";
    public static final String USER_EXISTS = "EXISTS";

    public static final String STATUS_GRADING = "Grading";
    public static final String STATUS_CORRECT = "Correct";
    public static final String STATUS_INCORRECT = "Incorrect";
    public static final String STATUS_ERROR = "Grading Error. Please Resubmit";
    public static final String STATUS_NO_SUBMISSION = "No Submissions";

    public static final int SOCKET_TIMEOUT = 15000;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter ZDT_FORMAT = DateTimeFormatter.ISO_DATE_TIME;
    public static final String TOO_LARGE = "TOO_LARGE";
    public static final String NO_DATA = "NO_DATA";
    public static final String RENAME = "RENAME";
    public static final String ADD_PROOF = "ADD_PROOF";
    public static final String REMOVE_PROOF = "REMOVE_PROOF";
    public static final String CHANGE_DUE = "CHANGE_DUE";
    public static final String UPLOAD = "UPLOAD";

    /**
     * Compares two version strings.
     * <p>
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     * <p>
     * <p>
     * <p>
     * Note: It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is numerically less than str2.
     * The result is a positive integer if str1 is numerically greater than str2.
     * The result is zero if the strings are numerically equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    public static ZonedDateTime localToUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
    }

    public static ZonedDateTime zoneToUTC(ZonedDateTime dateTime) {
        return dateTime.withZoneSameInstant(ZoneOffset.UTC);
    }

    public static LocalDateTime UTCToLocal(ZonedDateTime utcTime) {
        return utcTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Timestamp ZDTToTimestamp(ZonedDateTime zdt) {
        return new Timestamp(UTCToMilli(zoneToUTC(zdt)));
    }

    public static ZonedDateTime zoneToLocal(ZonedDateTime dateTime) {
        return dateTime.withZoneSameInstant(ZoneOffset.systemDefault());
    }

    public static long UTCToMilli(ZonedDateTime utcTime) {
        return zoneToLocal(utcTime).toInstant().toEpochMilli();
    }

}
