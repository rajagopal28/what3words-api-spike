package com.w3w.rm.geo.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * WelshLangConvertDTO contains the 3wa string in English which should be converted to Welsh or vice-versa.
 *
 * @author Rajagopal
 * */
@Data
public class WelshLangConvertDTO {
    @JsonProperty("3wa")
    private String wa3;
}
