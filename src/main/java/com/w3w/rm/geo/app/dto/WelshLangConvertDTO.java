package com.w3w.rm.geo.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WelshLangConvertDTO {
    @JsonProperty("3wa")
    private String wa3;
}
