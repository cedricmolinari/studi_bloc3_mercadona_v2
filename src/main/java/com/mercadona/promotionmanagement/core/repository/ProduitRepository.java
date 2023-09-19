package com.mercadona.promotionmanagement.core.repository;

import com.mercadona.promotionmanagement.core.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    @Query(value = "SELECT RIGHT(reference, 6) FROM produit WHERE reference LIKE :prefix% ORDER BY reference DESC LIMIT 1", nativeQuery = true)
    String findLatestReferenceForCategory(@Param("prefix") String prefix);
}
