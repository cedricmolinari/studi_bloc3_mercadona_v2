package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.repository.ProduitRepository;
import com.mercadona.promotionmanagement.core.service.CategorieService;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import com.mercadona.promotionmanagement.form.ProduitForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@Controller
public class ProduitFormController {
    private static final Logger logger = LogManager.getLogger(ProduitFormController.class);
    private final ProduitService produitService;
    @Autowired
    public ProduitFormController(ProduitService produitService) {
        this.produitService = produitService;
    }
    @Autowired
    private CategorieService categorieService;
    @Autowired
    private ProduitRepository produitRepository;
    @Transactional
    @PostMapping("produit/form/ajout")
    public String ajouterProduitEtImage(@Valid @ModelAttribute ProduitForm form, BindingResult results, RedirectAttributes redirectAttributes) {
        if (results.hasErrors()) {
            logger.warn("Erreur dans le formulaire d'ajout de l'produit.");
            return "gestionProduit";
        }

        Produit produit = new Produit();
        produit.setLibelle(form.getLibelle());
        produit.setDescription(form.getDescription());
        produit.setPrix(form.getPrix());
        produit.setDateCreation(LocalDateTime.now());

        Categorie categorie = categorieService.findById(form.getCategorieId());
        produit.setCategorie(categorie);

        String lastReference = getLastUsedNumberFromDatabase(categorie.getLibelle());
        String newReference = generateNewReference(categorie.getLibelle(), lastReference);
        produit.setReference(newReference);

// Sauvegarde de l'image et mise à jour du chemin dans l'entité Produit
        MultipartFile imageFile = form.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            produit.setCheminImage(fileName);  // Enregistrement du chemin dans l'entité Produit
        }

// Sauvegarde de l'entité Produit dans la base de données
        // Sauvegarde de l'entité Produit dans la base de données
        produitService.save(produit);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = produit.getDateCreation().format(formatter);
        redirectAttributes.addFlashAttribute("message", "Le produit de référence " + produit.getReference() + " a été ajouté le " + formattedDate + ".");

        return "redirect:/produit/form";
    }
    private String saveImage(MultipartFile file) {
        logger.debug("Sauvegarde de l'image: {}", file.getOriginalFilename());
        try {
            // Définir le chemin où vous souhaitez sauvegarder l'image
            String folder = "src/main/resources/static/";

            // Construire un nom de fichier unique pour éviter les collisions
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // Créer le chemin du fichier
            Path path = Paths.get(folder + fileName);

            // Écrire le fichier sur le disque
            Files.write(path, file.getBytes());

            // Retourner seulement le nom du fichier (ou le chemin relatif)
            return fileName;
        } catch (IOException e) {
            // Gérer les exceptions comme vous le jugez approprié
            throw new RuntimeException("Échec de la sauvegarde de l'image", e);
        }
    }
    private String getLastUsedNumberFromDatabase(String categoryLibelle) {
        String categoryPrefix = categoryLibelle.substring(0, 3).toUpperCase();
        String lastReference = produitRepository.findLatestReferenceForCategory(categoryPrefix);
        return lastReference != null ? lastReference.substring(3) : "000000";
    }

    private String generateNewReference(String categorieLibelle, String lastNumber) {
        String prefix = categorieLibelle.substring(0, 3).toUpperCase();
        int incrementedNumber = Integer.parseInt(lastNumber) + 1;
        String newNumber = String.format("%06d", incrementedNumber);
        return prefix + newNumber;
    }
}
