package com.w3w.rm.geo.app.service;

import com.w3w.rm.geo.app.config.What3WordsWrapper;
import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import com.w3w.rm.geo.app.dto.EmergencyReportsSuggestionDTO;
import com.w3w.rm.geo.app.util.ApplicationUtil;
import com.what3words.javawrapper.What3WordsV3;
import com.what3words.javawrapper.request.*;
import com.what3words.javawrapper.response.Autosuggest;
import com.what3words.javawrapper.response.ConvertTo3WA;
import com.what3words.javawrapper.response.ConvertToCoordinates;
import com.what3words.javawrapper.response.Suggestion;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class What3WordsServiceTest {

    @Mock
    What3WordsWrapper what3WordsWrapper;

    @InjectMocks
    What3WordsService service;

    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_WithCoordsWithout3Wa() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        dto.setLatitude(12.12);
        dto.setLongitude(42.32);
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);

        String expected = "some.words.example";
        getMockedConvertTo3WaStubs(ApplicationUtil.LANGUAGE_ENGLISH_UK, mockApi, expected);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> actual = service.checkAndFillMissing3WaInfoForCoordinates(dto);
        Assert.assertNotNull(actual);
        EmergencyReportsInfoDTO respDto = actual.getKey();
        Assert.assertNotNull(respDto);
        Assert.assertEquals(dto, respDto);
        Assert.assertEquals(expected, respDto.getWa3());
    }

    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_WithoutCoordsWith3Wa() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        String wa3 = "some.words.example";
        dto.setWa3(wa3);
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);

        double expectedLat = 12.232;
        double expectedLong = 23.54;
        getConvertTo3waMockObjectWithTargetLang(wa3, expectedLat, expectedLong, mockApi);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> actual = service.checkAndFillMissing3WaInfoForCoordinates(dto);
        Assert.assertNotNull(actual);
        EmergencyReportsInfoDTO respDto = actual.getKey();
        Assert.assertNotNull(respDto);
        Assert.assertEquals(dto, respDto);
        Assert.assertEquals(expectedLat, respDto.getLatitude(), 0.01d);
        Assert.assertEquals(expectedLong, respDto.getLongitude(), 0.01d);
    }

    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_With3WaButNotValid3Wa() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        String wa3 = "some.words.example";
        dto.setWa3(wa3);
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);

        getMockedConvertToCoordsStubs(wa3, null, mockApi);
        List<Suggestion> suggestions = getMockAutoSuggestForLangAndString(null, wa3, ApplicationUtil.LANGUAGE_ENGLISH_UK, ApplicationUtil.COUNTRY_UK_GB, mockApi, getAutoSuggestMockWithSuggestions(Collections.singletonList(wa3+"-"+ApplicationUtil.COUNTRY_UK_GB)));

        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> actual = service.checkAndFillMissing3WaInfoForCoordinates(dto);
        Assert.assertNotNull(actual);
        EmergencyReportsInfoDTO respDto = actual.getKey();
        Assert.assertNotNull(respDto);
        EmergencyReportsSuggestionDTO reportsSuggestionDTO = actual.getValue();
        Assert.assertNotNull(reportsSuggestionDTO);
        Assert.assertEquals(dto, respDto);
        Assert.assertEquals(ApplicationUtil.NOT_RECOGNIZED_3WA_ERROR_FN.apply(wa3), reportsSuggestionDTO.getMessage());
        Assert.assertTrue(reportsSuggestionDTO.getSuggestions().stream().allMatch( s ->
            suggestions.stream().anyMatch(s2 -> s.getWords().equals(s2.getWords()) && s.getCountry().equals(s2.getCountry()) && s.getNearestPlace().equals(s2.getNearestPlace()))
        ));
    }


    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_NothingToFetch_AllFieldsSent() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        String wa3 = "some.words.example";
        dto.setWa3(wa3);
        dto.setLatitude(12.12);
        dto.setLongitude(42.32);
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> actual = service.checkAndFillMissing3WaInfoForCoordinates(dto);
        Assert.assertNotNull(actual);
        EmergencyReportsInfoDTO respDto = actual.getKey();
        Assert.assertNotNull(respDto);
        Assert.assertEquals(dto, respDto);
    }


    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_PartialCoordsSent_Latitude() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        dto.setLatitude(12.12);
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        try {
            service.checkAndFillMissing3WaInfoForCoordinates(dto);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            Assert.assertEquals("Invalid Request!", ex.getMessage());
        }
    }

    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_PartialCoordsSent_Longitude() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        dto.setLongitude(12.12);
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        try {
            service.checkAndFillMissing3WaInfoForCoordinates(dto);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            Assert.assertEquals("Invalid Request!", ex.getMessage());
        }

    }

    @Test
    public void testCheckAndFillMissing3WaInfoForCoordinates_FailureAllBlankRequest() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        dto.setMessage("Some Message");
        dto.setReportingOfficerName("Some officer Name");

        try {
            service.checkAndFillMissing3WaInfoForCoordinates(dto);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            Assert.assertEquals("Invalid Request!", ex.getMessage());
        }

    }

    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_SuccessScenario1_FirstLangIsSourceLang() {
        String source3Wa = "some.valid.word";
        String target3Wa = "2some2.2valid2.2word2";
        String sourceLang = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String targetLang = ApplicationUtil.LANGUAGE_WELSH_WALES;

        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        getMockAutoSuggestForLangAndString(null, source3Wa, sourceLang, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.singletonList(source3Wa+"-")));

        getMockedConvertTo3WaStubs(targetLang, mockApi, target3Wa);
        getConvertTo3waMockObjectWithTargetLang(source3Wa,12.341, 94.212, mockApi);

        String result = service.checkAndConvert3WaBetween2GivenLanguages(source3Wa, sourceLang, targetLang);
        Assert.assertEquals(target3Wa, result);

        Mockito.verify(mockApi).autosuggest(Mockito.eq(source3Wa));
        Mockito.verify(mockApi).convertToCoordinates(source3Wa);
    }

    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_SuccessScenario2_SecondLangIsSourceLang() {
        String source3Wa = "some.valid.word";
        String target3Wa = "2some2.2valid2.2word2";
        String first = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String second = ApplicationUtil.LANGUAGE_WELSH_WALES;

        // Here we convert from en --> cy but giving cy as first language while calling method

        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);
        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);
        getMockAutoSuggestForLangAndString(mockBuilder, source3Wa, second, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.singletonList(target3Wa+"-")));

        getMockAutoSuggestForLangAndString(mockBuilder, source3Wa, first, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.singletonList(source3Wa+"-")));

        getMockedConvertTo3WaStubs(second, mockApi, target3Wa);
        getConvertTo3waMockObjectWithTargetLang(source3Wa,12.341, 94.212, mockApi);

        String result = service.checkAndConvert3WaBetween2GivenLanguages(source3Wa, second, first);
        Assert.assertEquals(target3Wa, result);
        Mockito.verify(mockApi).convertToCoordinates(source3Wa);
        Mockito.verify(mockApi).convertTo3wa(Mockito.any(Coordinates.class));
        Mockito.verify(mockApi, Mockito.times(2)).autosuggest(Mockito.eq(source3Wa));
    }


    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_ErrorScenario1_Invalid3WaInput() {
        String input = "so12me.valid43_word";
        String first = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String second = ApplicationUtil.LANGUAGE_WELSH_WALES;

        try{
            service.checkAndConvert3WaBetween2GivenLanguages(input, first, second);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            // Assert.assertTrue(ex instanceof RuntimeException);
            Assert.assertEquals(ApplicationUtil.INVALID_3WA_WORD_ERROR_FN.apply(input), ex.getMessage());
        }
    }

    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_ErrorScenario2_FirstAutoSuggestCallFails() {
        String input = "some.valid.word";
        String first = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String second = ApplicationUtil.LANGUAGE_WELSH_WALES;
        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        getMockAutoSuggestForLangAndString(null, input, first, "", mockApi, null);
        try{
            service.checkAndConvert3WaBetween2GivenLanguages(input, first, second);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            // Assert.assertTrue(ex instanceof RuntimeException);
            Mockito.verify(mockApi).autosuggest(input);
            Assert.assertEquals(ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply(input, first), ex.getMessage());
        }
    }

    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_ErrorScenario2_EmptySuggestionsIn2ndCall() {
        String input = "some.valid.word";
        String first = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String second = ApplicationUtil.LANGUAGE_WELSH_WALES;
        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);
        getMockAutoSuggestForLangAndString(mockBuilder, input, first, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.emptyList()));
        getMockAutoSuggestForLangAndString(mockBuilder, input, second, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.emptyList()));
        try{
            service.checkAndConvert3WaBetween2GivenLanguages(input, first, second);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            // Assert.assertTrue(ex instanceof RuntimeException);
            Mockito.verify(mockApi, Mockito.times(2)).autosuggest(input);
            Assert.assertEquals(ApplicationUtil.ERROR_NO_SUGGESTION_AVAILABLE, ex.getMessage());
        }
    }


    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_ErrorScenario2_EmptyCoordinatesDueToFetchIssue() {
        String input = "some.valid.word";
        String first = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String second = ApplicationUtil.LANGUAGE_WELSH_WALES;
        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);
        getMockAutoSuggestForLangAndString(mockBuilder, input, first, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.singletonList(input)));
        getConvertTo3waMockObjectWithTargetLangForCoords(input, null, mockApi);
        try{
            service.checkAndConvert3WaBetween2GivenLanguages(input, first, second);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            // Assert.assertTrue(ex instanceof RuntimeException);
            Mockito.verify(mockApi).autosuggest(input);
            Assert.assertEquals(ApplicationUtil.ERROR_MISSING_INFO_TO_CONVERT, ex.getMessage());
        }
    }

    @Test
    public void testCheckAndConvert3WaBetween2GivenLanguages_ErrorScenario2_SecondAutoSuggestFetchNull() {
        String input = "some.valid.word";
        String first = ApplicationUtil.LANGUAGE_ENGLISH_UK;
        String second = ApplicationUtil.LANGUAGE_WELSH_WALES;
        What3WordsV3 mockApi = Mockito.mock(What3WordsV3.class);
        Mockito.when(what3WordsWrapper.getInstance()).thenReturn(mockApi);

        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);
        getMockAutoSuggestForLangAndString(mockBuilder, input, first, "", mockApi, getAutoSuggestMockWithSuggestions(Collections.emptyList()));
        getMockAutoSuggestForLangAndString(mockBuilder, input, second, "", mockApi, null);
        getConvertTo3waMockObjectWithTargetLangForCoords(input, null, mockApi);
        try{
            service.checkAndConvert3WaBetween2GivenLanguages(input, first, second);
            Assert.fail("Should not come here!");
        } catch (Exception ex) {
            // Assert.assertTrue(ex instanceof RuntimeException);
            Mockito.verify(mockApi, Mockito.times(2)).autosuggest(input);
            Assert.assertEquals(ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply(input, second), ex.getMessage());
        }
    }

    private void getMockedConvertToCoordsStubs(String wa3, com.what3words.javawrapper.response.Coordinates coordinates, What3WordsV3 mockApi) {
        ConvertToCoordinatesRequest.Builder mockBuilder2 = Mockito.mock(ConvertToCoordinatesRequest.Builder.class);
        ConvertToCoordinates mockConvToCoords = Mockito.mock(ConvertToCoordinates.class);

        Mockito.when(mockConvToCoords.getCoordinates()).thenReturn(coordinates);
        Mockito.when(mockBuilder2.execute()).thenReturn(mockConvToCoords);

        Mockito.when(mockApi.convertToCoordinates(Mockito.eq(wa3))).thenReturn(mockBuilder2);
    }

    private void getConvertTo3waMockObjectWithTargetLang(String source3Wa, double expectedLat, double expectedLong, What3WordsV3 mockApi) {
        com.what3words.javawrapper.response.Coordinates mockCoords = Mockito.mock(com.what3words.javawrapper.response.Coordinates.class);

        Mockito.when(mockCoords.getLat()).thenReturn(expectedLat);
        Mockito.when(mockCoords.getLng()).thenReturn(expectedLong);
        getConvertTo3waMockObjectWithTargetLangForCoords(source3Wa, mockCoords, mockApi);
    }

    private void getConvertTo3waMockObjectWithTargetLangForCoords(String source3Wa, com.what3words.javawrapper.response.Coordinates coords, What3WordsV3 mockApi) {
        ConvertToCoordinatesRequest.Builder mockBuilder2 = Mockito.mock(ConvertToCoordinatesRequest.Builder.class);
        ConvertToCoordinates mockConvCoords = Mockito.mock(ConvertToCoordinates.class);

        Mockito.when(mockConvCoords.getCoordinates()).thenReturn(coords);
        Mockito.when(mockBuilder2.execute()).thenReturn(mockConvCoords);

        Mockito.when(mockApi.convertToCoordinates(Mockito.eq(source3Wa))).thenReturn(mockBuilder2);
    }

    private void getMockedConvertTo3WaStubs(String language, What3WordsV3 mockApi, String  wa3) {
        ConvertTo3WA mock3WaRes = Mockito.mock(ConvertTo3WA.class);
        Mockito.when(mock3WaRes.getWords()).thenReturn(wa3);

        ConvertTo3WARequest.Builder mockBuilder3 = Mockito.mock(ConvertTo3WARequest.Builder.class);
        Mockito.when(mockBuilder3.language(Mockito.eq(language))).thenReturn(mockBuilder3);
        Mockito.when(mockBuilder3.execute()).thenReturn(mock3WaRes);

        Mockito.when(mockApi.convertTo3wa(Mockito.any(Coordinates.class))).thenReturn(mockBuilder3);
    }

    private List<Suggestion> getMockAutoSuggestForLangAndString(AutosuggestRequest.Builder mockBuilder, String source3Wa, String sourceLang,
                                                                String country, What3WordsV3 mockApi,
                                                                Autosuggest autosuggest) {
        AutosuggestRequest.Builder mockBuilder2 = Mockito.mock(AutosuggestRequest.Builder.class);
        if(mockBuilder == null) {
            mockBuilder = mockBuilder2;
        }
        Mockito.when(mockBuilder.inputType(Mockito.eq(AutosuggestInputType.GENERIC_VOICE))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.clipToCountry(Mockito.eq(country))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.language(Mockito.eq(sourceLang))).thenReturn(mockBuilder2);
        Mockito.when(mockBuilder2.execute()).thenReturn(autosuggest);

        Mockito.when(mockApi.autosuggest(Mockito.eq(source3Wa))).thenReturn(mockBuilder);

        return autosuggest != null ? autosuggest.getSuggestions() : Collections.emptyList();
    }

    private Autosuggest getAutoSuggestMockWithSuggestions(List<String> inputStrings) {
        Autosuggest mockAutoSuggest = Mockito.mock(Autosuggest.class);
    List<Suggestion> suggestionList = new ArrayList<>();
       int i=1;
       for (String in : inputStrings) {
           String nearPlace1 = "nearPlace"+i;
           Suggestion suggestion = Mockito.mock(Suggestion.class);
           Mockito.when(suggestion.getCountry()).thenReturn(in.split("-").length > 1 ? in.split("-")[1] : "");
           Mockito.when(suggestion.getNearestPlace()).thenReturn(nearPlace1);
           Mockito.when(suggestion.getWords()).thenReturn(in.split("-")[0]); // suggestion matching the input
           suggestionList.add(suggestion);
       }
        Mockito.when(mockAutoSuggest.getSuggestions()).thenReturn(suggestionList);
        return mockAutoSuggest;
    }

}