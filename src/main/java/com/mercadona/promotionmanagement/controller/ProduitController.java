package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.service.CategorieService;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import com.mercadona.promotionmanagement.form.ProduitForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class ProduitController {
    private final ProduitService produitService;
    private final CategorieService categorieService; // 1. Injection de CategorieService
    @Autowired
    public ProduitController(ProduitService produitService, CategorieService categorieService) {
        this.produitService = produitService;
        this.categorieService = categorieService; // Initialisation de CategorieService
    }
    @GetMapping("/produit")
    public String displayProduits(Model model) {
        List<Produit> produits = produitService.list();
        model.addAttribute("produits", produits);
        return "produit";
    }

    @GetMapping("produit/form")
    public String displayProduitForm(Model model) {
        model.addAttribute("produitForm", new ProduitForm());
        List<Categorie> categories = categorieService.findAll(); // 2. Récupération de la liste des catégories
        model.addAttribute("categories", categories); // Ajout de la liste des catégories au modèle
        return "gestionProduit";
    }
}
