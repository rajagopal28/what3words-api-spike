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
                    // ger suggestions
                    Autosuggest autoSuggestForLanguage = What3WordsAPIUtil.getAutoSuggestForLanguageInCountry(api, infoDTO.getWa3(), ApplicationUtil.LANGUAGE_ENGLISH_UK, ApplicationUtil.COUNTRY_UK_GB);
                    emergencyReportsSuggestionDTO.setMessage("3wa not recognised: " + infoDTO.getWa3());
                    List<SuggestionDTO> suggestionsList = autoSuggestForLanguage.getSuggestions().stream().map(SuggestionDTO::from).collect(Collectors.toList());
                    emergencyReportsSuggestionDTO.setSuggestions(suggestionsList);
                }
            }
        }
        Map<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> map = Map.of(infoDTO, emergencyReportsSuggestionDTO);
        return map.entrySet().stream().findFirst().get();
    }
}
