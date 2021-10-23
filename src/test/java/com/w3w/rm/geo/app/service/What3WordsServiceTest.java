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

import java.util.Arrays;
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

        ConvertTo3WARequest.Builder mockBuilder = Mockito.mock(ConvertTo3WARequest.Builder.class);

        Mockito.when(mockBuilder.language(Mockito.anyString())).thenReturn(mockBuilder);
        ConvertTo3WA mock3WaResp = Mockito.mock(ConvertTo3WA.class);

        String expected = "some.words.example";
        Mockito.when(mock3WaResp.getWords()).thenReturn(expected);
        Mockito.when(mockBuilder.execute()).thenReturn(mock3WaResp);

        Mockito.when(mockApi.convertTo3wa(Mockito.any(Coordinates.class))).thenReturn(mockBuilder);
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

        ConvertToCoordinatesRequest.Builder mockBuilder = Mockito.mock(ConvertToCoordinatesRequest.Builder.class);

        ConvertToCoordinates mockConvToCoords = Mockito.mock(ConvertToCoordinates.class);

        com.what3words.javawrapper.response.Coordinates mockCoords = Mockito.mock(com.what3words.javawrapper.response.Coordinates.class);
        double expectedLat = 12.232;
        double expectedLong = 23.54;
        Mockito.when(mockCoords.getLat()).thenReturn(expectedLat);
        Mockito.when(mockCoords.getLng()).thenReturn(expectedLong);
        Mockito.when(mockConvToCoords.getCoordinates()).thenReturn(mockCoords);

        Mockito.when(mockBuilder.execute()).thenReturn(mockConvToCoords);

        Mockito.when(mockApi.convertToCoordinates(Mockito.eq(wa3))).thenReturn(mockBuilder);
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

        ConvertToCoordinatesRequest.Builder mockBuilder2 = Mockito.mock(ConvertToCoordinatesRequest.Builder.class);
        ConvertToCoordinates mockConvToCoords = Mockito.mock(ConvertToCoordinates.class);

        Mockito.when(mockConvToCoords.getCoordinates()).thenReturn(null);
        Mockito.when(mockBuilder2.execute()).thenReturn(mockConvToCoords);
        Mockito.when(mockBuilder2.execute()).thenReturn(mockConvToCoords);

        Mockito.when(mockApi.convertToCoordinates(Mockito.eq(wa3))).thenReturn(mockBuilder2);

        AutosuggestRequest.Builder mockBuilder = Mockito.mock(AutosuggestRequest.Builder.class);

        Autosuggest mockAutoSuggest = Mockito.mock(Autosuggest.class);

        String country1 = "country1";
        String country2 = "country2";
        String nearPlace1 = "nearPlace1";
        String nearPlace2 = "nearPlace2";
        String word1 = "word1";
        String word2 = "word2";
        Suggestion suggestion1 = Mockito.mock(Suggestion.class);
        Mockito.when(suggestion1.getCountry()).thenReturn(country1);
        Mockito.when(suggestion1.getNearestPlace()).thenReturn(nearPlace1);
        Mockito.when(suggestion1.getWords()).thenReturn(word1);

        Suggestion suggestion2 = Mockito.mock(Suggestion.class);
        Mockito.when(suggestion2.getCountry()).thenReturn(country2);
        Mockito.when(suggestion2.getNearestPlace()).thenReturn(nearPlace2);
        Mockito.when(suggestion2.getWords()).thenReturn(word2);
        List<Suggestion> suggestions = Arrays.asList(suggestion1, suggestion2);
        Mockito.when(mockAutoSuggest.getSuggestions()).thenReturn(suggestions);

        Mockito.when(mockBuilder.execute()).thenReturn(mockAutoSuggest);
        Mockito.when(mockBuilder.inputType(Mockito.eq(AutosuggestInputType.GENERIC_VOICE))).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.language(Mockito.anyString())).thenReturn(mockBuilder);
        Mockito.when(mockBuilder.clipToCountry(Mockito.eq(ApplicationUtil.COUNTRY_UK_GB))).thenReturn(mockBuilder);

        Mockito.when(mockApi.autosuggest(Mockito.eq(wa3))).thenReturn(mockBuilder);
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

}