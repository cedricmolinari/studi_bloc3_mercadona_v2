package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    // Méthode pour récupérer toutes les catégories
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    // Méthode pour trouver une catégorie par son ID
    public Categorie findById(Integer id) {
        // Utilisation de la méthode findById du repository qui retourne un Optional
        // Si l'ID n'existe pas, orElse(null) retournera null
        return categorieRepository.findById(id).orElse(null);
    };
}

