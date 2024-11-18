package com.encoder.urlshortner.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
}
