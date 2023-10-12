# Studi - bloc3 Mercadona

## Technologies Utilisées
- Java 11 
- Spring Boot
- PostgreSQL
- AWS S3
- Heroku

## Fonctionnalités
- Gestion des produits : Ajout, modification, suppression et visualisation de produits.
- Gestion des promotions : Ajout, modification, suppression et visualisation de promotions.
- Stockage d'images sur AWS S3.

## Installation et Configuration
1. Clonage du dépôt <br>
`git clone https://github.com/cedricmolinari/studi_bloc3_mercadona_v2.git`
2. Installation des dépendances <br>
`mvn install`
3. Configuration des variables d'environnement <br>
- AWS_ACCESS_KEY_ID : cf heroku
- AWS_SECRET_ACCESS_KEY : cf heroku
- DATABASE_URL : cf heroku
4. Démarrage de l'application
`mvn spring-boot:run`

## Utilisation
- Ouvrez un navigateur et accédez à http://localhost:8080
- Utilisez l'interface utilisateur pour gérer les produits et les promotions.

## Tests
`mvn test`
