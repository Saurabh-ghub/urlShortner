package com.encoder.urlshortner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlErrorResponseDto {
    private String status;
    private String error;
}
