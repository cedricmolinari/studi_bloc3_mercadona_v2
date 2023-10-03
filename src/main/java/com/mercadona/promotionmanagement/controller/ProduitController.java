package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.service.CategorieService;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import com.mercadona.promotionmanagement.form.ProduitForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class ProduitController {
    private final ProduitService produitService;
    private final CategorieService categorieService;
    @Autowired
    public ProduitController(ProduitService produitService, CategorieService categorieService) {
        this.produitService = produitService;
        this.categorieService = categorieService;
    }
    // Méthode pour formater les dates de création des produits
    private List<Produit> formatProduitsDates(List<Produit> produits) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Utilise le stream pour formater chaque date de produit et retourne une nouvelle liste de produits
        return produits.stream().map(produit -> {
            Produit copy = produitService.cloneProduit(produit);
            String formattedDate = produit.getDateCreation().format(formatter);
            copy.setFormattedDate(formattedDate);
            return copy;
        }).collect(Collectors.toList());
    }

    // Gère les requêtes GET sur l'URL "/produit"
    @GetMapping("/produit")
    public String displayProduits(@RequestParam(value = "categorie", required = false) String categorieString, Model model) {
        // Récupère les informations d'authentification de l'utilisateur
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");

        List<Produit> produits;
        Integer categorieId = null;

        // Initialisation de la liste de produits en fonction de la catégorie
        if (categorieString != null && !categorieString.trim().isEmpty()) {
            try {
                categorieId = Integer.parseInt(categorieString);
                produits = produitService.findByCategorieId(categorieId);
            } catch (NumberFormatException e) {
                produits = produitService.listWithCategories();
            }
        } else {
            produits = produitService.listWithCategories();
        }

        List<Produit> produitsFormatted = formatProduitsDates(produits);

        // Ajoute les attributs au modèle pour les utiliser dans la vue
        model.addAttribute("produitsFormatted", produitsFormatted);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("selectedCategorieId", categorieId);
        model.addAttribute("isAuthenticated", isAuthenticated);

        return "produit"; // Nom de la vue à rendre
    }

    // Gère les requêtes GET sur l'URL "produit/gestion-produit"
    @GetMapping("produit/gestion-produit")
    public String displayProduitForm(Model model, @PageableDefault(size = 10) Pageable pageable) {
        // Récupère les informations d'authentification de l'utilisateur
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");

        // Récupère la liste paginée des produits
        Page<Produit> produits = produitService.listPage(pageable);
        List<Produit> produitsFormatted = formatProduitsDates(produits.getContent());

        // Génère la liste des numéros de pages pour la pagination
        List<Integer> pageNumbers = IntStream.rangeClosed(1, produits.getTotalPages())
                .boxed()
                .collect(Collectors.toList());

        // Ajoute les attributs au modèle pour les utiliser dans la vue
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("produitForm", new ProduitForm());
        model.addAttribute("produits", produits);
        model.addAttribute("produitsFormatted", produitsFormatted);
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("oneWeekFromNow", LocalDate.now().plusDays(7));

        return "gestionProduit"; // Nom de la vue à rendre
    }
}
