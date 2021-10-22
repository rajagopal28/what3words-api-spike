package com.w3w.rm.geo.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmergencyReportsInfoDTO {
    private String message;

    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lng")
    private Double longitude;

    @JsonProperty("3wa")
    private String wa3;

    private String reportingOfficerName;
}
