package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProduitService {
    @Autowired
    ProduitRepository produitRepository;

    public Page<Produit> listPage(Pageable pageable) {
        try {
            return produitRepository.findAll(pageable);
        } catch (DataAccessException e) {
            // Renvoi une page vide
            return Page.empty();
        }
    }

    public List<Produit> listSimple() {
        return produitRepository.findAll();
    }

    public Produit findById(Integer id) {
        // La méthode findById du ProduitRepository pour récupérer l'produit par son identifiant
        return produitRepository.findById(id).orElse(null);
    }

    public void save(Produit produit) {
        produitRepository.save(produit);
    }
    public int getLastUsedNumberFromDatabase(String categoryPrefix) {
        // Utilise le ProduitRepository pour obtenir la dernière référence pour une catégorie spécifique
        String latestReference = produitRepository.findLatestReferenceForCategory(categoryPrefix.toUpperCase());

        // Si aucune référence n'est trouvée, renvoye 0
        if (latestReference == null || latestReference.isEmpty()) {
            return 0;
        }

        // Sinon, extraire le numéro de la référence.
        // Par exemple, si la référence est "CAT000002", le numéro est "000002", qui est converti en 2.
        // Prendre les caractères de la position 3 à la fin
        int lastNumber = Integer.parseInt(latestReference.substring(3, 9));

        return lastNumber;
    }

    // Méthode pour récupérer les produits d'une catégorie spécifique
    public List<Produit> findByCategorie(String categorie) {
        return produitRepository.findByCategorieLibelle(categorie);
    }
    // Méthode pour récupérer tous les produits
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    // Méthode pour récupérer tous les produits avec leurs catégories
    public List<Produit> listWithCategories() {
        return produitRepository.findAllWithCategories();
    }

    // Méthode pour trouver des produits par l'ID de leur catégorie
    public List<Produit> findByCategorieId(Integer categorieId) {
        return produitRepository.findByCategorieId(categorieId);
    }

    // Méthode pour cloner un produit
    public Produit cloneProduit(Produit original) {
        // Ici, copie de tous les champs du produit original vers le produit copié
        Produit copy = new Produit();
        copy.setIdProduit(original.getIdProduit());
        copy.setReference(original.getReference());
        copy.setLibelle(original.getLibelle());
        copy.setDescription(original.getDescription());
        copy.setPrix(original.getPrix());
        copy.setCheminImage(original.getCheminImage());
        copy.setDateCreation(original.getDateCreation());
        copy.setCategorie(original.getCategorie());
        copy.setUtilisateur(original.getUtilisateur());
        copy.setPromotion(original.getPromotion());
        return copy;
    }

}
