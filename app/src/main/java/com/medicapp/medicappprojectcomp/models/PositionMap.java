package com.medicapp.medicappprojectcomp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PositionMap {
    private String title;
    private String content;
    private String imageBase64;
    private Double lat;
    private Double lng;
}
