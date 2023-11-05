// Ajoute un événement click sur chaque bouton de catégorie
document.querySelectorAll('.categorie-btn').forEach((button) => {
    button.addEventListener('click', function(e) {
        e.preventDefault();  // Empêche le rechargement de la page
        const categorieId = this.getAttribute('data-categorie-id');  // Récupère l'ID de la catégorie
        filterProductsByCategory(categorieId);
    });
});

document.querySelector('.toutes-categories-btn').addEventListener('click', function(e) {
    e.preventDefault();  // Empêche le rechargement de la page
    document.querySelectorAll('.produit').forEach((produit) => {
        produit.style.display = '';
    });
});


// Fonction pour filtrer les produits
function filterProductsByCategory(categorieId) {
    // Cache tous les produits
    document.querySelectorAll('.produit').forEach((produit) => {
        produit.style.display = 'none';
    });

    // Affiche seulement les produits qui correspondent à la catégorie sélectionnée
    document.querySelectorAll(`.produit[data-categorie-id="${categorieId}"]`).forEach((produit) => {
        produit.style.display = '';
    });
}
