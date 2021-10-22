package com.w3w.rm.geo.app.dto;

import com.what3words.javawrapper.response.Suggestion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuggestionDTO {
    private String country;
    private String nearestPlace;
    private String words;

    public static SuggestionDTO from(Suggestion suggestion) {
        return new SuggestionDTO(suggestion.getCountry(), suggestion.getNearestPlace(), suggestion.getWords());
    }
}
