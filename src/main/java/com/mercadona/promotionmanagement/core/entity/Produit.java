package com.mercadona.promotionmanagement.core.entity;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idProduit;
    @Column(name = "reference")
    private String reference;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "description")
    private String description;
    @Column(name = "prix")
    private BigDecimal prix;
    @Column(name = "chemin_image")
    private String cheminImage;
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Transient  // Annotation JPA pour indiquer que ce champ n'est pas persistant
    private String formattedDate;
    public String getFormattedDate() {
        return formattedDate;
    }
    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public Integer getIdProduit() {
        return idProduit;
    }
    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }
    public Produit() {
    }
    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
