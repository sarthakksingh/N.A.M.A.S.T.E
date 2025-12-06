package com.ayush.terminology.namaste.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_systems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // "NAMASTE", "ICD11_TM2", "ICD11_BIOMED"

    private String version;
    private String sourceUrl;
    private LocalDateTime lastUpdated;

    @Builder.Default
    private Boolean isActive = true;
}
