package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Utilisateur;
import com.mercadona.promotionmanagement.core.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String identifiant) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiant(identifiant);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("L'utilisateur " + identifiant + " n'a pas été trouvé.");
        }
        return User.builder()
                .username(utilisateur.getIdentifiant())
                .password(utilisateur.getMotDePasse())
                .roles("ADMIN")  // Vous pouvez adapter ceci selon vos besoins
                .build();
    }

    public void saveAdmin(String identifiant, String motDePasse) {
        String encryptedPassword = passwordEncoder.encode(motDePasse);
        Utilisateur admin = new Utilisateur();
        admin.setIdentifiant(identifiant);
        admin.setMotDePasse(encryptedPassword);
        utilisateurRepository.save(admin);
    }
}
