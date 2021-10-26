package com.w3w.rm.geo.app.util;

import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.AutosuggestInputType;
import com.what3words.javawrapper.request.AutosuggestRequest;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * What3WordsAPIUtil contains segregated What3WordsV3 API invocations for the given inputs.<br/>
 * They primarily delegate the API invocation for specific aspect avoiding code duplication.<br/>
 * This Utils also helps improve testability.
 *
 * @author Rajagopal
 */
public interface What3WordsAPIUtil {
    /**
     * Method getAutoSuggestForLanguageInCountry is used to construct the AutoSuggest Request based on the input parameters.
     *
     * @param api instance with which the method is invoked.
     * @param input string that is used to fetch autoSuggestions.
     * @param language in which the AutoSuggest should be provided.
     * @param country for which the API should be invoked.
     * @return Autosuggest based on the input String, Language and Country.
     */
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


    /**
     * Method getAutoSuggestForLanguageInCountry is used to construct the AutoSuggest Request based on the input parameters.
     *
     * @param api instance with which the method is invoked.
     * @param input string that is used to fetch autoSuggestions
     * @param language in which the AutoSuggest should be provided
     * @return Autosuggest based on the input String, Language and Country.
     */
    static Autosuggest getAutoSuggestForLanguage(What3WordsV3 api, String input, String language) {
        return getAutoSuggestForLanguageInCountry(api, input, language, "");// empty country
    }

    /**
     * Method getCoordinatesForWords is used to fetch the related CoOrdinates based on the input 3wa String.
     *
     * @param api instance with which the method is invoked.
     * @param word string that is used to fetch autoSuggestions
     * @return Optional<Coordinates> Based on whether or not the API was able to fetch CoOrdinates with the given word.
     */
    static Optional<Coordinates> getCoordinatesForWords(What3WordsV3 api, String word) {
        com.what3words.javawrapper.response.Coordinates
                coordinates = api.convertToCoordinates(word)
                .execute()
                .getCoordinates();
        if(coordinates == null) {
            return Optional.empty();
        }
        return Optional.of(new Coordinates(coordinates.getLat(), coordinates.getLng()));
    }

    /**
     * Method getWordsInLangForCoordinates is used to fetch the related 3wa String based on the input CoOrdinates.
     *
     * @param api  instance with which the method is invoked.
     * @param coordinates for which the corresponding 3wa should be fetched.
     * @param language in which the 3wa output should be provided
     * @return 3wa String matching the corresponding CoOrdinates passed.
     */
    static String getWordsInLangForCoordinates(What3WordsV3 api, Coordinates coordinates, String language) {
        return api
                .convertTo3wa(coordinates)
                .language(language)
                .execute()
                .getWords();
    }
}
