package com.w3w.rm.geo.app.util;

import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ApplicationUtil {
    String WA3_PATTERN_REGEX = "^/*[^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}$";
    String LANGUAGE_ENGLISH_UK = "en";
    String COUNTRY_UK_GB = "GB";
    String LANGUAGE_WELSH_WALES = "cy";

    String ERROR_NOT_RECOGNISED_3WA = "3wa not recognised: ";
    static boolean validate3WaString(String input) {
        if(StringUtils.isNotBlank(input)) {
            Pattern pattern = Pattern.compile(WA3_PATTERN_REGEX);
            Matcher matcher = pattern.matcher(input);
            return matcher.find();
        }
        return false;
    }

    static boolean validateEmergencyReportsRequest(EmergencyReportsInfoDTO request) {
        return (request.getLatitude() != null &&
                request.getLongitude() != null) ||
                validate3WaString(request.getWa3());
    }

    static boolean validateEmergencyReportsResponse(EmergencyReportsInfoDTO request) {
        return (request.getLatitude() != null &&
                request.getLongitude() != null) &&
                validate3WaString(request.getWa3());
    }

    Function<String, String> NOT_RECOGNIZED_3WA_ERROR_FN = (s) -> ERROR_NOT_RECOGNISED_3WA  + s;
}
