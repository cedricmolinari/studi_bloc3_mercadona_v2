package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.core.entity.Categorie;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProduitController {
    private final ProduitService produitService;
    private final CategorieService categorieService; // 1. Injection de CategorieService
    @Autowired
    public ProduitController(ProduitService produitService, CategorieService categorieService) {
        this.produitService = produitService;
        this.categorieService = categorieService; // Initialisation de CategorieService
    }
    @GetMapping({"/", "/produit"})
    public String displayProduits(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        /*List<Produit> produits = produitService.list();
        model.addAttribute("produits", produits);*/
        model.addAttribute("isAuthenticated", isAuthenticated); // la valeur de isAuthenticated dépend de votre logique d'authentification
        return "produit";
    }

    @GetMapping("produit/gestion-produit")
    public String displayProduitForm(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");

        Page<Produit> produits = produitService.list(pageable); // Utilisation de la pagination

        // Génère la liste des numéros de pages
        List<Integer> pageNumbers = IntStream.rangeClosed(1, produits.getTotalPages())
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("produitForm", new ProduitForm());
        model.addAttribute("produits", produits); // Ajout de la liste paginée des produits au modèle
        List<Categorie> categories = categorieService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("isAuthenticated", isAuthenticated);

        LocalDate today = LocalDate.now();
        LocalDate oneWeekFromNow = today.plusDays(7);
        model.addAttribute("today", today);
        model.addAttribute("oneWeekFromNow", oneWeekFromNow);


        return "gestionProduit";
    }


}
