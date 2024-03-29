package com.w3w.rm.geo.app.util;

import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ApplicationUtil contains all constants and validations related to the application data.
 *
 * @author Rajagopal
 */
public interface ApplicationUtil {
    String WA3_PATTERN_REGEX = "^/*[^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}[.｡。･・︒។։။۔።।][^0-9`~!@#$%^&*()+\\-_=\\]\\[{\\}\\\\|'<,.>?/\";:£§º©®\\s]{1,}$";
    String LANGUAGE_ENGLISH_UK = "en";
    String COUNTRY_UK_GB = "GB";
    String LANGUAGE_WELSH_WALES = "cy";

    String ENDPOINT_WELSH_CONVERT_PATH = "/emergency-api/welsh-convert";
    String ENDPOINT_EMERGENCY_REPORT_PATH = "/emergency-api/reports";

    String ERROR_NOT_RECOGNISED_3WA = "3wa not recognised: ";
    String ERROR_MISSING_INFO_TO_CONVERT = "Missing info to fetch suggestion!";
    String ERROR_NO_SUGGESTION_AVAILABLE = "No Suggestion found for given word!";
    String ERROR_MISSING_INFO_TO_CONVERT_P1 = "Unable to fetch AutoSuggest for word:";
    String ERROR_MISSING_INFO_TO_CONVERT_P2 = "in Language:";
    String ERROR_INVALID_REQUEST = "Invalid Request!";

    /*
    * The validate3WaString method validated the given string against 3wa pattern.
    *
    * @param input string to be validated.
    * @return boolean indicating whether the given string is a valid 3wa String.
    * */
    static boolean validate3WaString(String input) {
        if(StringUtils.isNotBlank(input)) {
            Pattern pattern = Pattern.compile(WA3_PATTERN_REGEX);
            Matcher matcher = pattern.matcher(input);
            return matcher.find();
        }
        return false;
    }

    /*
     * The validateEmergencyReportsRequest method validated the given EmergencyReportsInfoDTO to have blank 3wa or empty CoOrdinates..
     *
     * @param EmergencyReportsInfoDTO instance to be validated.
     * @return boolean indicating whether the given EmergencyReportsInfoDTO is a valid Request to be processed.
     * */
    static boolean validateEmergencyReportsRequest(EmergencyReportsInfoDTO request) {
        return (request.getLatitude() != null &&
                request.getLongitude() != null) ||
                validate3WaString(request.getWa3());
    }

    /*
     * The validateEmergencyReportsResponse method validated the given EmergencyReportsInfoDTO has fully formed data.
     *
     * @param input EmergencyReportsInfoDTO to be validated.
     * @return boolean indicating whether the given EmergencyReportsInfoDTO has all fields not empty.
     * */
    static boolean validateEmergencyReportsResponse(EmergencyReportsInfoDTO request) {
        return (request.getLatitude() != null &&
                request.getLongitude() != null) &&
                validate3WaString(request.getWa3());
    }

    Function<String, String> NOT_RECOGNIZED_3WA_ERROR_FN = (s) -> ERROR_NOT_RECOGNISED_3WA  + s;
    BiFunction<String, String, String> UNABLE_TO_FETCH_AUTO_SUGGEST_FOR = (first, second) -> ERROR_MISSING_INFO_TO_CONVERT_P1  + first + ERROR_MISSING_INFO_TO_CONVERT_P2 + second;
}
