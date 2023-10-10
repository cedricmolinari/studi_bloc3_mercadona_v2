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
3. Configuration des variables d'environnement
  | AWS_ACCESS_KEY_ID    | AWS_SECRET_ACCESS_KEY                    | DATABASE_URL                                                                                                                                                    |<br>
  |----------------------|------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|<br>
  | AKIAVL4WRYHPKKUFP44X | 96IpUT27guuMINFI91TXCJOdZ5n3ZxcC3d3a0N5/ | postgres://vcinocvsgmlieq:89e599e02b90700c770586ab70f177078d75898fb19b6f676f5779943bc4b524@ec2-52-215-68-14.eu-west-1.compute.amazonaws.com:5432/d3vged7rv65ntj |<br>
4. Démarrage de l'application
`mvn spring-boot:run`

## Utilisation
- Ouvrez un navigateur et accédez à http://localhost:8080
- Utilisez l'interface utilisateur pour gérer les produits et les promotions.

## Tests
`mvn test`
