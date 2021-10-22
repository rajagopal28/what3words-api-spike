package com.w3w.rm.geo.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmergencyReportsResponse {
    private String message;

    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lon")
    private Double longitude;

    @JsonProperty("3wa")
    private String wa3;

    private String reportingOfficerName;
}
