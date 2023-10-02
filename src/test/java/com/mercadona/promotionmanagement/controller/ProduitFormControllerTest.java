package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.controller.ProduitFormController;
import com.mercadona.promotionmanagement.core.entity.Categorie;
import com.mercadona.promotionmanagement.core.entity.Produit;
import com.mercadona.promotionmanagement.core.repository.ProduitRepository;
import com.mercadona.promotionmanagement.core.service.CategorieService;
import com.mercadona.promotionmanagement.core.service.ProduitService;
import com.mercadona.promotionmanagement.form.ProduitForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitFormControllerTest {

    @InjectMocks
    private ProduitFormController controller;

    @Mock
    private ProduitService produitService;

    @Mock
    private CategorieService categorieService;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {

    }


    @Test
    public void testAjouterProduitEtImage_withValidForm() throws IOException {

        // Arrange
        ProduitForm form = new ProduitForm();
        form.setLibelle("Test");
        form.setDescription("Description");
        form.setPrix(BigDecimal.valueOf(10.0));
        form.setCategorieId(1);
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        when(mockMultipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockMultipartFile.getBytes()).thenReturn(new byte[10]);

        form.setImageFile(mockMultipartFile);

        when(bindingResult.hasErrors()).thenReturn(false);

        Categorie mockCategorie = new Categorie();
        mockCategorie.setLibelle("Alimentation");

        Produit unProduitMock = mock(Produit.class);

        when(categorieService.findById(anyInt())).thenReturn(mockCategorie);
        when(produitRepository.findLatestReferenceForCategory(anyString())).thenReturn("ALI000005");
        doNothing().when(produitService).save(any(Produit.class));


        // Act
        String result = controller.ajouterProduitEtImage(form, bindingResult, redirectAttributes);

        // Assert

        verify(categorieService, times(1)).findById(anyInt());
        verify(produitRepository, times(1)).findLatestReferenceForCategory(anyString());
        verify(produitService, times(1)).save(any(Produit.class));

        assertEquals("redirect:/produit/gestion-produit", result);
    }
    @Test
    public void testAjouterProduitEtImage_withInvalidForm() {
        // Arrange
        ProduitForm form = new ProduitForm();
        form.setLibelle("");
        form.setDescription("Description trop longue qui dépasse la limite de caractères...");
        form.setPrix(BigDecimal.valueOf(-1.0));
        form.setCategorieId(1);
        form.setImageFile(multipartFile);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getSize()).thenReturn((long) (2 * 1024 * 1024 + 1));
        when(multipartFile.getContentType()).thenReturn("image/gif");

        // Act
        String result = controller.ajouterProduitEtImage(form, bindingResult, redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute(eq("errorMessageImgType"), anyString());

        assertEquals("redirect:/produit/gestion-produit", result);
    }
    @Test
    public void testAjouterProduitEtImage_withLongDescription() throws IOException {
        // Arrange
        ProduitForm form = new ProduitForm();
        /*image + type*/
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        //when(mockMultipartFile.getOriginalFilename()).thenReturn("test.jpg");
        //when(mockMultipartFile.getBytes()).thenReturn(new byte[10]);
        form.setImageFile(mockMultipartFile);
        /*taille*/
        //when(multipartFile.getSize()).thenReturn((long) (2 * 1024 * 1024 + 1));
        /*libellé*/
        form.setLibelle("");
        /*description*/
        form.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non urna eros. Duis " +
                "scelerisque aliquet ante, ac sagittis lacus sollicitudin ut. Praesent volutpat justo nulla, et placerat" +
                " tellus facilisis at. Vivamus volutpat tristique libero, quis venenatis mauris. Pellentesque habitant.");
        /*prix*/
        form.setPrix(BigDecimal.valueOf(-1.0));
        /*catégorie*/
        form.setCategorieId(1);

        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = controller.ajouterProduitEtImage(form, bindingResult, redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute("errorMessageDescription", "La description ne peut excéder 255 caractères");
        assertEquals("redirect:/produit/gestion-produit", result);
    }
    @Test
    public void testAjouterProduitEtImage_withNegativePrice()  throws IOException {
        // Arrange
        ProduitForm form = new ProduitForm();
        /*image + type*/
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        System.out.println("Identifiant du mock dans le test: " + mockMultipartFile);
        when(mockMultipartFile.getContentType()).thenReturn("image/jpeg");
        form.setImageFile(mockMultipartFile);
        /*description*/
        form.setDescription("Lorem ipsum dolor sit amet.");
        /*prix*/
        form.setPrix(BigDecimal.valueOf(-1.0));
        when(bindingResult.hasErrors()).thenReturn(true);

        /*catégorie*/
        form.setCategorieId(1);

        // Act
        String result = controller.ajouterProduitEtImage(form, bindingResult, redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute(eq("errorMessagePrix"), anyString());
        assertEquals("redirect:/produit/gestion-produit", result);
    }
    @Test
    public void testAjouterProduitEtImage_withLargeImage() throws IOException {
        // Arrange
        ProduitForm form = new ProduitForm();
        /*image + type*/
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        System.out.println("Identifiant du mock dans le test: " + mockMultipartFile);
        when(mockMultipartFile.getContentType()).thenReturn("image/jpeg");
        form.setImageFile(mockMultipartFile);
        /*description*/
        form.setDescription("Lorem ipsum dolor sit amet.");
        /*prix*/
        form.setPrix(BigDecimal.valueOf(1.0));

        when(mockMultipartFile.getSize()).thenReturn((long) (2 * 1024 * 1024 + 1)); // Plus grand que 2 MB
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = controller.ajouterProduitEtImage(form, bindingResult, redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute("errorMessageImgTaille", "La taille de l'image est limitée à 2 MO");
        assertEquals("redirect:/produit/gestion-produit", result);
    }

}

