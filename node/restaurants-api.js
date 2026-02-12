import express from "express";
import cors from "cors";

const app = express();
app.use(cors());
app.use(express.json({ limit: "1mb" }));

// --- LOGGING (à mettre après cors/json, avant les routes) ---
app.use((req, res, next) => {
  const start = Date.now();

  // quand la réponse est envoyée, on trace
  res.on("finish", () => {
    const ms = Date.now() - start;
    console.log(
      `[${new Date().toISOString()}] ${req.method} ${req.originalUrl} -> ${res.statusCode} (${ms}ms)`
    );
  });

  next();
});

/**
 * "DB" en mémoire pour les restaurants
 * Format RestaurantDto JSON
 */
const restaurants = [
  {
    id: "resto_001",
    nom: "Le Jules Verne",
    description: "Restaurant gastronomique au 2ème étage de la Tour Eiffel, vue imprenable sur Paris.",
    cuisine: "Française",
    prixMoyen: 185.0,
    noteGoogle: 4.5,
    latitude: 48.858093,
    longitude: 2.294694,
    adresse: "Avenue Gustave Eiffel, 75007 Paris",
    telephone: "+33 1 45 55 61 44",
    url: "https://example.com/restaurants/le-jules-verne",
    isFavorite: false,
    etoilesMichelin: 1,
    horaires: {
      lundi: "Fermé",
      mardi: "12:00-13:30, 19:00-21:30",
      mercredi: "12:00-13:30, 19:00-21:30",
      jeudi: "12:00-13:30, 19:00-21:30",
      vendredi: "12:00-13:30, 19:00-21:30",
      samedi: "12:00-13:30, 19:00-21:30",
      dimanche: "12:00-13:30, 19:00-21:30"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_002",
    nom: "L'Arpège",
    description: "Restaurant 3 étoiles Michelin, cuisine végétale et produits du potager d'Alain Passard.",
    cuisine: "Française",
    prixMoyen: 320.0,
    noteGoogle: 4.6,
    latitude: 48.856294,
    longitude: 2.315595,
    adresse: "84 Rue de Varenne, 75007 Paris",
    telephone: "+33 1 47 05 09 06",
    url: "https://example.com/restaurants/larpege",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "12:00-14:00, 20:00-22:00",
      mardi: "12:00-14:00, 20:00-22:00",
      mercredi: "12:00-14:00, 20:00-22:00",
      jeudi: "12:00-14:00, 20:00-22:00",
      vendredi: "12:00-14:00, 20:00-22:00",
      samedi: "Fermé",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_003",
    nom: "Chez Paul",
    description: "Bistrot traditionnel parisien, ambiance authentique et conviviale.",
    cuisine: "Française",
    prixMoyen: 35.0,
    noteGoogle: 4.2,
    latitude: 48.853066,
    longitude: 2.371322,
    adresse: "13 Rue de Charonne, 75011 Paris",
    telephone: "+33 1 47 00 34 57",
    url: "https://example.com/restaurants/chez-paul",
    isFavorite: false,
    etoilesMichelin: 0,
    horaires: {
      lundi: "12:00-14:30, 19:00-23:00",
      mardi: "12:00-14:30, 19:00-23:00",
      mercredi: "12:00-14:30, 19:00-23:00",
      jeudi: "12:00-14:30, 19:00-23:00",
      vendredi: "12:00-14:30, 19:00-23:30",
      samedi: "12:00-14:30, 19:00-23:30",
      dimanche: "12:00-14:30, 19:00-22:30"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_004",
    nom: "Le Petit Nice",
    description: "Restaurant 3 étoiles sur la corniche, cuisine méditerranéenne d'exception.",
    cuisine: "Méditerranéenne",
    prixMoyen: 280.0,
    noteGoogle: 4.7,
    latitude: 43.268768,
    longitude: 5.351363,
    adresse: "Anse de Maldormé, 13007 Marseille",
    telephone: "+33 4 91 59 25 92",
    url: "https://example.com/restaurants/le-petit-nice",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:30-14:00, 20:00-22:00",
      jeudi: "12:30-14:00, 20:00-22:00",
      vendredi: "12:30-14:00, 20:00-22:00",
      samedi: "12:30-14:00, 20:00-22:00",
      dimanche: "12:30-14:00, 20:00-22:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_005",
    nom: "Le Comptoir du Relais",
    description: "Bistrot animé de Yves Camdeborde, cuisine de marché et ambiance chaleureuse.",
    cuisine: "Française",
    prixMoyen: 45.0,
    noteGoogle: 4.3,
    latitude: 48.851902,
    longitude: 2.338944,
    adresse: "9 Carrefour de l'Odéon, 75006 Paris",
    telephone: "+33 1 44 27 07 97",
    url: "https://example.com/restaurants/comptoir-du-relais",
    isFavorite: false,
    etoilesMichelin: 0,
    horaires: {
      lundi: "12:00-23:00",
      mardi: "12:00-23:00",
      mercredi: "12:00-23:00",
      jeudi: "12:00-23:00",
      vendredi: "12:00-23:00",
      samedi: "12:00-23:00",
      dimanche: "12:00-23:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_006",
    nom: "Le Chateaubriand",
    description: "Restaurant néo-bistrot créatif, menu surprise changeant quotidiennement.",
    cuisine: "Française Contemporaine",
    prixMoyen: 75.0,
    noteGoogle: 4.4,
    latitude: 48.867157,
    longitude: 2.381461,
    adresse: "129 Avenue Parmentier, 75011 Paris",
    telephone: "+33 1 43 57 45 95",
    url: "https://example.com/restaurants/le-chateaubriand",
    isFavorite: false,
    etoilesMichelin: 0,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "19:00-22:30",
      jeudi: "19:00-22:30",
      vendredi: "19:00-22:30",
      samedi: "19:00-22:30",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_007",
    nom: "La Mère Brazier",
    description: "Institution lyonnaise 2 étoiles, cuisine bourgeoise revisitée.",
    cuisine: "Française",
    prixMoyen: 120.0,
    noteGoogle: 4.5,
    latitude: 45.770595,
    longitude: 4.829359,
    adresse: "12 Rue Royale, 69001 Lyon",
    telephone: "+33 4 78 23 17 20",
    url: "https://example.com/restaurants/la-mere-brazier",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:00-13:30, 19:30-21:30",
      jeudi: "12:00-13:30, 19:30-21:30",
      vendredi: "12:00-13:30, 19:30-21:30",
      samedi: "19:30-21:30",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_008",
    nom: "Le Cinq",
    description: "Restaurant 3 étoiles du Four Seasons George V, luxe et gastronomie.",
    cuisine: "Française",
    prixMoyen: 380.0,
    noteGoogle: 4.8,
    latitude: 48.868182,
    longitude: 2.300882,
    adresse: "31 Avenue George V, 75008 Paris",
    telephone: "+33 1 49 52 71 54",
    url: "https://example.com/restaurants/le-cinq",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "19:00-22:00",
      mardi: "19:00-22:00",
      mercredi: "19:00-22:00",
      jeudi: "12:30-14:00, 19:00-22:00",
      vendredi: "12:30-14:00, 19:00-22:00",
      samedi: "12:30-14:00, 19:00-22:00",
      dimanche: "12:30-14:00, 19:00-22:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_009",
    nom: "L'Auberge du Pont de Collonges",
    description: "Restaurant emblématique de Paul Bocuse, temple de la cuisine lyonnaise.",
    cuisine: "Française",
    prixMoyen: 220.0,
    noteGoogle: 4.6,
    latitude: 45.833458,
    longitude: 4.839458,
    adresse: "40 Rue de la Plage, 69660 Collonges-au-Mont-d'Or",
    telephone: "+33 4 72 42 90 90",
    url: "https://example.com/restaurants/auberge-paul-bocuse",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:00-13:30, 20:00-21:30",
      jeudi: "12:00-13:30, 20:00-21:30",
      vendredi: "12:00-13:30, 20:00-21:30",
      samedi: "12:00-13:30, 20:00-21:30",
      dimanche: "12:00-13:30, 20:00-21:30"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_010",
    nom: "Septime",
    description: "Restaurant tendance, cuisine inventive dans un cadre épuré.",
    cuisine: "Française Contemporaine",
    prixMoyen: 85.0,
    noteGoogle: 4.5,
    latitude: 48.853165,
    longitude: 2.381686,
    adresse: "80 Rue de Charonne, 75011 Paris",
    telephone: "+33 1 43 67 38 29",
    url: "https://example.com/restaurants/septime",
    isFavorite: false,
    etoilesMichelin: 1,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:15-14:00, 19:30-22:00",
      jeudi: "12:15-14:00, 19:30-22:00",
      vendredi: "12:15-14:00, 19:30-22:00",
      samedi: "Fermé",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_011",
    nom: "Le Meurice Alain Ducasse",
    description: "Restaurant palace 3 étoiles, décor somptueux inspiré de Versailles.",
    cuisine: "Française",
    prixMoyen: 395.0,
    noteGoogle: 4.7,
    latitude: 48.865447,
    longitude: 2.328183,
    adresse: "228 Rue de Rivoli, 75001 Paris",
    telephone: "+33 1 44 58 10 55",
    url: "https://example.com/restaurants/le-meurice",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:30-14:00, 19:00-22:00",
      jeudi: "12:30-14:00, 19:00-22:00",
      vendredi: "12:30-14:00, 19:00-22:00",
      samedi: "12:30-14:00, 19:00-22:00",
      dimanche: "12:30-14:00, 19:00-22:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_012",
    nom: "L'Ami Jean",
    description: "Bistrot basque-béarnais généreux, ambiance conviviale et décontractée.",
    cuisine: "Basque",
    prixMoyen: 55.0,
    noteGoogle: 4.4,
    latitude: 48.856563,
    longitude: 2.309638,
    adresse: "27 Rue Malar, 75007 Paris",
    telephone: "+33 1 47 05 86 89",
    url: "https://example.com/restaurants/lami-jean",
    isFavorite: false,
    etoilesMichelin: 0,
    horaires: {
      lundi: "Fermé",
      mardi: "12:00-14:00, 19:00-23:00",
      mercredi: "12:00-14:00, 19:00-23:00",
      jeudi: "12:00-14:00, 19:00-23:00",
      vendredi: "12:00-14:00, 19:00-23:00",
      samedi: "12:00-14:00, 19:00-23:00",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_013",
    nom: "Mirazur",
    description: "Restaurant 3 étoiles à Menton, vue mer et potager en terrasses.",
    cuisine: "Méditerranéenne",
    prixMoyen: 340.0,
    noteGoogle: 4.8,
    latitude: 43.774311,
    longitude: 7.491889,
    adresse: "30 Avenue Aristide Briand, 06500 Menton",
    telephone: "+33 4 92 41 86 86",
    url: "https://example.com/restaurants/mirazur",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "Fermé",
      jeudi: "12:00-14:00, 19:30-22:00",
      vendredi: "12:00-14:00, 19:30-22:00",
      samedi: "12:00-14:00, 19:30-22:00",
      dimanche: "12:00-14:00, 19:30-22:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_014",
    nom: "Frenchie",
    description: "Restaurant néo-bistrot créatif, produits de saison et influences internationales.",
    cuisine: "Française Contemporaine",
    prixMoyen: 68.0,
    noteGoogle: 4.3,
    latitude: 48.866287,
    longitude: 2.342108,
    adresse: "5 Rue du Nil, 75002 Paris",
    telephone: "+33 1 40 39 96 19",
    url: "https://example.com/restaurants/frenchie",
    isFavorite: false,
    etoilesMichelin: 1,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "19:00-22:30",
      jeudi: "19:00-22:30",
      vendredi: "19:00-22:30",
      samedi: "19:00-22:30",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_015",
    nom: "La Côte Saint-Jacques",
    description: "Restaurant 2 étoiles en Bourgogne, cave exceptionnelle et cuisine raffinée.",
    cuisine: "Française",
    prixMoyen: 165.0,
    noteGoogle: 4.6,
    latitude: 47.796925,
    longitude: 3.479186,
    adresse: "14 Faubourg de Paris, 89300 Joigny",
    telephone: "+33 3 86 62 09 70",
    url: "https://example.com/restaurants/cote-saint-jacques",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "Fermé",
      mardi: "12:00-13:30, 19:30-21:00",
      mercredi: "12:00-13:30, 19:30-21:00",
      jeudi: "12:00-13:30, 19:30-21:00",
      vendredi: "12:00-13:30, 19:30-21:00",
      samedi: "12:00-13:30, 19:30-21:00",
      dimanche: "12:00-13:30, 19:30-21:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_016",
    nom: "Le Pré Catelan",
    description: "Restaurant 3 étoiles au cœur du Bois de Boulogne, cadre romantique.",
    cuisine: "Française",
    prixMoyen: 310.0,
    noteGoogle: 4.6,
    latitude: 48.864286,
    longitude: 2.254842,
    adresse: "Route de Suresnes, 75016 Paris",
    telephone: "+33 1 44 14 41 14",
    url: "https://example.com/restaurants/pre-catelan",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:30-14:00, 19:30-21:30",
      jeudi: "12:30-14:00, 19:30-21:30",
      vendredi: "12:30-14:00, 19:30-21:30",
      samedi: "12:30-14:00, 19:30-21:30",
      dimanche: "12:30-14:00"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_017",
    nom: "Le Taillevent",
    description: "Restaurant historique parisien, service impeccable et cuisine classique.",
    cuisine: "Française",
    prixMoyen: 195.0,
    noteGoogle: 4.5,
    latitude: 48.873386,
    longitude: 2.307286,
    adresse: "15 Rue Lamennais, 75008 Paris",
    telephone: "+33 1 44 95 15 01",
    url: "https://example.com/restaurants/taillevent",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "Fermé",
      mardi: "12:00-14:00, 19:00-22:00",
      mercredi: "12:00-14:00, 19:00-22:00",
      jeudi: "12:00-14:00, 19:00-22:00",
      vendredi: "12:00-14:00, 19:00-22:00",
      samedi: "Fermé",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_018",
    nom: "Le Clarence",
    description: "Restaurant 2 étoiles dans un hôtel particulier, ambiance feutrée.",
    cuisine: "Française",
    prixMoyen: 265.0,
    noteGoogle: 4.7,
    latitude: 48.870189,
    longitude: 2.314761,
    adresse: "31 Avenue Franklin D. Roosevelt, 75008 Paris",
    telephone: "+33 1 82 82 10 10",
    url: "https://example.com/restaurants/le-clarence",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "19:30-22:00",
      jeudi: "12:30-14:00, 19:30-22:00",
      vendredi: "12:30-14:00, 19:30-22:00",
      samedi: "12:30-14:00, 19:30-22:00",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_019",
    nom: "Les Bacchanales",
    description: "Restaurant 2 étoiles niché dans les vignes, terrasse panoramique.",
    cuisine: "Française Contemporaine",
    prixMoyen: 145.0,
    noteGoogle: 4.6,
    latitude: 43.668997,
    longitude: 7.052765,
    adresse: "247 Route de la Gaude, 06140 Vence",
    telephone: "+33 4 93 24 19 19",
    url: "https://example.com/restaurants/les-bacchanales",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "Fermé",
      jeudi: "12:00-13:30, 19:30-21:30",
      vendredi: "12:00-13:30, 19:30-21:30",
      samedi: "12:00-13:30, 19:30-21:30",
      dimanche: "12:00-13:30"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_020",
    nom: "Le Bouchon des Filles",
    description: "Bouchon lyonnais authentique tenu par des femmes, terroir généreux.",
    cuisine: "Lyonnaise",
    prixMoyen: 32.0,
    noteGoogle: 4.4,
    latitude: 45.766869,
    longitude: 4.834639,
    adresse: "20 Rue Sergent Blandan, 69001 Lyon",
    telephone: "+33 4 78 30 40 44",
    url: "https://example.com/restaurants/bouchon-des-filles",
    isFavorite: false,
    etoilesMichelin: 0,
    horaires: {
      lundi: "Fermé",
      mardi: "12:00-14:00, 19:00-22:00",
      mercredi: "12:00-14:00, 19:00-22:00",
      jeudi: "12:00-14:00, 19:00-22:00",
      vendredi: "12:00-14:00, 19:00-22:00",
      samedi: "12:00-14:00, 19:00-22:00",
      dimanche: "Fermé"
    },
    thumbImageUrl: [
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  }
];

// Index rapide par id
const byId = new Map(restaurants.map((r) => [r.id, r]));

/** Utilitaires */
function isValidRestaurant(obj) {
  if (!obj || typeof obj !== "object") return false;
  const required = [
    "id",
    "nom",
    "description",
    "cuisine",
    "prixMoyen",
    "noteGoogle",
    "latitude",
    "longitude",
    "adresse",
    "telephone",
    "url"
  ];
  return required.every((k) => obj[k] !== undefined && obj[k] !== null);
}

function normalizeRestaurant(input) {
  return {
    id: String(input.id),
    nom: String(input.nom),
    description: String(input.description),
    cuisine: String(input.cuisine),
    prixMoyen: Number(input.prixMoyen),
    noteGoogle: Number(input.noteGoogle),
    latitude: Number(input.latitude),
    longitude: Number(input.longitude),
    adresse: String(input.adresse),
    telephone: String(input.telephone),
    url: String(input.url),
    isFavorite: Boolean(input.isFavorite ?? false),
    etoilesMichelin: input.etoilesMichelin != null ? Number(input.etoilesMichelin) : 0,
    horaires: input.horaires || {},
    thumbImageUrl: Array.isArray(input.thumbImageUrl) ? input.thumbImageUrl.map(String) : [],
    featuredImageUrl: input.featuredImageUrl == null ? null : String(input.featuredImageUrl)
  };
}

/** Routes de l'API */

// GET /restaurants - Récupérer tous les restaurants
app.get("/restaurants", (req, res) => {
  res.json(Array.from(byId.values()));
});

// GET /restaurants/:id - Récupérer un restaurant spécifique
app.get("/restaurants/:id", (req, res) => {
  const { id } = req.params;
  const restaurant = byId.get(id);
  if (!restaurant) {
    return res.status(404).json({ 
      error: "Not Found", 
      message: `Restaurant '${id}' introuvable` 
    });
  }
  res.json(restaurant);
});

// POST /restaurants - Ajouter ou mettre à jour un restaurant
app.post("/restaurants", (req, res) => {
  if (!isValidRestaurant(req.body)) {
    return res.status(400).json({
      error: "Bad Request",
      message:
        "Champs requis: id, nom, description, cuisine, prixMoyen, noteGoogle, latitude, longitude, adresse, telephone, url"
    });
  }

  const restaurant = normalizeRestaurant(req.body);
  const exists = byId.has(restaurant.id);

  byId.set(restaurant.id, restaurant);

  // 201 si création, 200 si update
  return res.status(exists ? 200 : 201).json(restaurant);
});

// PUT /restaurants/:id - Mettre à jour un restaurant existant
app.put("/restaurants/:id", (req, res) => {
  const { id } = req.params;
  const existing = byId.get(id);
  
  if (!existing) {
    return res.status(404).json({ 
      error: "Not Found", 
      message: `Restaurant '${id}' introuvable` 
    });
  }

  if (!isValidRestaurant(req.body)) {
    return res.status(400).json({
      error: "Bad Request",
      message: "Données de restaurant invalides"
    });
  }

  const restaurant = normalizeRestaurant({ ...req.body, id });
  byId.set(id, restaurant);
  
  return res.json(restaurant);
});

// DELETE /restaurants/:id - Supprimer un restaurant
app.delete("/restaurants/:id", (req, res) => {
  const { id } = req.params;
  const restaurant = byId.get(id);
  
  if (!restaurant) {
    return res.status(404).json({ 
      error: "Not Found", 
      message: `Restaurant '${id}' introuvable` 
    });
  }

  byId.delete(id);
  return res.status(204).send();
});

// POST /restaurants/:id/toggle-favorite - Basculer le statut favori
app.post("/restaurants/:id/toggle-favorite", (req, res) => {
  const { id } = req.params;
  const restaurant = byId.get(id);
  
  if (!restaurant) {
    return res.status(404).json({ 
      error: "Not Found", 
      message: `Restaurant '${id}' introuvable` 
    });
  }
  
  restaurant.isFavorite = !restaurant.isFavorite;
  byId.set(id, restaurant);
  
  return res.json(restaurant);
});

// GET /restaurants/search - Recherche par critères
app.get("/restaurants/search", (req, res) => {
  const { cuisine, etoiles, prixMax, ville } = req.query;
  let results = Array.from(byId.values());

  if (cuisine) {
    results = results.filter((r) => 
      r.cuisine.toLowerCase().includes(cuisine.toLowerCase())
    );
  }

  if (etoiles !== undefined) {
    results = results.filter((r) => r.etoilesMichelin >= Number(etoiles));
  }

  if (prixMax !== undefined) {
    results = results.filter((r) => r.prixMoyen <= Number(prixMax));
  }

  if (ville) {
    results = results.filter((r) => 
      r.adresse.toLowerCase().includes(ville.toLowerCase())
    );
  }

  res.json(results);
});

// GET /restaurants/favorites - Récupérer les restaurants favoris
app.get("/restaurants/favorites", (req, res) => {
  const favorites = Array.from(byId.values()).filter((r) => r.isFavorite);
  res.json(favorites);
});

// GET /health - Vérification de santé de l'API
app.get("/health", (req, res) => {
  res.json({ 
    ok: true, 
    totalRestaurants: byId.size,
    timestamp: new Date().toISOString()
  });
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`✅ Restaurants API en écoute sur http://localhost:${PORT}`);
  console.log(`   GET    http://localhost:${PORT}/restaurants`);
  console.log(`   GET    http://localhost:${PORT}/restaurants/:id`);
  console.log(`   POST   http://localhost:${PORT}/restaurants`);
  console.log(`   PUT    http://localhost:${PORT}/restaurants/:id`);
  console.log(`   DELETE http://localhost:${PORT}/restaurants/:id`);
  console.log(`   POST   http://localhost:${PORT}/restaurants/:id/toggle-favorite`);
  console.log(`   GET    http://localhost:${PORT}/restaurants/search`);
  console.log(`   GET    http://localhost:${PORT}/restaurants/favorites`);
});