package com.w3w.rm.geo.app;

import com.w3w.rm.geo.app.util.ApplicationUtil;
import com.what3words.javawrapper.What3WordsV3;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class IntegrationTestSuite {

    @Autowired
    private GenericWebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void getContext() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void testWelshConvertFromWelshToEnglish() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_WELSH_CONVERT_PATH).
                    content("{\"3wa\": \"daring.lion.race\"}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("{\"3wa\":\"sychach.parciau.lwmpyn\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWelshConvertFromEnglishToWelsh() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_WELSH_CONVERT_PATH).
                    content("{\"3wa\":\"sychach.parciau.lwmpyn\"}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("{\"3wa\":\"daring.lion.race\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testWelshConvert_Wrong3wa() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_WELSH_CONVERT_PATH).
                    content("{\"3wa\":\"23sadas_3243.lwmpyn\"}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"3wa not recognised: 23sadas_3243.lwmpyn\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEmergencyReport_Get3WaFilledForLatLong() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_EMERGENCY_REPORT_PATH).
                    content("{" +
                            "\"message\":\"A hiker has got lost\"," +
                            "\"lat\": 51.508341," +
                            "\"lng\":-0.125499," +
                            "\"3wa\": null," +
                            "\"reportingOfficerName\": \"Joe Bloggs\"" +
                            "}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A hiker has got lost\"," +
                            "\"reportingOfficerName\":\"Joe Bloggs\"," +
                            "\"lat\":51.508341," +
                            "\"lng\":-0.125499," +
                            "\"3wa\":\"daring.lion.race\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEmergencyReport_GetLatFilledForLong3Wa() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_EMERGENCY_REPORT_PATH).
                    content("{" +
                            "\"message\":\"A hiker has got lost\"," +
                            "\"lat\": null," +
                            "\"lng\":null," +
                            "\"3wa\": \"daring.lion.race\"," +
                            "\"reportingOfficerName\": \"Joe Bloggs\"" +
                            "}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("{" +
                            "\"message\":\"A hiker has got lost\"," +
                            "\"lat\": 51.508341," +
                            "\"lng\":-0.125499," +
                            "\"3wa\": \"daring.lion.race\"," +
                            "\"reportingOfficerName\": \"Joe Bloggs\"" +
                            "}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEmergencyReport_Scenario3_AllSentInRequest() {
        try {
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_EMERGENCY_REPORT_PATH).
                    content("{" +
                            "\"message\":\"A hiker has got lost\"," +
                            "\"lat\": 51.508341," +
                            "\"lng\":-0.125499," +
                            "\"3wa\": \"daring.lion.race\"," +
                            "\"reportingOfficerName\": \"Joe Bloggs\"" +
                            "}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"A hiker has got lost\"," +
                            "\"reportingOfficerName\":\"Joe Bloggs\"," +
                            "\"lat\":51.508341," +
                            "\"lng\":-0.125499," +
                            "\"3wa\":\"daring.lion.race\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEmergencyReport_ErrorScenario_Invalid3wa_SuggestionResponse() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUtil.ENDPOINT_EMERGENCY_REPORT_PATH).
                    content("{" +
                            "\"message\":\"A hiker has got lost\"," +
                            "\"lat\": null," +
                            "\"lng\": null," +
                            "\"3wa\": \"filled.count.smap\"," +
                            "\"reportingOfficerName\": \"Joe Bloggs\"" +
                            "}")
                    .contentType(What3WordsV3.CONTENT_TYPE_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"3wa not recognised: filled.count.smap\"," +
                            "\"suggestions\":[" +
                            "{\"country\":\"GB\",\"nearestPlace\":\"Bayswater, London\",\"words\":\"filled.count.soap\"}," +
                            "{\"country\":\"GB\",\"nearestPlace\":\"Wednesfield, W. Midlands\",\"words\":\"filled.count.slap\"}," +
                            "{\"country\":\"GB\",\"nearestPlace\":\"Clayton-le-Woods, Lancs.\",\"words\":\"filled.count.same\"}" +
                            "]}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
