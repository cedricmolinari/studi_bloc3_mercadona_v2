package com.mercadona.promotionmanagement.form;

import javax.validation.constraints.NotBlank;

public class LoginForm {
    private Long idUtilisateur;
    @NotBlank(message = "Veuillez entrer votre identifiant")
    private String identifiant;
    @NotBlank(message = "Veuillez entrer votre mot de passe")
    private String motDePasse;

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String password) {
        this.motDePasse = password;
    }
}
