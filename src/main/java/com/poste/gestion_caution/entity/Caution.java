package com.poste.gestion_caution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeInterne;

    // RELATIONS (NOT IDS)
    @ManyToOne
    @JoinColumn(name = "ordonnateur_id")
    private Ordonnateur ordonnateur;

    @ManyToOne
    @JoinColumn(name = "banque_id")
    private Banque banque;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    // BUSINESS FIELDS
    private LocalDate date;

    private String reference;

    private Double montant;

    private LocalDate dateMainLevee;

    private LocalDate dateRestitution;

    @Enumerated(EnumType.STRING)
    private EtatCaution etat;

    @Column(length = 255)
    private String remarque;
}