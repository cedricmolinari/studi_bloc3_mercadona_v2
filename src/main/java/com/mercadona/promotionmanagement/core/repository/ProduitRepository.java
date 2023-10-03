package com.mercadona.promotionmanagement.core.repository;

import com.mercadona.promotionmanagement.core.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {  // Hérite de JpaRepository pour fournir des méthodes CRUD

    // Requête SQL pour trouver la dernière référence pour une catégorie donnée
    @Query(value = "SELECT RIGHT(reference, 6) FROM produit WHERE reference LIKE :prefix% ORDER BY reference DESC LIMIT 1", nativeQuery = true)
    String findLatestReferenceForCategory(@Param("prefix") String prefix);

    // Recherche des produits par leur libellé de catégorie
    List<Produit> findByCategorieLibelle(String libelle);

    // Requête SQL pour récupérer tous les produits avec leurs catégories
    @Query("SELECT p FROM Produit p JOIN FETCH p.categorie")
    List<Produit> findAllWithCategories();

    // Requête SQL pour trouver des produits par l'ID de leur catégorie
    @Query("SELECT p FROM Produit p WHERE p.categorie.idCategorie = :idCategorie")
    List<Produit> findByCategorieId(@Param("idCategorie") Integer idCategorie);
}
