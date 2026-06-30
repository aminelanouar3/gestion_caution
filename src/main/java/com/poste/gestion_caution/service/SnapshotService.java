package com.poste.gestion_caution.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poste.gestion_caution.dto.CautionSnapshot;
import com.poste.gestion_caution.entity.Caution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final ObjectMapper objectMapper;

    public String buildSnapshot(Caution c) {
        try {

            CautionSnapshot snapshot = new CautionSnapshot();

            snapshot.setCodeInterne(c.getCodeInterne());
            snapshot.setReference(c.getReference());
            snapshot.setMontant(c.getMontant());
            snapshot.setEtat(c.getEtat());

            snapshot.setDate(c.getDate());
            snapshot.setDateMainLevee(c.getDateMainLevee());
            snapshot.setDateRestitution(c.getDateRestitution());

            snapshot.setRemarque(c.getRemarque());

            snapshot.setBanque(
                    c.getBanque() != null ? c.getBanque().getLibelle() : null
            );

            snapshot.setFournisseur(
                    c.getFournisseur() != null ? c.getFournisseur().getLibelle() : null
            );

            snapshot.setOrdonnateur(
                    c.getOrdonnateur() != null ? c.getOrdonnateur().getLibelle() : null
            );

            return objectMapper.writeValueAsString(snapshot);

        } catch (Exception e) {
            throw new RuntimeException("Error while building snapshot", e);
        }
    }

    public CautionSnapshot readSnapshot(String json) {

        try {
            return objectMapper.readValue(json, CautionSnapshot.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while reading snapshot", e);
        }

    }
}