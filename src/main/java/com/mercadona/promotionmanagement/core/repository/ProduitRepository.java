package com.mercadona.promotionmanagement.core.repository;

import com.mercadona.promotionmanagement.core.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

}
