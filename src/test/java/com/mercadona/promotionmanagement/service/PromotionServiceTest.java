package com.mercadona.promotionmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.mercadona.promotionmanagement.core.entity.Promotion;
import com.mercadona.promotionmanagement.core.repository.PromotionRepository;
import com.mercadona.promotionmanagement.core.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PromotionServiceTest {

    @InjectMocks
    private PromotionService promotionService;

    @Mock
    private PromotionRepository promotionRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddPromotion() {
        // Arrange
        BigDecimal pourcentage = new BigDecimal("10.00");
        LocalDate dateDebut = LocalDate.of(2023, 1, 1);
        LocalDate dateFin = LocalDate.of(2023, 12, 31);

        Promotion promotion = new Promotion();
        promotion.setPourcentage(pourcentage);
        promotion.setDateDebut(dateDebut);
        promotion.setDateFin(dateFin);

        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        // Act
        Promotion result = promotionService.addPromotion(pourcentage, dateDebut, dateFin);

        // Assert
        assertEquals(pourcentage, result.getPourcentage());
        assertEquals(dateDebut, result.getDateDebut());
        assertEquals(dateFin, result.getDateFin());

        verify(promotionRepository).save(any(Promotion.class));
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        Optional<Promotion> promotion = Optional.of(new Promotion());
        when(promotionRepository.findById(id)).thenReturn(promotion);

        // Act
        Optional<Promotion> result = promotionService.findById(id);

        // Assert
        assertEquals(promotion, result);
        verify(promotionRepository).findById(id);
    }
}

