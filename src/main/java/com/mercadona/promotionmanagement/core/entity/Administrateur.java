package com.mercadona.promotionmanagement.core.entity;

import javax.persistence.*;

@Entity
@Table(name = "Administrateur")
public class Administrateur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idAdmin;

    @Column(name = "administrateur")
    private String administrateur;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    /*public Administrateur(Long idAdmin, String administrateur, String motDePasse) {
        this.idAdmin = idAdmin;
        this.administrateur = administrateur;
        this.motDePasse = motDePasse;
    }*/

    public Administrateur() {
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(String administrateur) {
        this.administrateur = administrateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
