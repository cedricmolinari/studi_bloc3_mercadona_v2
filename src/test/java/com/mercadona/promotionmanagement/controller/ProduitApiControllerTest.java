package com.mercadona.promotionmanagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mercadona.promotionmanagement.api.ProduitApiController;
import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.entity.Promotion;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import com.mercadona.promotionmanagement.core.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
@ExtendWith(MockitoExtension.class)
public class ProduitApiControllerTest {

    @Mock
    private ProduitService produitService;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private ProduitApiController produitApiController;

    private Produit produit;
    private Promotion promotion;

    @BeforeEach
    public void setUp() {
        produit = new Produit();
        produit.setIdProduit(1);
        produit.setPrix(BigDecimal.valueOf(100));

        promotion = new Promotion();
        promotion.setIdPromotion(1);
        promotion.setDateDebut(LocalDate.now());
        promotion.setDateFin(LocalDate.now().plusDays(7));
        promotion.setPourcentage(BigDecimal.valueOf(10));
    }

    @Test
    public void testAddPromotionToProduct_success() {
        when(produitService.findById(1)).thenReturn(produit);
        when(promotionService.addPromotion(any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(promotion);

        ResponseEntity<?> response = produitApiController.addPromotionToProduct(1L, promotion);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testAddPromotionToProduct_productNotFound() {
        when(produitService.findById(1)).thenReturn(null);

        ResponseEntity<?> response = produitApiController.addPromotionToProduct(1L, promotion);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddPromotionToProduct_exceptionThrown() {
        when(produitService.findById(1)).thenReturn(produit);
        when(promotionService.addPromotion(any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class))).thenThrow(new RuntimeException("An exception"));

        ResponseEntity<?> response = produitApiController.addPromotionToProduct(1L, promotion);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
