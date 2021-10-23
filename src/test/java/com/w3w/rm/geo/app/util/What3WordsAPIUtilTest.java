package com.w3w.rm.geo.app.util;


import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.AutosuggestInputType;
import com.what3words.javawrapper.request.AutosuggestRequest;
import com.what3words.javawrapper.request.ConvertTo3WARequest;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.Autosuggest;
import com.what3words.javawrapper.response.ConvertTo3WA;
import com.what3words.javawrapper.response.ConvertToCoordinates;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class What3WordsAPIUtilTest {

    @Test
    public void testGetAutoSuggestForGivenLanguage() {
        String input = "some.words.here";
        com.what3words.javawrapper.request.ConvertToCoordinatesRequest.Builder mockBuilder = Mockito.mock(com.what3words.javawrapper.request.ConvertToCoordinatesRequest.Builder.class);

        com.what3words.javawrapper.response.Coordinates expected = new com.what3words.javawrapper.response.Coordinates();

        ConvertToCoordinates mockConvertToCoords = Mockito.mock(ConvertToCoordinates.class);
        Mockito.when(mockConvertToCoords.getCoordinates()).thenReturn(expected);

        Mockito.when(mockBuilder.execute()).thenReturn(mockConvertToCoords);

        What3WordsV3 api = Mockito.mock(What3WordsV3.class);
        Mockito.when(api.convertToCoordinates(input)).thenReturn(mockBuilder);

        Optional<Coordinates> actual = What3WordsAPIUtil.getCoordinatesForWords(api, input);
        Assert.assertTrue(actual.isPresent());
        Assert.assertEquals(expected.getLat(), actual.get().getLat(), 0.00d);
        Assert.assertEquals(expected.getLng(), actual.get().getLng(), 0.00d);
    }

    @Test
    public void testAutoSuggestWordsLanguage_WithCountry() {
        String input = "some.words.here";
        String language = "lang";
        String country = "country1";
        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);

        Autosuggest expected = Mockito.mock(Autosuggest.class);

        Mockito.when(mockBuilder.inputType(Mockito.any(AutosuggestInputType.class))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.language(Mockito.eq(language))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.execute()).thenReturn(expected);
        Mockito.when(mockBuilder.clipToCountry(Mockito.eq(country))).thenReturn(mockBuilder);

        What3WordsV3 api = Mockito.mock(What3WordsV3.class);
        Mockito.when(api.autosuggest(input)).thenReturn(mockBuilder);

        Autosuggest actual = What3WordsAPIUtil.getAutoSuggestForLanguageInCountry(api, input, language, country);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAutoSuggestWordsLanguage_WithoutCountry() {
        String input = "some.words.here";
        String language = "lang";
        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);

        Autosuggest expected = Mockito.mock(Autosuggest.class);

        Mockito.when(mockBuilder.inputType(Mockito.any(AutosuggestInputType.class))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.language(Mockito.eq(language))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.execute()).thenReturn(expected);

        What3WordsV3 api = Mockito.mock(What3WordsV3.class);
        Mockito.when(api.autosuggest(input)).thenReturn(mockBuilder);

        Autosuggest actual = What3WordsAPIUtil.getAutoSuggestForLanguage(api, input, language);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void testGetWordsInLangForCoordinates() {
        String expected = "some.words.here";
        String language = "lang";
        ConvertTo3WARequest.Builder mockBuilder = Mockito.mock(ConvertTo3WARequest.Builder.class);

        Coordinates mockCoordinates = new Coordinates(10.0d, 12.0d);
        ConvertTo3WA mockConvertResp = Mockito.mock(ConvertTo3WA.class);

        Mockito.when(mockConvertResp.getWords()).thenReturn(expected);

        Mockito.when(mockBuilder.language(Mockito.eq(language))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.execute()).thenReturn(mockConvertResp);

        What3WordsV3 api = Mockito.mock(What3WordsV3.class);
        Mockito.when(api.convertTo3wa(Mockito.eq(mockCoordinates))).thenReturn(mockBuilder);

        String actual = What3WordsAPIUtil.getWordsInLangForCoordinates(api, mockCoordinates, language);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

}