package com.poste.gestion_caution.service;

import com.poste.gestion_caution.dto.CreateUserRequest;
import com.poste.gestion_caution.dto.UpdateUserRequest;
import com.poste.gestion_caution.entity.EtatUtilisateur;
import com.poste.gestion_caution.entity.Utilisateur;
import com.poste.gestion_caution.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    // ================= CREATE USER =================
    public Utilisateur createUser(CreateUserRequest request) {

        if (request.getNom() == null || request.getNom().isBlank())
            throw new RuntimeException("Nom obligatoire");

        if (request.getPrenom() == null || request.getPrenom().isBlank())
            throw new RuntimeException("Prénom obligatoire");

        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new RuntimeException("Mot de passe obligatoire");

        if (!request.getPassword().equals(request.getConfirmPassword()))
            throw new RuntimeException("Les mots de passe ne correspondent pas");

        if (request.getRole() == null)
            throw new RuntimeException("Rôle obligatoire");

        Utilisateur user = new Utilisateur();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setRole(request.getRole());

        // Default ACTIVE
        user.setEtat(EtatUtilisateur.ACTIVE);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return utilisateurRepository.save(user);
    }

    // ================= UPDATE USER =================
    public Utilisateur updateUser(Integer matricule, UpdateUserRequest request) {

        Utilisateur user = utilisateurRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (request.getNom() != null && !request.getNom().isBlank())
            user.setNom(request.getNom());

        if (request.getPrenom() != null && !request.getPrenom().isBlank())
            user.setPrenom(request.getPrenom());

        if (request.getRole() != null)
            user.setRole(request.getRole());

        if (request.getEtat() != null)
            user.setEtat(request.getEtat());

        if (request.getPassword() != null && !request.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(request.getPassword()));

        return utilisateurRepository.save(user);
    }

    // ================= GET ALL USERS =================
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    // ================= GET USER BY MATRICULE =================
    public Utilisateur findByMatricule(Integer matricule) {
        return utilisateurRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}