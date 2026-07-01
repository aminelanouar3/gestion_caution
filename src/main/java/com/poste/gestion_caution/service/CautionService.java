package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.*;
import com.poste.gestion_caution.exception.InvalidEditTransitionException;
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
    private final HistoriqueService historiqueService;
    // -----------------------
    // LIST
    // -----------------------
    public List<Caution> findAll() {
        return repo.findAllByOrderByCodeInterneDesc();
    }

    public List<Caution> search(String code, String reference, EtatCaution etat) {
        return findAll().stream()
                .filter(c -> code == null || code.isBlank()
                        || c.getCodeInterne().toString().contains(code))
                .filter(c -> reference == null || reference.isBlank()
                        || c.getReference().toLowerCase().contains(reference.toLowerCase()))
                .filter(c -> etat == null || c.getEtat() == etat)
                .toList();
    }

    public Caution findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caution not found"));
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

        Caution saved = repo.save(c);

        // 🔥 HISTORY (ONLY ONE LINE)
        historiqueService.save(
                "CREATION",
                saved.getCodeInterne(),
                null,
                saved
        );

        return saved;
    }

    // -----------------------
    // UPDATE BASIC INFO
    // -----------------------
    public Caution update(Caution c,
                          Integer banqueId,
                          Integer fournisseurId,
                          Integer ordonnateurId) {

        Caution old = repo.findById(c.getCodeInterne())
                .orElseThrow(() -> new RuntimeException("Caution not found"));

        Banque b = banqueRepo.findById(banqueId).orElseThrow();
        Fournisseur f = fournisseurRepo.findById(fournisseurId).orElseThrow();
        Ordonnateur o = ordonnateurRepo.findById(ordonnateurId).orElseThrow();

        c.setBanque(b);
        c.setFournisseur(f);
        c.setOrdonnateur(o);

        Caution saved = repo.save(c);

        // 🔥 HISTORY
        historiqueService.save(
                "MODIFICATION",
                saved.getCodeInterne(),
                old,
                saved
        );

        return saved;
    }

    // -----------------------
    // DELETE
    // -----------------------
    public void delete(Long id) {
        Caution old = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Caution introuvable"));

        repo.delete(old);
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

        Caution old = repo.findById(id).orElseThrow();

        if (c.getEtat() == newState) return;

        if (c.getEtat() == EtatCaution.SAISIE) {
            throw new RuntimeException("Caution clôturée");
        }

        if (c.getEtat() == EtatCaution.EN_COURS && newState == EtatCaution.RESTITUE) {
            throw new RuntimeException("Passe par MAIN_LEVEE");
        }

        if (newState == EtatCaution.MAIN_LEVEE) {

            if (dateMainLevee == null)
                throw new RuntimeException("Date Main Levée obligatoire");

            if (!dateMainLevee.isAfter(c.getDate()))
                throw new RuntimeException("Date invalide");

            c.setDateMainLevee(dateMainLevee);
        }

        if (newState == EtatCaution.RESTITUE) {

            if (c.getDateMainLevee() == null)
                throw new RuntimeException("Main levée manquante");

            if (dateRestitution == null)
                c.setDateRestitution(LocalDate.now());
        }

        c.setEtat(newState);

        if (remarque != null) c.setRemarque(remarque);

        Caution saved = repo.save(c);

        // 🔥 HISTORY
        historiqueService.save(
                "MODIFICATION",
                saved.getCodeInterne(),
                old,
                saved
        );
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