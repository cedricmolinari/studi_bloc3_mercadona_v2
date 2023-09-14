package com.mercadona.promotionmanagement.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProduitForm {
    private Long idProduit;
    private String reference;
    @NotBlank(message = "Veuillez entrer un libellé")
    @Size(max = 20)
    private String libelle;
    @NotBlank(message = "Veuillez entrer une description")
    @Size(max = 255)
    private String description;
    @NotNull(message = "Le prix ne doit pas être nul")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à zéro")
    private BigDecimal prix;
    // Ce champ gère le fichier image téléchargé.
    @NotNull(message = "Veuillez fournir une image")
    private MultipartFile imageFile;

    @NotNull(message = "Veuillez choisir une catégorie")
    private Integer categorieId;

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Integer getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
}
