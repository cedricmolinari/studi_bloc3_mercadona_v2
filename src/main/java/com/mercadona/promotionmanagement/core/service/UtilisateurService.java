package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Utilisateur;
import com.mercadona.promotionmanagement.core.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Utilisateur findByIdentifiant(String identifiant) {
        return utilisateurRepository.findByIdentifiant(identifiant);
    }
    public void saveAdmin(String identifiant, String motDePasse) {
        String encryptedPassword = passwordEncoder.encode(motDePasse);
        Utilisateur admin = new Utilisateur();
        admin.setIdentifiant(identifiant);
        admin.setMotDePasse(encryptedPassword);
        utilisateurRepository.save(admin);
    }

}
