package com.w3w.rm.geo.app.util;

import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ApplicationUtil {
    String WA3_PATTERN_REGEX = "^/*[^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}$";
    String LANGUAGE_ENGLISH_UK = "en";
    String COUNTRY_UK_GB = "GB";
    String LANGUAGE_WELSH_WALES = "cy";

    String ERROR_NOT_RECOGNISED_3WA = "3wa not recognised: ";
    String ERROR_MISSING_INFO_TO_CONVERT = "Missing info to fetch suggestion!";
    String ERROR_NO_SUGGESTION_AVAILABLE = "No Suggestion found for given word!";
    String ERROR_MISSING_INFO_TO_CONVERT_P1 = "Unable to fetch AutoSuggest for word:";
    String ERROR_MISSING_INFO_TO_CONVERT_P2 = "in Language:";
    String ERROR_INVALID_REQUEST = "Invalid Request!";
    String ERROR_INVALID_3WA_WORDS = "Invalid 3wa word: ";
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
    Function<String, String> INVALID_3WA_WORD_ERROR_FN = (s) -> ERROR_INVALID_3WA_WORDS  + s;
    BiFunction<String, String, String> UNABLE_TO_FETCH_AUTO_SUGGEST_FOR = (first, second) -> ERROR_MISSING_INFO_TO_CONVERT_P1  + first + ERROR_MISSING_INFO_TO_CONVERT_P2 + second;
}
