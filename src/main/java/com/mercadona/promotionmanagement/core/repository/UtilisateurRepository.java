package com.mercadona.promotionmanagement.core.repository;

import com.mercadona.promotionmanagement.core.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByIdentifiant(String identifiant);

}
