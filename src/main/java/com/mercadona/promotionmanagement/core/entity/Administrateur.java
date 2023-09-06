package com.mercadona.promotionmanagement.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Administrateur")
public class Administrateur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idAdmin;

    @Column(name = "nom")
    private String nom;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    public Administrateur(Long idAdmin, String nom, String motDePasse) {
        this.idAdmin = idAdmin;
        this.nom = nom;
        this.motDePasse = motDePasse;
    }

    public Administrateur() {
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
