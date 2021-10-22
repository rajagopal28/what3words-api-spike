package com.w3w.rm.geo.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmergencyReportsSuggestionDTO {
    private String message;
    private List<SuggestionDTO> suggestions;
}
