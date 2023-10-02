package com.mercadona.promotionmanagement.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mercadona.promotionmanagement.core.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mercadona.promotionmanagement.core.entity.Utilisateur;
import com.mercadona.promotionmanagement.core.repository.UtilisateurRepository;

public class UtilisateurServiceTest {

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername() {
        // Arrange
        String identifiant = "admin";
        String motDePasse = "password";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdentifiant(identifiant);
        utilisateur.setMotDePasse(motDePasse);

        when(utilisateurRepository.findByIdentifiant(identifiant)).thenReturn(utilisateur);

        // Act
        UserDetails userDetails = utilisateurService.loadUserByUsername(identifiant);

        // Assert
        assertNotNull(userDetails);
        assertEquals(identifiant, userDetails.getUsername());
        assertEquals(motDePasse, userDetails.getPassword());

        verify(utilisateurRepository).findByIdentifiant(identifiant);
    }

    @Test
    public void testLoadUserByUsername_NotFound() {
        // Arrange
        String identifiant = "unknown";

        when(utilisateurRepository.findByIdentifiant(identifiant)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> utilisateurService.loadUserByUsername(identifiant));

        verify(utilisateurRepository).findByIdentifiant(identifiant);
    }

    @Test
    public void testSaveAdmin() {
        // Arrange
        String identifiant = "admin";
        String motDePasse = "password";
        String encryptedPassword = "encryptedPassword";

        when(passwordEncoder.encode(motDePasse)).thenReturn(encryptedPassword);

        // Act
        utilisateurService.saveAdmin(identifiant, motDePasse);

        // Assert
        verify(passwordEncoder).encode(motDePasse);
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }
}

