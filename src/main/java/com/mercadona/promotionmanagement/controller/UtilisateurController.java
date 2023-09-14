package com.mercadona.promotionmanagement.controller;

import com.mercadona.promotionmanagement.form.LoginForm;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class UtilisateurController {
    @GetMapping("/login")
    public String displayLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
}
