package com.mercadona.promotionmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;


@Configuration
@EnableWebSecurity
public class ConfigurationSecurite extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/static/**").permitAll() // Autorise l'accès à toutes les ressources statiques
                .antMatchers("/produit/gestion-produit").authenticated() // Seuls les utilisateurs authentifiés peuvent y accéder
                .antMatchers("/", "/produit", "/login").permitAll() // Autorise l'accès à tout le monde
                .antMatchers(HttpMethod.POST, "/api/produits/**/promotion").permitAll() // Autorise les requêtes POST vers cette URL à tout le monde
            .and()
            .formLogin()
                .usernameParameter("identifiant") // Définit le paramètre pour le nom d'utilisateur dans le formulaire
                .passwordParameter("motDePasse") // Définit le paramètre pour le mot de passe dans le formulaire
                .permitAll() // Autorise l'accès au formulaire de login à tout le monde
                .and()
            .logout() // Configuration pour la déconnexion
                .logoutUrl("/logout")
                .logoutSuccessUrl("/produit")
                .permitAll();
    }
    // Méthode qui configure l'authentification globale de l'application.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
