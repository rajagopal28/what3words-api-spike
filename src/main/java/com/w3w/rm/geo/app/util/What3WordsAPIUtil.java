package com.w3w.rm.geo.app.util;

import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.AutosuggestInputType;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;

import java.util.Optional;

public interface What3WordsAPIUtil {
    static Autosuggest getAutoSuggestForLanguage(What3WordsV3 api, String input, String language) {
        return api
                .autosuggest(input)
                .inputType(AutosuggestInputType.GENERIC_VOICE)
                .language(language)
                .execute();
    }

    static Optional<Coordinates> getCoordinatesForWords(What3WordsV3 api, String words) {
        com.what3words.javawrapper.response.Coordinates
                coordinates = api.convertToCoordinates(words)
                .execute()
                .getCoordinates();
        if(coordinates == null) {
            return Optional.empty();
        }
        return Optional.of(new Coordinates(coordinates.getLat(), coordinates.getLng()));
    }

    static String getWordsInLangForCoordinates(What3WordsV3 api, Coordinates coordinates, String language) {
        return api
                .convertTo3wa(coordinates)
                .language(language)
                .execute()
                .getWords();
    }
}
