package com.agricycle.model;

import com.agricycle.model.Utilisateur;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @ManyToOne
    private Utilisateur acheteur;

    @OneToOne
    private Produit produit;

    public enum Statut {
        ENCOURS,
        VALIDE,
        ANNULE,
    }
}
