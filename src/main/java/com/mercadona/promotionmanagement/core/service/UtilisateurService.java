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

    // Injection de dépendance pour l'objet PasswordEncoder, utilisé pour le hachage des mots de passe
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Implémentation de la méthode loadUserByUsername de l'interface UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String identifiant) throws UsernameNotFoundException {
        // Recherche d'un utilisateur dans la base de données par identifiant
        Utilisateur utilisateur = utilisateurRepository.findByIdentifiant(identifiant);

        // Si l'utilisateur n'est pas trouvé, une exception est levée
        if (utilisateur == null) {
            throw new UsernameNotFoundException("L'utilisateur " + identifiant + " n'a pas été trouvé.");
        }

        // Construction et renvoi de l'objet UserDetails pour l'authentification et l'autorisation
        return User.builder()
                .username(utilisateur.getIdentifiant())
                .password(utilisateur.getMotDePasse())
                .roles("ADMIN")  // Ce rôle peut être adapté en fonction de vos besoins
                .build();
    }

    // Méthode pour sauvegarder un administrateur dans la base de données
    public void saveAdmin(String identifiant, String motDePasse) {
        // Le mot de passe est haché avant d'être sauvegardé
        String encryptedPassword = passwordEncoder.encode(motDePasse);

        // Création d'un nouvel objet Utilisateur
        Utilisateur admin = new Utilisateur();
        admin.setIdentifiant(identifiant);
        admin.setMotDePasse(encryptedPassword);

        // Sauvegarde de l'objet Utilisateur dans la base de données
        utilisateurRepository.save(admin);
    }
}
