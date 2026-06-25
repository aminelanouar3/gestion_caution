package com.poste.gestion_caution.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String nom;

    private String prenom;

    private String password;

    @Enumerated(EnumType.STRING)
    private EtatUtilisateur etat;

    @Enumerated(EnumType.STRING)
    private RoleUtilisateur role;
}