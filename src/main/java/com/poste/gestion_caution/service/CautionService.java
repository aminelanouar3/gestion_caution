package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.*;
import com.poste.gestion_caution.repository.BanqueRepository;
import com.poste.gestion_caution.repository.CautionRepository;
import com.poste.gestion_caution.repository.FournisseurRepository;
import com.poste.gestion_caution.repository.OrdonnateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.poste.gestion_caution.exception.InvalidStateTransitionException;
import com.poste.gestion_caution.entity.EtatCaution;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CautionService {

    private final CautionRepository repo;
    private final BanqueRepository banqueRepo;
    private final FournisseurRepository fournisseurRepo;
    private final OrdonnateurRepository ordonnateurRepo;

    // -----------------------
    // LIST
    // -----------------------
    public List<Caution> findAll() {
        return repo.findAll();
    }

    public List<Caution> search(Long code,
                                LocalDate dateFrom,
                                LocalDate dateTo,
                                EtatCaution etat) {

        return repo.findAll().stream()

                // reference filter
                .filter(c -> code == null ||
                        c.getCodeInterne().equals(code))

                // etat filter
                .filter(c -> etat == null || c.getEtat() == etat)

                // date from
                .filter(c -> dateFrom == null || !c.getDate().isBefore(dateFrom))

                // date to
                .filter(c -> dateTo == null || !c.getDate().isAfter(dateTo))

                .toList();
    }

    public Caution findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caution not found"));
    }
    public List<Caution> searchByCode(Long code) {

        if (code == null) {
            return repo.findAll();
        }

        return repo.findAll().stream()
                .filter(c -> c.getCodeInterne() != null
                        && c.getCodeInterne().equals(code))
                .toList();
    }
    // -----------------------
    // CREATE
    // -----------------------
    public Caution save(Caution c,
                        Integer banqueId,
                        Integer fournisseurId,
                        Integer ordonnateurId) {

        Banque b = banqueRepo.findById(banqueId).orElseThrow();
        Fournisseur f = fournisseurRepo.findById(fournisseurId).orElseThrow();
        Ordonnateur o = ordonnateurRepo.findById(ordonnateurId).orElseThrow();

        c.setBanque(b);
        c.setFournisseur(f);
        c.setOrdonnateur(o);

        c.setEtat(EtatCaution.EN_COURS);

        return repo.save(c);
    }

    // -----------------------
    // UPDATE BASIC INFO
    // -----------------------
    public Caution update(Caution c) {
        return repo.save(c);
    }

    // -----------------------
    // DELETE
    // -----------------------
    public void delete(Long id) {
        Caution c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caution introuvable"));

        repo.delete(c);
    }

    // -----------------------
    // STATE TRANSITION LOGIC
    // -----------------------
    public void changeState(Long id,
                            EtatCaution newState,
                            LocalDate dateMainLevee,
                            LocalDate dateRestitution,
                            String remarque) {

        Caution c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caution not found"));

        EtatCaution oldState = c.getEtat();

        // ❌ BLOCK FINAL STATE
        if (oldState == EtatCaution.SAISIE) {
            throw new InvalidStateTransitionException(
                    "Impossible de modifier une caution déjà clôturée (SAISIE)"
            );
        }

        // ❌ INVALID TRANSITIONS
        if (oldState == EtatCaution.EN_COURS && newState == EtatCaution.RESTITUE) {
            throw new InvalidStateTransitionException(
                    "Transition invalide : passer par MAIN_LEVEE avant RESTITUE"
            );
        }

        // ✅ MAIN LEVEE RULE
        if (newState == EtatCaution.MAIN_LEVEE) {

            if (dateMainLevee == null) {
                throw new InvalidStateTransitionException(
                        "Date Main Levée obligatoire"
                );
            }

            if (!dateMainLevee.isAfter(c.getDate())) {
                throw new InvalidStateTransitionException(
                        "La Date Main Levée doit être postérieure à la date de la caution."
                );
            }

            c.setDateMainLevee(dateMainLevee);
        }

        // ✅ RESTITUTION RULE
        if (newState == EtatCaution.RESTITUE) {

            if (c.getDateMainLevee() == null) {
                throw new InvalidStateTransitionException(
                        "La Date Main Levée est obligatoire."
                );
            }
            if (dateRestitution == null) {
                c.setDateRestitution(LocalDate.now());
            }

            c.setDateRestitution(dateRestitution);
        }
        c.setEtat(newState);
        if (remarque != null && !remarque.isBlank()) {
            c.setRemarque(remarque.trim());
        }
        repo.save(c);
    }
    public long totalCautions() {
        return repo.count();
    }

    public long totalEnCours() {
        return repo.countByEtat(EtatCaution.EN_COURS);
    }

    public long totalMainLevee() {
        return repo.countByEtat(EtatCaution.MAIN_LEVEE);
    }

    public long totalRestitue() {
        return repo.countByEtat(EtatCaution.RESTITUE);
    }

    public Double montantTotal() {
        return repo.getTotalMontant();
    }

    public List<Caution> dernieresCautions() {
        return repo.findTop5ByOrderByDateDesc();
    }
}