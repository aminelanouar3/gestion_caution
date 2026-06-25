package com.poste.gestion_caution.dto;


import com.poste.gestion_caution.entity.RoleUtilisateur;
import lombok.Data;

@Data
public class CreateUserRequest {

    private String nom;
    private String prenom;
    private String password;
    private String confirmPassword;
    private RoleUtilisateur role;

}