package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.controller.ProduitController;
import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.service.CategorieService;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ProduitControllerTest {

    @Mock
    private ProduitService produitService;

    @Mock
    private CategorieService categorieService;

    @Mock
    private Model model;

    @InjectMocks
    private ProduitController produitController;

    @Test
    public void testDisplayProduits() {
        Produit p1 = new Produit();
        p1.setDateCreation(LocalDateTime.now());
        Produit p2 = new Produit();
        p2.setDateCreation(LocalDateTime.now());
        List<Produit> produits = Arrays.asList(p1, p2);
        when(produitService.listWithCategories()).thenReturn(produits);
        when(produitService.cloneProduit(any(Produit.class))).thenAnswer(inv -> {
            Produit original = inv.getArgument(0);
            Produit copy = new Produit();
            copy.setDateCreation(original.getDateCreation());
            return copy;
        });

        List<Categorie> categories = Arrays.asList(new Categorie(), new Categorie());
        when(categorieService.findAll()).thenReturn(categories);

        String result = produitController.displayProduits(null, model);

        verify(produitService, times(1)).listWithCategories();
        verify(categorieService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("produitsFormatted"), any());
        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("isAuthenticated"), any());

        assert "produit".equals(result);
    }

    @Test
    public void testDisplayProduitForm() {
        Produit p1 = new Produit();
        p1.setDateCreation(LocalDateTime.now());
        Produit p2 = new Produit();
        p2.setDateCreation(LocalDateTime.now());
        List<Produit> produits = Arrays.asList(p1, p2);
        Page<Produit> pageProduits = new PageImpl<>(produits);

        when(produitService.listPage(any(Pageable.class))).thenReturn(pageProduits);
        when(produitService.cloneProduit(any(Produit.class))).thenAnswer(inv -> {
            Produit original = inv.getArgument(0);
            Produit copy = new Produit();
            copy.setDateCreation(original.getDateCreation());
            return copy;
        });

        List<Categorie> categories = Arrays.asList(new Categorie(), new Categorie());
        when(categorieService.findAll()).thenReturn(categories);

        String result = produitController.displayProduitForm(model, Pageable.unpaged());


        verify(produitService, times(1)).listPage(any(Pageable.class));
        verify(categorieService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("pageNumbers"), any());
        verify(model, times(1)).addAttribute(eq("produitForm"), any());
        verify(model, times(1)).addAttribute(eq("produitsFormatted"), any());
        verify(model, times(1)).addAttribute(eq("categories"), any());
        verify(model, times(1)).addAttribute(eq("isAuthenticated"), any());
        verify(model, times(1)).addAttribute(eq("today"), any());
        verify(model, times(1)).addAttribute(eq("oneWeekFromNow"), any());

        assert "gestionProduit".equals(result);
    }
}

