package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Administrateur;
import com.mercadona.promotionmanagement.core.repository.AdministrateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdministrateurService {
    @Autowired
    AdministrateurRepository administrateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Administrateur findByAdministrateur(String administrateur) {
        return administrateurRepository.findByAdministrateur(administrateur);
    }
    public void saveAdmin(String administrateur, String motDePasse) {
        String encryptedPassword = passwordEncoder.encode(motDePasse);
        Administrateur admin = new Administrateur();
        admin.setAdministrateur(administrateur);
        admin.setMotDePasse(encryptedPassword);
        administrateurRepository.save(admin);
    }

}
