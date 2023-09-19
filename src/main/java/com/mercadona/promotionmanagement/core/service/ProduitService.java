package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProduitService {
    @Autowired
    ProduitRepository produitRepository;

    public Page<Produit> list(Pageable pageable) {
        try {
            return produitRepository.findAll(pageable);
        } catch (DataAccessException e) {
            // Renvoi une page vide
            return Page.empty();
        }
    }

    public Produit findById(Integer id) {
        // Utilisez la méthode findById du ProduitRepository pour récupérer l'produit par son identifiant
        return produitRepository.findById(id).orElse(null);
    }

    public void save(Produit produit) {
        produitRepository.save(produit);
    }
    public int getLastUsedNumberFromDatabase(String categoryPrefix) {
        // Utilisez le ProduitRepository pour obtenir la dernière référence pour une catégorie spécifique
        String latestReference = produitRepository.findLatestReferenceForCategory(categoryPrefix.toUpperCase());

        // Si aucune référence n'est trouvée (c'est-à-dire pour le premier produit), renvoyez 0
        if (latestReference == null || latestReference.isEmpty()) {
            return 0;
        }

        // Sinon, extrayez le numéro de la référence.
        // Par exemple, si la référence est "CAT000002", le numéro est "000002", qui est converti en 2.
        // Prenez les caractères de la position 3 à la fin
        int lastNumber = Integer.parseInt(latestReference.substring(3, 9));

        return lastNumber;
    }


}
