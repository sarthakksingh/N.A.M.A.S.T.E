package com.ayush.terminology.namaste.mappings;

import com.ayush.terminology.namaste.model.Concept;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_concept_id", nullable = false)
    private Concept fromConcept;

    @ManyToOne
    @JoinColumn(name = "to_concept_id", nullable = false)
    private Concept toConcept;

    private String mappingType; // "EQUIVALENT", "NARROWER", "BROADER"
    private Double confidence; // 0.0 to 1.0 for ML-based mappings
}

