package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.core.entity.Utilisateur;
import com.mercadona.promotionmanagement.core.service.UtilisateurService;
import com.mercadona.promotionmanagement.form.LoginForm;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class LoginFormController {
    private final UtilisateurService utilisateurService;
    @Autowired
    public LoginFormController(UtilisateurService utilisateurService) { this.utilisateurService = utilisateurService; }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        System.out.println("Model attributes : " + model.asMap());
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }



    @Transactional
    @PostMapping("login")
    public String connexion(@Valid @ModelAttribute LoginForm form, BindingResult results, RedirectAttributes redirectAttributes) {
        if (results.hasErrors()) {
            return "login";
        }
        String identifiant = form.getIdentifiant();
        Utilisateur utilisateur = utilisateurService.findByIdentifiant(identifiant);

        if (utilisateur != null && passwordEncoder.matches(form.getMotDePasse(), utilisateur.getMotDePasse())) {
            redirectAttributes.addFlashAttribute("message", "Vous vous êtes identifiés avec succès.");
            return "redirect:/produit"; // Rediriger si succès
        } else {
            // Si l'utilisateur n'est pas trouvé ou si le mot de passe ne correspond pas, retourner à la page de login
            redirectAttributes.addFlashAttribute("error", "Identifiant ou mot de passe incorrect.");
            return "redirect:/login";
        }
    }

}
