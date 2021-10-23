package com.w3w.rm.geo.app.service;

import com.w3w.rm.geo.app.config.What3WordsWrapper;
import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import com.w3w.rm.geo.app.dto.EmergencyReportsSuggestionDTO;
import com.w3w.rm.geo.app.dto.SuggestionDTO;
import com.w3w.rm.geo.app.util.ApplicationUtil;
import com.w3w.rm.geo.app.util.What3WordsAPIUtil;
import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import com.what3words.javawrapper.response.Suggestion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class What3WordsService {

    @Autowired
    What3WordsWrapper what3WordsWrapper;

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
                    System.out.println("suggest 11 == " + autosuggest.getSuggestions().stream()
                        .map(s -> s.getWords() + "-" + s.getCountry() + "-" + s.getLanguage() + "-" + s.getNearestPlace())
                        .collect(Collectors.joining(" , ")));
                    // then the chosen language is wrong --> Fetch in the second language
                    System.out.println("Inside if Welsh!!");
                    autosuggest = What3WordsAPIUtil.getAutoSuggestForLanguage(api, input, secondLanguage);
                    toLang = firstLanguage; // the target language is 1st one as the 3wa is in 2nd lang
                }
            } else {
                throw new RuntimeException(ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply(input, firstLanguage));
            }
            // whichever lang the word is not in that is the target language
            if (autosuggest != null) {
                if(autosuggest.getSuggestions().isEmpty()) {
                    throw new RuntimeException(ApplicationUtil.ERROR_NO_SUGGESTION_AVAILABLE);
                }
                Suggestion suggestion = autosuggest.getSuggestions().get(0);
                System.out.println("suggest ### 33 == " + autosuggest.getSuggestions().stream()
                        .map(s -> s.getWords() + "-" + s.getCountry() + "-" + s.getLanguage() + "-" + s.getNearestPlace())
                        .collect(Collectors.joining(",")));

                Optional<Coordinates> coordinatesForWords = What3WordsAPIUtil.getCoordinatesForWords(api, suggestion.getWords());
                if (coordinatesForWords.isPresent()) {
                    return What3WordsAPIUtil.getWordsInLangForCoordinates(api,
                            coordinatesForWords.get(),
                            toLang);
                } else {
                    throw new RuntimeException(ApplicationUtil.ERROR_MISSING_INFO_TO_CONVERT);
                }
            } else {
                throw new RuntimeException(ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply(input, secondLanguage));
            }
        } else {
            throw new RuntimeException("Invalid 3wa word : " + input);
        }
    }

    public Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> checkAndFillMissing3WaInfoForCoordinates(EmergencyReportsInfoDTO infoDTO) {
        EmergencyReportsSuggestionDTO emergencyReportsSuggestionDTO = new EmergencyReportsSuggestionDTO();
        if(!ApplicationUtil.validateEmergencyReportsRequest(infoDTO)) {
            // TODO: throw Custom exception
            throw new RuntimeException("Invalid Request!");
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
