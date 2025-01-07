# Gestion des fichiers avec Spring Boot

Ce projet permet de gérer l'upload, le stockage, le téléchargement et la suppression de fichiers via une API REST. 
Les fichiers peuvent être stockés localement (dans le système de fichiers) ou dans une base de données en tant que BLOB. 
Il inclut également des validations sur les fichiers uploadés (taille maximale et types permis) et offre une interface pour afficher la liste des fichiers stockés avec leurs métadonnées.

Elle est documentée avec swagger en utilisant ce lien https://gestiondesfichiers-1-0.onrender.com/swagger-ui/index.html

## Fonctionnalités principales

- **Upload de fichier** : Permet à l'utilisateur de télécharger des fichiers via une interface web ou un client API (Postman).
- **Validation des fichiers** : Vérification de la taille maximale et des types de fichiers autorisés.
- **Liste des fichiers** : Affiche la liste des fichiers stockés avec leurs métadonnées (nom, taille, type MIME, date d'upload).
- **Téléchargement des fichiers** : Permet le téléchargement de fichiers via un lien sécurisé.
- **Suppression de fichiers** (optionnelle) : Supprime un fichier via son ID ou son nom.
- **Gestion des limites** : La taille totale des fichiers uploadés est limitée à 10 Mo.

### Fonctionnalités bonus

- Pagination et recherche dans la liste des fichiers.
- Authentification avec Spring Security pour protéger les opérations sensibles.
- Monitoring avec Spring Boot Actuator.

## Prérequis

- **Langage** : Java 21 installé
- **Framework** : Spring Boot 3.x
- **Base de données** : PostgreSQL (ou H2 pour la simplicité)
- **Gestionnaire de dépendances** : Maven ou Gradle
- **Documentation API** : Swagger/OpenAPI

## Endpoints de l'API

- **POST /auth/signup** : Inscription d'un utilisateur
    - Réponse : `200 OK` avec les données de l'utilisateur
- **POST /auth/login** : Connexion d'un utilisateur
    - Réponse : `200 OK` avec le token ; ensuite mettre le token dans Authorize avec le Bearer Token
  
     ## Les Endpoints de l'API aprés connexion
- **POST /files** : Upload d'un fichier
    - Corps de la requête : `multipart/form-data` (fichier à uploader)
    - Réponse : `200 OK` avec le fichier uploadé et ses métadonnées
- **GET /files** : Liste des fichiers 
    - Paramètre : `page` de la liste, `size` d'une page, `searchQuery` le nom à recherher
    - Réponse : Liste de fichiers avec leurs métadonnées et pagination
- **GET /files/{id}/download** : Téléchargement d'un fichier
    - Paramètre : `id` du fichier
    - Réponse : Contenu du fichier
- **DELETE /files/{id}** : Suppression d'un fichier (optionnelle)
    - Paramètre : `id` du fichier
    - Réponse : `204 No Content` si suppression réussie

## Installation

### Cloner le projet

```bash
git clone https://github.com/ZahraThiaw/GestionDesFichiers.git
Configurer la base de données: Mettre à jour les propriétés de connexion à la base de données dans le fichier application.yaml
Démarrer l'application