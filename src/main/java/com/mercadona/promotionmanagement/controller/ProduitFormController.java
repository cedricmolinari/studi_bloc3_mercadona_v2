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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@Controller
public class ProduitFormController {
    private static final Logger logger = LogManager.getLogger(ProduitFormController.class);
    private ProduitService produitService;
    @Autowired
    public ProduitFormController(ProduitService produitService, CategorieService categorieService, ProduitRepository produitRepository) {
        this.produitService = produitService;
        this.categorieService = categorieService;
        this.produitRepository = produitRepository;
    }
    @Autowired
    private CategorieService categorieService;
    @Autowired
    private ProduitRepository produitRepository;


    // Getter pour ProduitService
    public ProduitService getProduitService() {
        return this.produitService;
    }

    public void setProduitService(ProduitService produitService) {
        this.produitService = produitService;
    }

    @Transactional
    @PostMapping("produit/gestion-produit/ajout")
    public String ajouterProduitEtImage(@Valid @ModelAttribute ProduitForm form, BindingResult results, RedirectAttributes redirectAttributes) {

// Gestion des contraintes de saisie de formulaire
        MultipartFile imageFile = form.getImageFile();
        String description = form.getDescription();

        if (results.hasErrors()) {
            if (imageFile == null || imageFile.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessageImgVide", "L'image est requise");
            }
            if (!"image/png".equals(imageFile.getContentType()) && !"image/jpeg".equals(imageFile.getContentType())) {
                redirectAttributes.addFlashAttribute("errorMessageImgType", "Seul le format .jpeg est autorisé");
            }
            if (imageFile.getSize() > 2 * 1024 * 1024) { // Taille en octets (2 MB)
                redirectAttributes.addFlashAttribute("errorMessageImgTaille", "La taille de l'image est limitée à 2 MO");
            }

            if (description.length() > 255) {
                redirectAttributes.addFlashAttribute("errorMessageDescription", "La description ne peut excéder 255 caractères");
            }

            BigDecimal prix = form.getPrix();
            if (prix != null && prix.doubleValue() < 0) {
                redirectAttributes.addFlashAttribute("errorMessagePrix", "Le prix doit être supérieur à 0");
            }
            if (results.getFieldError("libelle") != null) {
                redirectAttributes.addFlashAttribute("errorMessageLibelle", "Le libellé ne peut pas être vide ou excéder une certaine taille");
            }

            return "redirect:/produit/gestion-produit";
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
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            produit.setCheminImage(fileName);  // Enregistrement du chemin dans l'entité Produit
        }


// Sauvegarde de l'entité Produit dans la base de données
        produitService.save(produit);
        System.out.println("Dans le contrôleur, produitService est : " + this.produitService);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = produit.getDateCreation().format(formatter);
        redirectAttributes.addFlashAttribute("message", "Le produit de référence " + produit.getReference() + " a été ajouté le " + formattedDate + ".");

        return "redirect:/produit/gestion-produit";
    }
    private String saveImage(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            logger.warn("Le fichier ou le nom du fichier original est null");
            // Vous pouvez également lancer une exception ici si vous le souhaitez
            return null;
        }

        logger.debug("Sauvegarde de l'image: {}", file.getOriginalFilename());
        try {
            // Définit le chemin où sauvegarder l'image
            String folder = "src/main/resources/static/images/";

            // Construit un nom de fichier unique pour éviter les conflits
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // Crée le chemin du fichier
            Path path = Paths.get(folder + fileName);

            // Écrit le fichier sur le disque
            Files.write(path, file.getBytes());

            // Retourne seulement le nom du fichier (ou le chemin relatif)
            return fileName;
        } catch (IOException e) {
            // Gère les exceptions
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
