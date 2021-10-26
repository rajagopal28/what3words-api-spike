package com.w3w.rm.geo.app.service;

import com.w3w.rm.geo.app.config.What3WordsWrapper;
import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import com.w3w.rm.geo.app.dto.EmergencyReportsSuggestionDTO;
import com.w3w.rm.geo.app.dto.SuggestionDTO;
import com.w3w.rm.geo.app.exception.Invalid3WaWordException;
import com.w3w.rm.geo.app.exception.MissingRequiredDataException;
import com.w3w.rm.geo.app.exception.NoSuggestionsAvailableException;
import com.w3w.rm.geo.app.exception.UnableToRetrieveDataException;
import com.w3w.rm.geo.app.util.ApplicationUtil;
import com.w3w.rm.geo.app.util.What3WordsAPIUtil;
import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import com.what3words.javawrapper.response.Suggestion;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * What3WordsService is responsible for performing the requested EmergencyAlert based operation by invoking one or more What3WordsV3 API calls.
 *
 * @author Rajagopal
 * */
@Service
@Slf4j
public class What3WordsService {

    @Autowired
    What3WordsWrapper what3WordsWrapper;

    /*
    * This method CheckAndConvert3WaBetween2GivenLanguages takes care of converting the given 3wa from oneLangage to another by identifying which of the 2 languages in which the word is sent.
    * Following are the steps to identify the language and convert to the other
    * 1. Assume the given word is in firstLanguage, Make an auto suggest call in firstLanguage
    * 2. See if the given word is in the part of the suggestions received
    *   2a. If yes, then the given word is in firstLanguage hence need to convert to secondLanguage hence point the instance to AutoSuggest
    *   2b. Else the word might be in secondLanguage but should validate and fetch the appropriate suggestions and point to AutoSuggest
    * 3. For the appropriate AutoSuggest get the CoOrdinates from the given correct 3wa in that region
    * 4. From the CoOrdinates get the other language 3wa bases upon the language identified to switch/convert to.
    *
    * @param input the 3wa string which should be converted
    * @param firstLanguage that is involved in the process
    * @param secondLanguage that should be involved in the process.
    * @return the converted 3wa String in the other language or any error based up on the point of failure.
    * */
    public String checkAndConvert3WaBetween2GivenLanguages(String input, String firstLanguage, String secondLanguage) {
        if (ApplicationUtil.validate3WaString(input)) {
            String toLang = secondLanguage;
            What3WordsV3 api = what3WordsWrapper.getInstance();
            // check if the given 3wa is in firstLanguage
            Autosuggest autosuggest = What3WordsAPIUtil.getAutoSuggestForLanguage(api, input, firstLanguage);
            if (autosuggest != null ) {
                // found no suggestion 3wa matching exact given word in 1st lang ==> the word is in 2nd lang
                if(autosuggest.getSuggestions()
                            .stream()
                            .map(Suggestion::getWords)
                            .noneMatch(input::equals)) {
                    log.info("suggest 11 == " + autosuggest.getSuggestions().stream()
                        .map(s -> s.getWords() + "-" + s.getCountry() + "-" + s.getLanguage() + "-" + s.getNearestPlace())
                        .collect(Collectors.joining(" , ")));
                    // then the chosen language is wrong --> Fetch in the second language
                    log.info("Inside if Welsh!!");
                    autosuggest = What3WordsAPIUtil.getAutoSuggestForLanguage(api, input, secondLanguage);
                    toLang = firstLanguage; // the target language is 1st one as the 3wa is in 2nd lang
                }
            } else {
                throw new UnableToRetrieveDataException(input, firstLanguage);
            }
            // whichever lang the word is not in that is the target language
            if (autosuggest != null) {
                if(autosuggest.getSuggestions().isEmpty()) {
                    throw new NoSuggestionsAvailableException();
                }
                Suggestion suggestion = autosuggest.getSuggestions().get(0);
                log.info("suggest ### 33 == " + autosuggest.getSuggestions().stream()
                        .map(s -> s.getWords() + "-" + s.getCountry() + "-" + s.getLanguage() + "-" + s.getNearestPlace())
                        .collect(Collectors.joining(",")));

                Optional<Coordinates> coordinatesForWords = What3WordsAPIUtil.getCoordinatesForWords(api, suggestion.getWords());
                if (coordinatesForWords.isPresent()) {
                    return What3WordsAPIUtil.getWordsInLangForCoordinates(api,
                            coordinatesForWords.get(),
                            toLang);
                } else {
                    throw new MissingRequiredDataException(ApplicationUtil.ERROR_MISSING_INFO_TO_CONVERT);
                }
            } else {
                throw new UnableToRetrieveDataException(input, secondLanguage);
            }
        } else {
            throw new Invalid3WaWordException(input);
        }
    }

