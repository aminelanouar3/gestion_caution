package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.Utilisateur;
import com.poste.gestion_caution.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository repo;

    @Override
    public UserDetails loadUserByUsername(String matricule) {

        Utilisateur user = repo.findByMatricule(Integer.parseInt(matricule))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getMatricule()),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}