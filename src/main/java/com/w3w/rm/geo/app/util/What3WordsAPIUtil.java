package com.w3w.rm.geo.app.util;

import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.AutosuggestInputType;
import com.what3words.javawrapper.request.AutosuggestRequest;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public interface What3WordsAPIUtil {
    static Autosuggest getAutoSuggestForLanguageInCountry(What3WordsV3 api, String input, String language, String country) {
        AutosuggestRequest.Builder builder = api
                .autosuggest(input)
                .inputType(AutosuggestInputType.GENERIC_VOICE)
                .language(language);
        if(StringUtils.isNotBlank(country)) {
            builder = builder.clipToCountry(country);
        }
        return builder.execute();
    }


    static Autosuggest getAutoSuggestForLanguage(What3WordsV3 api, String input, String language) {
        return getAutoSuggestForLanguageInCountry(api, input, language, "");// empty country
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
