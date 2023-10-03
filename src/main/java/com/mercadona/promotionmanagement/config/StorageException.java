package com.mercadona.promotionmanagement.config;

// Définition d'une nouvelle classe d'exception personnalisée
public class StorageException extends RuntimeException {
    // Ce message est passé au constructeur de la classe parente (RuntimeException) pour être stocké et récupéré ultérieurement.
    public StorageException(String message) {
        super(message);
    }
    // Constructeur prenant un message d'erreur et une cause en arguments.
    // Ces deux arguments sont passés au constructeur de la classe parente pour être stockés et récupérés ultérieurement.
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
