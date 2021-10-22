package com.w3w.rm.geo.app.dto;

import org.junit.Assert;
import org.junit.Test;

public class EmergencyReportsInfoDTOTest {
    @Test
    public void testEmergencyReportsInfoCreation() {
        EmergencyReportsInfoDTO request = new EmergencyReportsInfoDTO();
        Assert.assertNotNull(request);
    }
}