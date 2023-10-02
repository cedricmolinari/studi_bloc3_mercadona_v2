package com.mercadona.promotionmanagement.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.repository.ProduitRepository;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProduitServiceTest {

    @InjectMocks
    private ProduitService produitService;

    @Mock
    private ProduitRepository produitRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListPage() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Produit prod1 = new Produit();
        Produit prod2 = new Produit();
        Page<Produit> page = new PageImpl<>(Arrays.asList(prod1, prod2));
        when(produitRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Produit> result = produitService.listPage(pageable);

        // Assert
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testFindById_Exists() {
        // Arrange
        Produit prod = new Produit();
        when(produitRepository.findById(1)).thenReturn(Optional.of(prod));

        // Act
        Produit result = produitService.findById(1);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindById_NotExists() {
        // Arrange
        when(produitRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Produit result = produitService.findById(1);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSave() {
        // Arrange
        Produit prod = new Produit();
        when(produitRepository.save(prod)).thenReturn(prod);

        // Act
        produitService.save(prod);

        // Assert
        verify(produitRepository).save(prod);
    }

    @Test
    public void testGetLastUsedNumberFromDatabase_Exists() {
        // Arrange
        String categoryPrefix = "CAT";
        when(produitRepository.findLatestReferenceForCategory("CAT")).thenReturn("CAT000002");

        // Act
        int lastNumber = produitService.getLastUsedNumberFromDatabase(categoryPrefix);

        // Assert
        assertEquals(2, lastNumber);
    }

    @Test
    public void testGetLastUsedNumberFromDatabase_NotExists() {
        // Arrange
        String categoryPrefix = "CAT";
        when(produitRepository.findLatestReferenceForCategory("CAT")).thenReturn(null);

        // Act
        int lastNumber = produitService.getLastUsedNumberFromDatabase(categoryPrefix);

        // Assert
        assertEquals(0, lastNumber);
    }

    @Test
    public void testFindByCategorie() {
        // Arrange
        String categorie = "Electronics";
        Produit prod1 = new Produit();
        Produit prod2 = new Produit();
        List<Produit> produits = Arrays.asList(prod1, prod2);
        when(produitRepository.findByCategorieLibelle(categorie)).thenReturn(produits);

        // Act
        List<Produit> result = produitService.findByCategorie(categorie);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testCloneProduit() {
        // Arrange
        Produit original = new Produit();
        original.setIdProduit(1);
        original.setReference("REF001");

        // Act
        Produit clone = produitService.cloneProduit(original);

        // Assert
        assertEquals(original.getIdProduit(), clone.getIdProduit());
        assertEquals(original.getReference(), clone.getReference());
    }
}

