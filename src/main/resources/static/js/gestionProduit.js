document.querySelectorAll('.btnAppliquerPromo').forEach(button => {
    button.addEventListener('click', function(event) {
        // Trouve la ligne du tableau parente de ce bouton
        const row = event.currentTarget.closest('tr');

        // Récupère l'ID du produit depuis l'attribut data-id
        const produitId = event.currentTarget.getAttribute('data-id');
        const prixOriginal = parseFloat(row.querySelector('.prixOriginal').textContent);

        // Collecte les données du formulaire
        const pourcentagePromo = parseFloat(row.querySelector('.pourcentagePromo').value);
        const promoDebut = row.querySelector('.promoDebut').value;
        const promoFin = row.querySelector('.promoFin').value;

        // Vérifie si pourcentagePromo est vide ou égal à 0
        if (isNaN(pourcentagePromo) || pourcentagePromo <= 0 || pourcentagePromo > 100) {
            alert("Veuillez saisir une valeur de pourcentage entre 1 et 100.");
            return;  // Sort de la fonction pour ne pas continuer le traitement
        }

        const prixReduit = prixOriginal * (1 - (pourcentagePromo / 100));

        // Récupère le jeton CSRF et son nom d'en-tête depuis les balises meta
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        // Construit l'objet de données
        const promoData = {
            pourcentage: pourcentagePromo,
            dateDebut: promoDebut,
            dateFin: promoFin
        };

        // Envoie les données au backend
        fetch('/api/produits/' + produitId + '/promotion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeaderName]: csrfToken  // Ajout du jeton CSRF à l'en-tête de la requête
            },
            body: JSON.stringify(promoData)
        })
            .then(response => {
                // Vérifie si la réponse est OK (code de statut 200-299)
                if (!response.ok) {
                    // Si ce n'est pas le cas, renvoie une promesse rejetée avec le texte de la réponse
                    return response.text().then(text => Promise.reject(text));
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert('Promotion appliquée avec succès ! Nouveau prix : ' + prixReduit.toFixed(2) + ' €');
                } else {
                    alert('Erreur lors de l\'application de la promotion : ' + data.message);
                }
            })
            .catch(error => {
                // Gère les erreurs
                console.error('Erreur lors de l\'envoi de la demande:', error);
                alert('Erreur : ' + error);
            });
    });
});
