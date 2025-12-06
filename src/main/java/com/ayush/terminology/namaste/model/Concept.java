package com.ayush.terminology.namaste.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Entity
@Table(name = "concepts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Concept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code; // "N123", "5A11", "TM2-456"

    @Column(nullable = false)
    private String displayName; // "Type 2 diabetes mellitus"

    private String description;

    @ManyToOne
    @JoinColumn(name = "code_system_id", nullable = false)
    private CodeSystem codeSystem; // Link to CodeSystem

    @Builder.Default
    private Boolean isActive = true;
}
