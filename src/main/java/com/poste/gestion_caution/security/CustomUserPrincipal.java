package com.poste.gestion_caution.security;

import com.poste.gestion_caution.entity.Utilisateur;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserPrincipal extends User {

    private final Utilisateur utilisateur;

    public CustomUserPrincipal(Utilisateur user,
                               Collection<? extends GrantedAuthority> authorities) {

        super(
                user.getMatricule().toString(),
                user.getPassword(),
                authorities
        );

        this.utilisateur = user;
    }

    public String getNomPrenom() {
        return utilisateur.getNom() + " " + utilisateur.getPrenom();
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}