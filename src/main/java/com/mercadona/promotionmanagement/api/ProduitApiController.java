package com.mercadona.promotionmanagement.api;

import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.entity.Promotion;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import com.mercadona.promotionmanagement.core.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class ProduitApiController {
    @Autowired
    ProduitService produitService;
    @Autowired
    PromotionService promotionService;
    @PostMapping("/api/produits/{id}/promotion")
    public ResponseEntity<?> addPromotionToProduct(@PathVariable Long id, @RequestBody Promotion promotion) {
        Integer produitId = id.intValue();
        try {
            Produit produit = produitService.findById(produitId);
            if (produit == null) {
                return new ResponseEntity<>(Map.of("success", false, "message", "Produit non trouvé"), HttpStatus.NOT_FOUND);
            }

            // Extraire les informations de l'objet promotion
            BigDecimal pourcentagePromo = promotion.getPourcentage();
            LocalDate dateDebut = promotion.getDateDebut();
            LocalDate dateFin = promotion.getDateFin();

            // Enregistrez la promotion dans la base de données
            Promotion savedPromotion = promotionService.addPromotion(pourcentagePromo, dateDebut, dateFin);

            // Mettez à jour le produit avec l'ID de promotion
            produit.setPromotion(savedPromotion);
            produitService.save(produit);

            return new ResponseEntity<>(Map.of("success", true, "promotion", savedPromotion), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("success", false, "message", "Erreur lors de l'ajout de la promotion: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/api/produits/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Integer produitId = id.intValue();
        try {
            Produit produit = produitService.findById(produitId);
            if (produit == null) {
                return new ResponseEntity<>("Produit non trouvé", HttpStatus.NOT_FOUND);
            }

            // Vérifiez si le produit a une promotion active
            Promotion promotion = produit.getPromotion();
            if (promotion != null && LocalDate.now().isAfter(promotion.getDateDebut()) && LocalDate.now().isBefore(promotion.getDateFin())) {
                BigDecimal discountFactor = BigDecimal.ONE.subtract(promotion.getPourcentage().divide(BigDecimal.valueOf(100)));  // Convertit le pourcentage en facteur de réduction
                BigDecimal reducedPrice = produit.getPrix().multiply(discountFactor);
                produit.setPrix(reducedPrice);  // Appliquer la réduction
            }

            return new ResponseEntity<>(produit, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("success", false, "message", "Description de l'erreur"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
