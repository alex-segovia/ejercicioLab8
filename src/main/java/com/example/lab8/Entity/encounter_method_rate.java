package com.example.lab8.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class encounter_method_rate {
    private encounter_method encounter_method;
    private List<version_details> version_details;
}