    /*
    * Method CheckAndFillMissing3WaInfoForCoordinates is used to check and fill missing emergency info or to provide suggestions for a wrongly spelled 3wa string.
    * The steps are as follows :
    * 1. Check if the request either has missing 3wa or missing Coordinates.
    *   2a. if yes, check if the request has blank 3wa then fetch the 3wa for given CoOrdinates.
    *   2b. if not, check if the request has either of the CoOrdinates missing then fetch the CoOrdinates for the given string.
    * 3. If the request is valid but the word is not grammatically correct then invoke auto suggest to give appropriate 3wa strings.
    *
    * @param EmergencyReportsInfoDTO object with either CoOrdinates or 3wa String missing.
    *
    * @return Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> containing either the filled object or auto suggestions for a wrongly spelled 3wa string.
    * */
    public Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> checkAndFillMissing3WaInfoForCoordinates(EmergencyReportsInfoDTO infoDTO) {
        EmergencyReportsSuggestionDTO emergencyReportsSuggestionDTO = new EmergencyReportsSuggestionDTO();
        if(!ApplicationUtil.validateEmergencyReportsRequest(infoDTO)) {
            throw new MissingRequiredDataException();
        } else {
            What3WordsV3 api = what3WordsWrapper.getInstance();
            if(StringUtils.isBlank(infoDTO.getWa3())) {
                String wordsInLangForCoordinates = What3WordsAPIUtil.getWordsInLangForCoordinates(api, new Coordinates(infoDTO.getLatitude(), infoDTO.getLongitude()), ApplicationUtil.LANGUAGE_ENGLISH_UK);
                infoDTO.setWa3(wordsInLangForCoordinates);
                // has latitude and longitude
            } else if(infoDTO.getLongitude() == null || infoDTO.getLatitude() == null) {
                // has 3wa string
                Optional<Coordinates> coordinatesForWords = What3WordsAPIUtil.getCoordinatesForWords(api, infoDTO.getWa3());
                if(coordinatesForWords.isPresent()) {
                    infoDTO.setLatitude(coordinatesForWords.get().getLat());
                    infoDTO.setLongitude(coordinatesForWords.get().getLng());
                } else {
                    // get suggestions
                    Autosuggest autoSuggestForLanguage = What3WordsAPIUtil.getAutoSuggestForLanguageInCountry(api, infoDTO.getWa3(), ApplicationUtil.LANGUAGE_ENGLISH_UK, ApplicationUtil.COUNTRY_UK_GB);
                    emergencyReportsSuggestionDTO.setMessage(ApplicationUtil.NOT_RECOGNIZED_3WA_ERROR_FN.apply(infoDTO.getWa3()));
                    List<SuggestionDTO> suggestionsList = autoSuggestForLanguage.getSuggestions().stream().map(SuggestionDTO::from).collect(Collectors.toList());
                    emergencyReportsSuggestionDTO.setSuggestions(suggestionsList);
                }
            }
        }
        Map<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> map = Map.of(infoDTO, emergencyReportsSuggestionDTO);
        return map.entrySet().stream().findFirst().get();
    }
}
