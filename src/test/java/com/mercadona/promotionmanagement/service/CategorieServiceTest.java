package com.mercadona.promotionmanagement.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.repository.CategorieRepository;
import com.mercadona.promotionmanagement.core.service.CategorieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategorieServiceTest {

    @InjectMocks
    private CategorieService categorieService;

    @Mock
    private CategorieRepository categorieRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Categorie cat1 = new Categorie(1, "Electronics");
        Categorie cat2 = new Categorie(2, "Books");
        when(categorieRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        // Act
        List<Categorie> result = categorieService.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(categorieRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Exists() {
        // Arrange
        Categorie cat1 = new Categorie(1, "Electronics");
        when(categorieRepository.findById(1)).thenReturn(Optional.of(cat1));

        // Act
        Categorie result = categorieService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Electronics", result.getLibelle());
    }

    @Test
    public void testFindById_NotExists() {
        // Arrange
        when(categorieRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Categorie result = categorieService.findById(1);

        // Assert
        assertNull(result);
    }
}

