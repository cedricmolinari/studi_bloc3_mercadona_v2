package com.mercadona.promotionmanagement.core.repository;

import com.mercadona.promotionmanagement.core.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}
