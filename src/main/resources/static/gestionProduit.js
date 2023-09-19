document.querySelectorAll('.btnAppliquerPromo').forEach(button => {
    button.addEventListener('click', function(event) {
        // Récupérez l'ID du produit depuis l'attribut data-id
        const produitId = event.currentTarget.getAttribute('data-id');

        // Trouvez la ligne du tableau parente de ce bouton
        const row = event.currentTarget.closest('tr');

        // Collectez les données du formulaire
        const pourcentagePromo = parseFloat(row.querySelector('.pourcentagePromo').value);
        const promoDebut = row.querySelector('.promoDebut').value;
        const promoFin = row.querySelector('.promoFin').value;

        // Récupérez le jeton CSRF et son nom d'en-tête depuis les balises meta
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        // Construisez l'objet de données
        const promoData = {
            pourcentage: pourcentagePromo,
            dateDebut: promoDebut,
            dateFin: promoFin
        };

        // Envoyez les données au backend
        fetch('/api/produits/' + produitId + '/promotion', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeaderName]: csrfToken  // Ajoutez le jeton CSRF à l'en-tête de la requête
            },
            body: JSON.stringify(promoData)
        })
            .then(response => {
                // Vérifiez si la réponse est OK (code de statut 200-299)
                if (!response.ok) {
                    // Si ce n'est pas le cas, renvoyez une promesse rejetée avec le texte de la réponse
                    return response.text().then(text => Promise.reject(text));
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert('Promotion appliquée avec succès !');
                } else {
                    alert('Erreur lors de l\'application de la promotion : ' + data.message);
                }
            })
            .catch(error => {
                // Gérez les erreurs ici, y compris les réponses non-OK du serveur
                console.error('Erreur lors de l\'envoi de la demande:', error);
                alert('Erreur : ' + error);
            });
    });
});
