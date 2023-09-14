package com.mercadona.promotionmanagement.core.entity;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idPromotion;
    @Column(name = "date_debut")
    private String dateDebut;
    @Column(name = "date_fin")
    private String dateFin;
    @Column(name = "pourcentage")
    private BigDecimal pourcentage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    public Promotion(Integer idPromotion, String dateDebut, String dateFin, BigDecimal pourcentage, String produitId) {
        this.idPromotion = idPromotion;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.pourcentage = pourcentage;
    }

    public Promotion() {
    }

    public Integer getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(Integer idPromotion) {
        this.idPromotion = idPromotion;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public BigDecimal getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(BigDecimal pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}
