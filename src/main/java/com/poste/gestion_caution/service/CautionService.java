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

    public List<Caution> search(String reference,
                                LocalDate dateFrom,
                                LocalDate dateTo,
                                EtatCaution etat) {

        return repo.findAll().stream()

                // reference filter
                .filter(c -> reference == null ||
                        c.getReference().toLowerCase().contains(reference.toLowerCase()))

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
                            LocalDate dateRestitution) {

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
            c.setDateMainLevee(dateMainLevee);
        }

        // ✅ RESTITUTION RULE
        if (newState == EtatCaution.RESTITUE) {
            if (dateRestitution == null || c.getDateMainLevee() == null) {
                throw new InvalidStateTransitionException(
                        "Date restitution et date main levée obligatoires"
                );
            }
            c.setDateRestitution(dateRestitution);
        }

        c.setEtat(newState);

        repo.save(c);
    }

    // -----------------------
    // RULE ENGINE
    // -----------------------
    private boolean isValidTransition(EtatCaution oldState, EtatCaution newState) {

        return switch (oldState) {

            case EN_COURS ->
                    newState == EtatCaution.MAIN_LEVEE
                            || newState == EtatCaution.SAISIE;

            case MAIN_LEVEE ->
                    newState == EtatCaution.RESTITUE
                            || newState == EtatCaution.SAISIE;

            case RESTITUE ->
                    newState == EtatCaution.SAISIE;

            case SAISIE ->
                    false;
        };
    }
}