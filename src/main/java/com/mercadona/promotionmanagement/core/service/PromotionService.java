package com.mercadona.promotionmanagement.core.service;

import com.mercadona.promotionmanagement.core.entity.Promotion;
import com.mercadona.promotionmanagement.core.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public Promotion addPromotion(BigDecimal pourcentagePromo, LocalDate dateDebut, LocalDate dateFin) {
        Promotion promotion = new Promotion();
        promotion.setPourcentage(pourcentagePromo);
        promotion.setDateDebut(dateDebut);
        promotion.setDateFin(dateFin);

        return promotionRepository.save(promotion);
    }

    public Optional<Promotion> findById(Long id) {
        return promotionRepository.findById(id);
    }

    // Vous pouvez ajouter d'autres méthodes selon vos besoins, par exemple pour mettre à jour ou supprimer une promotion.
}
