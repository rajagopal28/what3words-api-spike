package com.w3w.rm.geo.app.util;

import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationUtilTest {

    @Test
    public void test3WaRegexPatternValidation() {
        Assert.assertTrue(ApplicationUtil.validate3WaString("filled.count.soap"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("fdf.dfsd2321"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("fdf.dfsd.2321"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some words with space"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some words space"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some-words-without-space"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some.words.without.space"));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_SuccessScenario1() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        dto.setLatitude(10.0d);
        dto.setLongitude(12.0d);
        dto.setWa3("some.word.valid");
        // all data still valid
        Assert.assertTrue(ApplicationUtil.validateEmergencyReportsRequest(dto));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_SuccessScenario2() {
        EmergencyReportsInfoDTO dto1 = new EmergencyReportsInfoDTO();
        dto1.setLatitude(10.0d);
        dto1.setLongitude(12.0d);
        // only latitude, longitude
        Assert.assertTrue(ApplicationUtil.validateEmergencyReportsRequest(dto1));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_SuccessScenario3() {
        EmergencyReportsInfoDTO dto2 = new EmergencyReportsInfoDTO();
        dto2.setWa3("some.word.valid");

        // only 3wa
        Assert.assertTrue(ApplicationUtil.validateEmergencyReportsRequest(dto2));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_SuccessScenario4() {
        EmergencyReportsInfoDTO dto2a = new EmergencyReportsInfoDTO();
        dto2a.setWa3("some.word.valid");
        dto2a.setLatitude(12.43d);
        // 3wa with partial co-ords
        Assert.assertTrue(ApplicationUtil.validateEmergencyReportsRequest(dto2a));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_FailureScenario1() {
        EmergencyReportsInfoDTO dto3 = new EmergencyReportsInfoDTO();
        dto3.setLatitude(12.32d);
        // partial latitude Longitude
        Assert.assertFalse(ApplicationUtil.validateEmergencyReportsRequest(dto3));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_FailureScenario3() {
        EmergencyReportsInfoDTO dto4 = new EmergencyReportsInfoDTO();
        dto4.setLongitude(12.32d);
        Assert.assertFalse(ApplicationUtil.validateEmergencyReportsRequest(dto4));
    }

    @Test
    public void testEmergencyRequestInfoDTOValidation_FailureScenario4() {
        EmergencyReportsInfoDTO dto5 = new EmergencyReportsInfoDTO();
        // empty dto should fail
        Assert.assertFalse(ApplicationUtil.validateEmergencyReportsRequest(dto5));

    }

    @Test
    public void testEmergencyResponseInfoDTOValidation_SuccessScenario() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        dto.setLatitude(10.0d);
        dto.setLongitude(12.0d);
        dto.setWa3("some.word.valid");
        // all data still valid
        Assert.assertTrue(ApplicationUtil.validateEmergencyReportsResponse(dto));
    }

    @Test
    public void testEmergencyResponseInfoDTOValidation_FailureScenario() {
        EmergencyReportsInfoDTO dto5 = new EmergencyReportsInfoDTO();
        // empty dto should fail
        Assert.assertFalse(ApplicationUtil.validateEmergencyReportsResponse(dto5));
        dto5.setLatitude(12.01d);
        Assert.assertFalse(ApplicationUtil.validateEmergencyReportsResponse(dto5));
        dto5.setLongitude(null);
        dto5.setWa3("good.word.here");
        Assert.assertFalse(ApplicationUtil.validateEmergencyReportsResponse(dto5));
    }

    @Test
    public void testErrorFunctionApply_NotRecognized() {
        String actual = ApplicationUtil.NOT_RECOGNIZED_3WA_ERROR_FN.apply("some.word.here");
        Assert.assertEquals("3wa not recognised: some.word.here", actual);
    }

    @Test
    public void testErrorFunctionApply_CantFetchAutoSuggest() {
        String actual = ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply("some.word.here", "language");
        Assert.assertEquals("Unable to fetch AutoSuggest for word:some.word.herein Language:language", actual);
    }
}