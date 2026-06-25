package com.poste.gestion_caution.dto;


import com.poste.gestion_caution.entity.EtatUtilisateur;
import com.poste.gestion_caution.entity.RoleUtilisateur;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String nom;
    private String prenom;
    private String password;
    private RoleUtilisateur role;
    private EtatUtilisateur etat;

}