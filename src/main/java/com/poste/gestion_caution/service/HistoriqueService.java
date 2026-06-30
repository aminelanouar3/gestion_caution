package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.*;
import com.poste.gestion_caution.repository.HistoriqueCautionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoriqueService {

    private final HistoriqueCautionRepository repository;
    private final SnapshotService snapshotService;

    // -------------------------
    // CREATE LOG
    // -------------------------
    public void logCreate(Caution caution, String utilisateur, HttpServletRequest request) {

        HistoriqueCaution h = new HistoriqueCaution();

        h.setAction(ActionHistorique.CREATE);
        h.setUtilisateur(utilisateur);
        h.setIpMachine(getClientIp(request));
        h.setDateAction(LocalDateTime.now());
        h.setCaution(caution);
        h.setOldValue(null); // CREATE = no old state

        repository.save(h);
    }

    // -------------------------
    // UPDATE LOG
    // -------------------------
    public void logUpdate(Caution oldCaution, String utilisateur, HttpServletRequest request) {

        HistoriqueCaution h = new HistoriqueCaution();

        h.setAction(ActionHistorique.UPDATE);
        h.setUtilisateur(utilisateur);
        h.setIpMachine(getClientIp(request));
        h.setDateAction(LocalDateTime.now());
        h.setCaution(oldCaution);

        h.setOldValue(snapshotService.buildSnapshot(oldCaution));

        repository.save(h);
    }

    // -------------------------
    // STATE CHANGE LOG
    // -------------------------
    public void logStateChange(Caution oldCaution, String utilisateur, HttpServletRequest request) {

        HistoriqueCaution h = new HistoriqueCaution();

        h.setAction(ActionHistorique.STATE_CHANGE);
        h.setUtilisateur(utilisateur);
        h.setIpMachine(getClientIp(request));
        h.setDateAction(LocalDateTime.now());
        h.setCaution(oldCaution);

        h.setOldValue(snapshotService.buildSnapshot(oldCaution));

        repository.save(h);
    }

    // -------------------------
    // IP HELPER
    // -------------------------
    private String getClientIp(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");

        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}