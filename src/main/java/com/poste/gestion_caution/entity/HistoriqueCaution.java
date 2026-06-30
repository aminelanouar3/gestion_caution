package com.poste.gestion_caution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueCaution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActionHistorique action;

    private String utilisateur;

    private String ipMachine;

    private LocalDateTime dateAction;

    @ManyToOne
    private Caution caution;

    @Column(columnDefinition = "TEXT")
    private String oldValue; // JSON snapshot
}