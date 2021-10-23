package com.w3w.rm.geo.app.controller;

import com.w3w.rm.geo.app.dto.EmergencyReportsInfoDTO;
import com.w3w.rm.geo.app.dto.EmergencyReportsSuggestionDTO;
import com.w3w.rm.geo.app.dto.WelshLangConvertDTO;
import com.w3w.rm.geo.app.service.What3WordsService;
import com.w3w.rm.geo.app.util.ApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class GeoLocationController {

    @Autowired
    What3WordsService what3WordsService;

    @RequestMapping(value = ApplicationUtil.ENDPOINT_WELSH_CONVERT_PATH, method= RequestMethod.POST)
    public ResponseEntity<WelshLangConvertDTO> getWelsh3WAOrLocation(@RequestBody WelshLangConvertDTO request) {
        String out = what3WordsService.checkAndConvert3WaBetween2GivenLanguages(request.getWa3(), ApplicationUtil.LANGUAGE_ENGLISH_UK, ApplicationUtil.LANGUAGE_WELSH_WALES);
        log.info("inside out =" + out);
        WelshLangConvertDTO response = new WelshLangConvertDTO();
        response.setWa3(out);
        return ResponseEntity.ok(response);
    }


    @RequestMapping(value = ApplicationUtil.ENDPOINT_EMERGENCY_REPORT_PATH, method= RequestMethod.POST)
    public ResponseEntity<Object> getEmergencyReportWith3Wa(@RequestBody EmergencyReportsInfoDTO request) {
        Map.Entry<EmergencyReportsInfoDTO, EmergencyReportsSuggestionDTO> entry = what3WordsService.checkAndFillMissing3WaInfoForCoordinates(request);
        EmergencyReportsInfoDTO key = entry.getKey();
        if(ApplicationUtil.validateEmergencyReportsResponse(key)) {
            return ResponseEntity.ok(key);
        }
        return ResponseEntity.ok(entry.getValue());
    }


}
