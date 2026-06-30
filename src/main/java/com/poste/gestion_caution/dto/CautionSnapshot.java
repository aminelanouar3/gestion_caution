package com.poste.gestion_caution.dto;

import com.poste.gestion_caution.entity.EtatCaution;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CautionSnapshot {

    private Long codeInterne;
    private String reference;
    private Double montant;

    private EtatCaution etat;

    private LocalDate date;
    private LocalDate dateMainLevee;
    private LocalDate dateRestitution;

    private String remarque;

    private String banque;
    private String fournisseur;
    private String ordonnateur;
}