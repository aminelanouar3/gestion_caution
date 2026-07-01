package com.poste.gestion_caution.service;

import com.poste.gestion_caution.dto.CautionSnapshot;
import com.poste.gestion_caution.entity.Historique;
import com.poste.gestion_caution.repository.HistoriqueRepository;
import com.poste.gestion_caution.security.CustomUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoriqueService {

    private final HistoriqueRepository repo;
    private final HttpServletRequest request;
    private final SnapshotService snapshotService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper; // ✅ ADD THIS

    public void save(String action,
                     Long codeInterne,
                     Object cautionBefore,
                     Object cautionAfter) {

        CustomUserPrincipal user =
                (CustomUserPrincipal) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Integer matricule = user.getUtilisateur().getMatricule();
        String ip = request.getRemoteAddr();

        CautionSnapshot snapshot = null;

        if (cautionBefore != null) {
            snapshot = snapshotService.buildSnapshot(
                    (com.poste.gestion_caution.entity.Caution) cautionBefore
            );
        } else if (cautionAfter != null) {
            snapshot = snapshotService.buildSnapshot(
                    (com.poste.gestion_caution.entity.Caution) cautionAfter
            );
        }

        String jsonSnapshot = null;

        try {
            if (snapshot != null) {
                jsonSnapshot = objectMapper.writeValueAsString(snapshot); // ✅ FIXED
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur serialization snapshot", e);
        }

        Historique h = Historique.builder()
                .act(action)
                .codeInterne(codeInterne)
                .utilisateurMatricule(matricule)
                .machineIp(ip)
                .dateAction(LocalDateTime.now())
                .snapshot(jsonSnapshot)
                .build();

        repo.save(h);
    }
}