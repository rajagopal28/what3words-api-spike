package com.w3w.rm.geo.app.controller;

import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import com.w3w.rm.geo.app.dto.EmergencyReportsSuggestionDTO;
import com.w3w.rm.geo.app.dto.WelshLangConvertDTO;
import com.w3w.rm.geo.app.service.What3WordsService;
import com.w3w.rm.geo.app.util.ApplicationUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class GeoLocationControllerTest {

    @Mock
    What3WordsService service;

    @InjectMocks
    GeoLocationController controller;

    @Test
    public void testWelshConvertHappyFlow() {
        String input = "some.input.value";
        String expected = "new.output.value";
        Mockito.when(service.checkAndConvert3WaBetween2GivenLanguages(input, ApplicationUtil.LANGUAGE_ENGLISH_UK, ApplicationUtil.LANGUAGE_WELSH_WALES)).thenReturn(expected);

        WelshLangConvertDTO request = new WelshLangConvertDTO();
        request.setWa3(input);
        ResponseEntity<WelshLangConvertDTO> responseEntity = controller.getWelsh3WAOrLocation(request);
        Assert.assertNotNull(responseEntity);
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(expected, responseEntity.getBody().getWa3());
    }

    @Test
    public void testEmergencyResponseHappyFlow_Scenario1_FoundMatchingInfo() {
        EmergencyReportsInfoDTO infoDTO = new EmergencyReportsInfoDTO();
        infoDTO.setLongitude(13.23);
        infoDTO.setLatitude(42.53);
        infoDTO.setMessage("some message");
        infoDTO.setWa3("some.message.word");
        infoDTO.setReportingOfficerName("Reporing Office");
        Map<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> map = Map.of(infoDTO, new EmergencyReportsSuggestionDTO());
        Mockito.when(service.checkAndFillMissing3WaInfoForCoordinates(Mockito.eq(infoDTO))).thenReturn(map.entrySet().stream().findFirst().get());

        ResponseEntity<Object> responseEntity = controller.getEmergencyReportWith3Wa(infoDTO);
        Assert.assertNotNull(responseEntity);
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertTrue(responseEntity.getBody() instanceof EmergencyReportsInfoDTO);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(infoDTO, responseEntity.getBody());
    }


    @Test
    public void testEmergencyResponseHappyFlow_Scenario2_NotFoundMatchingInfo_FetchSuggestions() {
        EmergencyReportsInfoDTO infoDTO = new EmergencyReportsInfoDTO();
        EmergencyReportsSuggestionDTO suggestionDTO = new EmergencyReportsSuggestionDTO();
        Map<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> map = Map.of(infoDTO, suggestionDTO);
        Mockito.when(service.checkAndFillMissing3WaInfoForCoordinates(Mockito.eq(infoDTO))).thenReturn(map.entrySet().stream().findFirst().get());

        ResponseEntity<Object> responseEntity = controller.getEmergencyReportWith3Wa(infoDTO);
        Assert.assertNotNull(responseEntity);
        Assert.assertNotNull(responseEntity.getBody());
        Assert.assertTrue(responseEntity.getBody() instanceof EmergencyReportsSuggestionDTO);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(suggestionDTO, responseEntity.getBody());
    }

}