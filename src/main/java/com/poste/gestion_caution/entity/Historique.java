package com.poste.gestion_caution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Historique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CREATION / MODIFICATION / SUPPRESSION
    private String act;

    private Long codeInterne;

    private Integer utilisateurMatricule;

    private String machineIp;

    private LocalDateTime dateAction;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String snapshot; // JSON from SnapshotService
}