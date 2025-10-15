# ClientManager API

## Description
ClientManager est une API REST développée avec **Spring Boot (Java 17)** et **MySQL**.  
Elle permet de gérer des clients et leurs contrats d’assurance, via une architecture simple et conteneurisée avec Docker Compose.

## Architecture
L’architecture repose sur une structure classique **Spring Boot MVC** :
- **Controller** : définit les endpoints REST (`/clients`, `/clients/{id}/contracts`, etc.) et gère les requêtes HTTP.
- **Service** : contient la logique métier (création, suppression, mise à jour des clients et contrats).
- **Repository** : interface JPA pour la persistance des entités dans la base MySQL.
- **Entity** : représente les tables `Client` et `Contract` dans la base de données.
- **Docker Compose** orchestre deux conteneurs : `clientmanager-app` (API) et `db-mysql` (base MySQL 8.0.29).

L’application se connecte à la base via un réseau interne Docker et expose son API sur le port **8080** (localhost).

## URLs disponibles

### Clients
| Méthode | Endpoint | Description |
|----------|-----------|-------------|
| `GET` | `/api/clients/{clientId}` | Récupère un client spécifique |
| `POST` | `/api/clients/person` | Crée un nouveau client de type **Personne** |
| `POST` | `/api/clients/company` | Crée un nouveau client de type **Entreprise** |
| `PATCH` | `/api/clients/{clientId}` | Met à jour partiellement un client existant |
| `DELETE` | `/api/clients/{clientId}` | Met à jour toutes les dates de fin de tous les contrats d'un client (en réalité ça ne supprime rien, j'étais pas sûr s'il fallait supprimer le client avec ses contrats après la mise a jour des dates de fin) |


### Contrats
| Méthode | Endpoint | Description |
|----------|-----------|-------------|
| `GET` | `/api/clients/{clientId}/contracts` | Liste tous les contrats actifs d’un client (optionnellement filtrés par `updateDateFilter`) |
| `POST` | `/api/clients/{clientId}/contracts` | Crée un nouveau contrat pour un client donné |
| `PATCH` | `/api/clients/{clientId}/contracts/{contractId}` | Met à jour un contrat spécifique |
| `GET` | `/api/clients/{clientId}/contracts/sum` | Calcule la somme totale des contrats actifs d’un client |
| `DELETE` | `/api/clients/{clientId}/contracts/{contractId}` | Supprime un contrat |


## Preuve de fonctionnement
L’API démarre correctement et se connecte à MySQL (logs Spring Boot).  
Les endpoints ont été testés avec **Postman** et répondent avec les statuts HTTP :
- `200 OK` pour les requêtes réussies,  
- `201 Created` pour les créations,  
- `400 bad request` lorsqu'une validation échoue,
- `404 Not Found` si l’ID n’existe pas,
- `500 Internal Servor Error` si l'app est cassée normalement ça devrait jamais arriver, j'espère ;)

## Installation & Exécution
### Prérequis
- Docker & Docker Compose installés
- Postman pour tester

### Lancement avec Docker
docker-compose up --build

L’API sera disponible sur http://localhost:8080
La base de donnée MySQL sera disponible sur http://localhost:3306 (voir variables dans docker-compose.yml).

### Lancement avec l'environnement de développement

1. **Ouvrir le projet** dans VS Code.  
2. **Installer les extensions recommandées** :
   - **Debugger for Java** : pour exécuter et déboguer l’application.  
   - **Language Support for Java** : fournit la complétion et le support du langage.  
   - **Spring Boot Extension Pack** : outils pour gérer les projets Spring Boot (run, debug, navigate).  
   - **Test Runner for Java** : pour lancer les tests unitaires et d’intégration.  

3. **Configurer la base de données** : lancer la commande "docker run --name db-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=insurance_db -e MYSQL_USER=user -e MYSQL_PASSWORD=1234 -p 3306:3306 -d mysql:8.1" dans un terminal  

4. **Lancer l’application** :
   - Ouvrir le fichier `ClientmanagerApplication.java`.
   - Cliquer sur **Run** ou **Debug** dans VS Code via le panneau de débogage.  
   - L’API sera accessible sur `http://localhost:8080`.  

5. **Tester l’API** : utilisez Postman pour envoyer des requêtes aux endpoints listés dans la section **Clients & Contrats**.

6. **Lancer les tests** :
   - Ouvrir le fichier `C:\Projet_test_vaudoise\clientmanager\src\test\java\ch\devanthery\clientmanager\ClientmanagerApplicationTests.java`.
   - Cliquer sur la flèche verte au niveau du nom de la classe (possible grace à Test Runner for Java).

## Tests avec Postman

Une collection Postman complète nommée **Requêtes_de_tests.postman_collection.json** est disponible a la racine du projet pour valider le bon fonctionnement de l’API.  
Elle contient tous les appels nécessaires pour tester les fonctionnalités principales :

| No | Requête | Méthode | Endpoint | Description |
|---|----------|----------|-----------|--------------|
| 1 | Create a Person | `POST` | `/api/clients/person` | Crée un client individuel |
| 2 | Create a Company | `POST` | `/api/clients/company` | Crée un client entreprise |
| 3 | Get client by id | `GET` | `/api/clients/{id}` | Récupère un client existant |
| 4 | Modify a client | `PATCH` | `/api/clients/{id}` | Met à jour les informations d’un client |
| 5 | Delete a client | `DELETE` | `/api/clients/{id}` | Supprime un client et tous ses contrats |
| 6 | Assign a contract | `POST` | `/api/clients/{id}/contracts` | Associe un contrat à un client |
| 7 | Get contracts | `GET` | `/api/clients/{id}/contracts` | Liste les contrats d’un client |
| 8 | Get contracts (filtrés) | `GET` | `/api/clients/{id}/contracts?updateDateFilter=YYYY-MM-DD` | Filtre par date de mise à jour |
| 9 | Get contracts sum | `GET` | `/api/clients/{id}/contracts/sum` | Calcule le montant total des contrats |
| 10 | Modify a contract | `PATCH` | `/api/clients/{id}/contracts/{contractId}` | Met à jour le montant d’un contrat |
| 11 | Delete a contract | `DELETE` | `/api/clients/{id}/contracts/{contractId}` | Supprime un contrat spécifique d’un client |

N'hésitez pas a en créer d'autres ou à modifier les body JSON pour voir les autres comportements de l'API !

# Auteur
Projet développé par Devanthéry Noa dans le cadre d’un test technique pour un entretien.

PS: Les auteurs des premiers commits sont un peut bizarre car, je l'avais pas remarqué tout de suite, mais mon email git local était différent de mon email sur github 😅