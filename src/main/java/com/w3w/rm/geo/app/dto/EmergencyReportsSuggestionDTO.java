package com.w3w.rm.geo.app.dto;

import lombok.Data;

import java.util.List;

/*
 * EmergencyReportsSuggestionDTO contains the data to suggest appropriate 3wa for a given 3wa which is malformed or not grammatically correct.<br/>
 * This class acts as both request and response and the reports API just fills the missing data or suggests appropriate 3was. <br/>
 *
 * @author Rajagopal
 * */
@Data
public class EmergencyReportsSuggestionDTO {
    private String message;
    private List<SuggestionDTO> suggestions;
}
