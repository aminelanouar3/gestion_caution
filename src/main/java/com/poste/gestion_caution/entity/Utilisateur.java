package com.poste.gestion_caution.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "utilisateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matricule;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EtatUtilisateur etat;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleUtilisateur role;
}