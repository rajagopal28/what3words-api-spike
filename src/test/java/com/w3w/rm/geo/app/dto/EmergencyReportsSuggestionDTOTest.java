package com.w3w.rm.geo.app.dto;


import org.junit.Assert;
import org.junit.Test;

public class EmergencyReportsSuggestionDTOTest {

    @Test
    public void testEmergencyReportsDTOCreation() {
        EmergencyReportsInfoDTO dto = new EmergencyReportsInfoDTO();
        String words = "some.d.asd";
        double longitude = 12.323d;
        String reporting = "reportign office";
        dto.setWa3(words);
        dto.setLongitude(longitude);
        dto.setReportingOfficerName(reporting);

        Assert.assertEquals(words, dto.getWa3());
        Assert.assertEquals(reporting, dto.getReportingOfficerName());
        Assert.assertEquals(longitude, dto.getLongitude(), 0.01d);
        Assert.assertNull(dto.getLatitude());
        Assert.assertNull(dto.getMessage());
    }
}