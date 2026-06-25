package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.EtatUtilisateur;
import com.poste.gestion_caution.entity.Utilisateur;
import com.poste.gestion_caution.repository.UtilisateurRepository;
import com.poste.gestion_caution.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository repo;

    @Override
    public UserDetails loadUserByUsername(String matricule) throws UsernameNotFoundException {

        Utilisateur user = repo.findByMatricule(Integer.parseInt(matricule))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getEtat() == EtatUtilisateur.INACTIVE) {
            throw new DisabledException("User is inactive");
        }

        return new CustomUserPrincipal(
                user,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

}