# FoodVisor

Application Android de découverte de restaurants gastronomiques en France, couplée à une API REST Node.js.

---

## Sommaire

- [Présentation](#présentation)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Lancer l'API](#lancer-lapi)
- [Lancer l'application Android](#lancer-lapplication-android)
- [Configuration réseau](#configuration-réseau)
- [Fonctionnalités](#fonctionnalités)
- [API REST — Référence](#api-rest--référence)
- [Structure du projet](#structure-du-projet)
- [Stack technique](#stack-technique)

---

## Présentation

FoodVisor permet de parcourir une sélection de restaurants gastronomiques français (étoilés Michelin), de les rechercher par critères, de les mettre en favoris et de consulter leur fiche complète (horaires, localisation, site web, téléphone).

---

## Architecture

Le projet est composé de deux parties indépendantes :

```
FoodVisor/
├── app/          → Application Android (Kotlin, MVVM)
└── node/         → API REST (Node.js / Express)
```

L'application Android consomme l'API Node.js via Retrofit. Les données sont également mises en cache localement via Room (SQLite).

Pattern : **MVVM + Repository**

---

## Prérequis

### API Node.js
- Node.js >= 18
- npm

### Application Android
- Android Studio Hedgehog ou supérieur
- SDK Android 24+ (Android 7.0 minimum)
- JDK 17

---

## Installation

### 1. Cloner le projet

```bash
git clone <url-du-repo>
cd FoodVisor
```

### 2. Installer les dépendances de l'API

```bash
cd node
npm install
```

### 3. Ouvrir le projet Android

Ouvrir le dossier `FoodVisor/` (racine) dans Android Studio. Gradle synchronisera automatiquement les dépendances.

---

## Lancer l'API

Depuis le dossier `node/` :

```bash
# Production
npm start

# Développement (rechargement automatique avec nodemon)
npm run dev
```

L'API démarre sur le port **8080**.

Vérifier que l'API fonctionne :
```
GET http://localhost:8080/health
```

Réponse attendue :
```json
{
  "ok": true,
  "totalRestaurants": 15,
  "timestamp": "2026-04-14T..."
}
```

> **Note :** Les données sont stockées en mémoire. Elles sont réinitialisées à chaque redémarrage du serveur.

---

## Lancer l'application Android

1. S'assurer que l'API est démarrée (voir section précédente)
2. [Configurer l'URL de l'API](#configuration-réseau) selon votre environnement
3. Connecter un émulateur ou un appareil physique
4. Lancer l'application depuis Android Studio (`Run > Run 'app'`)

---

## Configuration réseau

L'URL de base de l'API est définie dans :

```
app/src/main/java/com/foodvisor/data/api/RetrofitClient.kt
```

Modifier la constante `BASE_URL` selon votre cas :

| Environnement | URL |
|---|---|
| Émulateur Android Studio | `http://10.0.2.2:8080/` |
| Appareil physique | `http://<VOTRE_IP_LOCALE>:8080/` |

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```

Pour trouver votre IP locale sur Linux/Mac :
```bash
ip addr show   # ou ifconfig
```

---

## Fonctionnalités

### Authentification
- Connexion avec email / mot de passe (simulé, aucune vérification serveur)
- Inscription
- Mode invité (connexion sans compte)
- Déconnexion

### Accueil
- Liste de tous les restaurants
- Pull-to-refresh pour recharger depuis l'API
- Accès rapide à la mise en favori

### Recherche
- Recherche textuelle par nom ou description (avec debounce 500 ms)
- Filtres combinables :
  - Type de cuisine (Française, Française Contemporaine, Méditerranéenne)
  - Nombre d'étoiles Michelin (1+, 2+, 3)
  - Prix maximum (< 100 €, < 200 €, < 300 €)

### Favoris
- Liste des restaurants marqués comme favoris
- Synchronisé avec l'API et le cache local

### Fiche restaurant
- Photo en plein écran (collapsing toolbar)
- Informations : cuisine, prix moyen, note Google, étoiles Michelin
- Description, adresse, horaires par jour
- Actions directes :
  - Appel téléphonique
  - Ouverture du site web
  - Localisation sur carte (Google Maps)
  - Partage de la fiche
- Bouton favori (FAB)

### Profil utilisateur
- Affichage nom et email
- Modification du nom
- Déconnexion

---

## API REST — Référence

Base URL : `http://localhost:8080`

### Endpoints

| Méthode | Route | Description |
|---|---|---|
| `GET` | `/restaurants` | Liste tous les restaurants |
| `GET` | `/restaurants/search` | Recherche avec filtres |
| `GET` | `/restaurants/favorites` | Liste les favoris |
| `GET` | `/restaurants/:id` | Détail d'un restaurant |
| `POST` | `/restaurants` | Créer un restaurant |
| `PUT` | `/restaurants/:id` | Modifier un restaurant |
| `DELETE` | `/restaurants/:id` | Supprimer un restaurant |
| `POST` | `/restaurants/:id/toggle-favorite` | Basculer le statut favori |
| `GET` | `/health` | État de l'API |

### Paramètres de recherche (`GET /restaurants/search`)

| Paramètre | Type | Description |
|---|---|---|
| `query` | string | Recherche dans le nom et la description |
| `cuisine` | string | Filtrer par type de cuisine |
| `etoiles` | number | Nombre minimum d'étoiles Michelin |
| `prixMax` | number | Prix moyen maximum |
| `ville` | string | Filtrer par ville (dans l'adresse) |

Exemple :
```
GET /restaurants/search?cuisine=Française&etoiles=2&prixMax=300
```

### Modèle Restaurant

```json
{
  "id": "resto_001",
  "nom": "Le Jules Verne",
  "description": "...",
  "cuisine": "Française",
  "prixMoyen": 230.0,
  "noteGoogle": 4.5,
  "latitude": 48.858093,
  "longitude": 2.294694,
  "adresse": "2ème étage Tour Eiffel, 75007 Paris",
  "telephone": "+33 1 72 76 16 61",
  "url": "https://...",
  "isFavorite": false,
  "etoilesMichelin": 2,
  "horaires": {
    "lundi": "12:00-13:45, 19:00-21:00",
    "mardi": "..."
  },
  "thumbImageUrl": ["https://...", "https://..."],
  "featuredImageUrl": "https://..."
}
```

### Codes de réponse

| Code | Signification |
|---|---|
| `200` | Succès |
| `201` | Ressource créée |
| `204` | Suppression réussie (pas de body) |
| `400` | Données invalides |
| `404` | Restaurant introuvable |

---

## Structure du projet

```
FoodVisor/
├── node/
│   ├── restaurants-api.js     # Serveur Express + données en mémoire
│   └── package.json
│
└── app/src/main/java/com/foodvisor/
    ├── FoodvisorApplication.kt         # Initialisation (DB, Repo, Prefs)
    ├── MainActivity.kt                 # Navigation principale (Bottom Nav)
    │
    ├── data/
    │   ├── api/
    │   │   ├── ApiService.kt           # Définition des endpoints Retrofit
    │   │   ├── RetrofitClient.kt       # Configuration HTTP (URL, timeouts)
    │   │   └── NetworkResult.kt        # Wrapper résultat (Success/Error)
    │   ├── local/
    │   │   ├── AppDatabase.kt          # Base Room + DAO
    │   │   └── PreferencesManager.kt   # DataStore (données utilisateur)
    │   ├── model/
    │   │   ├── Restaurant.kt           # Entité Room + modèle API
    │   │   ├── User.kt
    │   │   └── UiState.kt
    │   └── repository/
    │       └── RestaurantRepository.kt # Source de vérité (API + cache)
    │
    └── ui/
        ├── auth/
        │   ├── LoginActivity.kt
        │   └── RegisterActivity.kt
        ├── home/
        │   ├── HomeFragment.kt
        │   └── HomeViewModel.kt
        ├── search/
        │   ├── SearchFragment.kt
        │   └── SearchViewModel.kt
        ├── favorites/
        │   ├── FavoritesFragment.kt
        │   └── FavoritesViewModel.kt
        ├── detail/
        │   ├── RestaurantDetailActivity.kt
        │   └── RestaurantDetailViewModel.kt
        ├── profile/
        │   ├── ProfileFragment.kt
        │   └── ProfileViewModel.kt
        ├── adapters/
        │   └── RestaurantAdapter.kt
        ├── base/
        │   └── BaseViewModel.kt
        ├── ViewModelFactory.kt
        └── utils/
            └── Extensions.kt          # Extensions Kotlin (loadImage, showToast…)
```

---

## Stack technique

### API Node.js
| Lib | Rôle |
|---|---|
| Express 4 | Serveur HTTP / routeur |
| cors | Gestion des CORS |
| nodemon | Rechargement auto en dev |

### Application Android
| Lib | Rôle |
|---|---|
| Kotlin | Langage principal |
| Coroutines | Asynchronisme |
| ViewModel + LiveData | MVVM / réactivité UI |
| Retrofit2 + OkHttp3 | Appels HTTP |
| Gson | Sérialisation JSON |
| Room | Cache SQLite local |
| Navigation Component | Navigation entre fragments |
| ViewBinding | Accès aux vues sans `findViewById` |
| Glide | Chargement d'images |
