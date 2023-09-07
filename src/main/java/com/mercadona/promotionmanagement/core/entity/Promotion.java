package com.mercadona.promotionmanagement.core.entity;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idPromotion;
    @Column(name = "date_debut")
    private String dateDebut;
    @Column(name = "date_fin")
    private String dateFin;
    @Column(name = "pourcentage")
    private BigDecimal pourcentage;

    @Column(name = "produit_id")
    private String produitId;

    public Promotion(Long idPromotion, String dateDebut, String dateFin, BigDecimal pourcentage, String produitId) {
        this.idPromotion = idPromotion;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.pourcentage = pourcentage;
        this.produitId = produitId;
    }

    public Promotion() {
    }

    public Long getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(Long idPromotion) {
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

    public String getProduitId() {
        return produitId;
    }

    public void setProduitId(String produitId) {
        this.produitId = produitId;
    }
}
