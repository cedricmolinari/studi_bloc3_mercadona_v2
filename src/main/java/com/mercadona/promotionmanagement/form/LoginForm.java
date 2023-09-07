package com.mercadona.promotionmanagement.form;

import javax.validation.constraints.NotBlank;

public class LoginForm {
    private Long idAdmin;
    @NotBlank(message = "Veuillez entrer votre identifiant")
    private String administrateur;
    @NotBlank(message = "Veuillez entrer votre mot de passe")
    private String motDePasse;

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

    public void setMotDePasse(String password) {
        this.motDePasse = password;
    }
}
