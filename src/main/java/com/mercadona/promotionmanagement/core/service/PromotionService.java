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

    // Méthode pour ajouter une promotion, marquée comme transactionnelle pour assurer l'intégrité des données
    @Transactional
    public Promotion addPromotion(BigDecimal pourcentagePromo, LocalDate dateDebut, LocalDate dateFin) {
        // Création d'une nouvelle instance de Promotion
        Promotion promotion = new Promotion();
        promotion.setPourcentage(pourcentagePromo);
        promotion.setDateDebut(dateDebut);
        promotion.setDateFin(dateFin);

        // Sauvegarde de la promotion dans la base de données et retour de l'instance sauvegardée
        return promotionRepository.save(promotion);
    }

    // Méthode pour trouver une promotion par son identifiant
    public Optional<Promotion> findById(Long id) {
        // Utilisation de la méthode findById du repository pour chercher la promotion
        // Le retour est un Optional qui peut être vide si l'ID n'est pas trouvé
        return promotionRepository.findById(id);
    }

}
