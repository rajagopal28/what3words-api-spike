package com.w3w.rm.geo.app.dto;

import com.what3words.javawrapper.response.Suggestion;
import lombok.AllArgsConstructor;
import lombok.Data;

/*
* SuggestionDTO contains the suggestion of words based on the 3wa string which was not a fully formed 3wa
*
* @author Rajagopal
* */
@Data
@AllArgsConstructor
public class SuggestionDTO {
    private String country;
    private String nearestPlace;
    private String words;

    /*
    * This method is used to get SuggestionDTO object from the Suggestion class data of What3WordsV3 API response
    * @param The Suggestion Object from which the SuggestionDTO should be created
    * @return SuggestionDTO which was created
    * */
    public static SuggestionDTO from(Suggestion suggestion) {
        return new SuggestionDTO(suggestion.getCountry(), suggestion.getNearestPlace(), suggestion.getWords());
    }
}
