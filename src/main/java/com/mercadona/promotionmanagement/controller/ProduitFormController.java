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

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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

        logger.info("Début de la méthode ajouterProduitEtImage");

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

            String prix = form.getPrix();
            if (prix != null) {
                try {
                    BigDecimal prixDecimal = new BigDecimal(prix.replace(",", "."));
                    if (prixDecimal.compareTo(BigDecimal.ZERO) < 0) {
                        redirectAttributes.addFlashAttribute("errorMessagePrix", "Le prix doit être supérieur à 0");
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException("La conversion a échoué", e);
                }
            }
            if (results.getFieldError("libelle") != null) {
                redirectAttributes.addFlashAttribute("errorMessageLibelle", "Le libellé ne peut pas être vide ou excéder une certaine taille");
            }

            return "redirect:/produit/gestion-produit";
        }


        try {
            logger.info("Création d'un nouveau produit");
            Produit produit = new Produit();
            produit.setLibelle(form.getLibelle());
            produit.setDescription(form.getDescription());
            // Conversion du prix
            String prix = form.getPrix().replace(',', '.');
            produit.setPrix(new BigDecimal(prix));

            produit.setDateCreation(LocalDateTime.now());

            Categorie categorie = categorieService.findById(form.getCategorieId());
            produit.setCategorie(categorie);

            String lastReference = getLastUsedNumberFromDatabase(categorie.getLibelle());
            String newReference = generateNewReference(categorie.getLibelle(), lastReference);
            produit.setReference(newReference);

            logger.info("Sauvegarde de l'image sur S3");

            // Sauvegarde de l'image et mise à jour du chemin dans l'entité Produit
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                produit.setCheminImage(fileName);  // Enregistrement du chemin dans l'entité Produit
            }

            logger.info("Sauvegarde du produit dans la base de données");

            // Sauvegarde de l'entité Produit dans la base de données
            produitService.save(produit);
            System.out.println("Dans le contrôleur, produitService est : " + this.produitService);
            logger.info("Produit sauvegardé avec succès");


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDate = produit.getDateCreation().format(formatter);
            redirectAttributes.addFlashAttribute("message", "Le produit de référence " + produit.getReference() + " a été ajouté le " + formattedDate + ".");

            return "redirect:/produit/gestion-produit";

        } catch (Exception e) {
            logger.error("Une erreur est survenue", e);
            throw new RuntimeException("Échec de la sauvegarde de l'image", e);
        }
    }
    private String saveImage(MultipartFile file) throws IOException {

        logger.info("Début de la méthode saveImage");

        if (file == null || file.getOriginalFilename() == null) {
            logger.warn("Le fichier ou le nom du fichier original est null");
            // Vous pouvez également lancer une exception ici si vous le souhaitez
            return null;
        }

        logger.debug("Sauvegarde de l'image: {}", file.getOriginalFilename());
        try {
            logger.info("Tentative de sauvegarde de l'image sur S3");
            String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
            String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
            String bucketName = "img-produits";

            S3Client s3client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                    .region(Region.EU_WEST_3)
                    .build();

            // Construit un nom de fichier unique pour éviter les conflits
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // Définit le chemin où sauvegarder l'image
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Convertir le contenu du fichier en un objet RequestBody
            RequestBody requestBody = RequestBody.fromInputStream(
                    file.getInputStream(),
                    file.getSize());

            // Effectuer l'opération de mise en ligne (upload)
            s3client.putObject(putObjectRequest, requestBody);

            logger.info("Image sauvegardée avec succès sur S3");

            // Retourne seulement le nom du fichier (ou le chemin relatif)
            return fileName;

        } catch (IOException e) {
            logger.error("Échec de la sauvegarde de l'image", e);
            throw e;
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
