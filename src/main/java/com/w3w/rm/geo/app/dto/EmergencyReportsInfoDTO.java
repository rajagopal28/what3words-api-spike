package com.w3w.rm.geo.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * EmergencyReportsInfoDTO contains the data to fetch emergency reports data.<br/>
 * This class acts as both request and response and the reports API just fills the missing data or suggests appropriate 3was. <br/>
 *
 * @author Rajagopal
 * */
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
