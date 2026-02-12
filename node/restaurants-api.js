import express from "express";
import cors from "cors";

const app = express();
app.use(cors());
app.use(express.json({ limit: "1mb" }));

// --- LOGGING ---
app.use((req, res, next) => {
  const start = Date.now();
  res.on("finish", () => {
    const ms = Date.now() - start;
    console.log(
      `[${new Date().toISOString()}] ${req.method} ${req.originalUrl} -> ${res.statusCode} (${ms}ms)`
    );
  });
  next();
});

/**
 * DB en mémoire - VRAIS RESTAURANTS avec données réelles
 */
const restaurants = [
  {
    id: "resto_001",
    nom: "Le Jules Verne",
    description: "Restaurant 2 étoiles Michelin du chef Frédéric Anton, situé au 2ème étage de la Tour Eiffel avec une vue imprenable sur Paris.",
    cuisine: "Française",
    prixMoyen: 230.0,
    noteGoogle: 4.5,
    latitude: 48.858093,
    longitude: 2.294694,
    adresse: "2ème étage Tour Eiffel, Avenue Gustave Eiffel, 75007 Paris",
    telephone: "+33 1 72 76 16 61",
    url: "https://www.restaurants-toureiffel.com/fr/restaurant-jules-verne.html",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "12:00-13:45, 19:00-21:00",
      mardi: "12:00-13:45, 19:00-21:00",
      mercredi: "12:00-13:45, 19:00-21:00",
      jeudi: "12:00-13:45, 19:00-21:00",
      vendredi: "12:00-13:45, 19:00-21:00",
      samedi: "12:00-13:45, 19:00-21:00",
      dimanche: "12:00-13:45, 19:00-21:00"
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
    description: "Restaurant 3 étoiles Michelin du chef Alain Passard, cuisine légumière d'exception avec produits des potagers personnels du chef.",
    cuisine: "Française",
    prixMoyen: 350.0,
    noteGoogle: 4.6,
    latitude: 48.856294,
    longitude: 2.315595,
    adresse: "84 Rue de Varenne, 75007 Paris",
    telephone: "+33 1 47 05 09 06",
    url: "https://www.alain-passard.com",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "12:30-14:30, 19:00-22:30",
      mardi: "12:30-14:30, 19:00-22:30",
      mercredi: "12:30-14:30, 19:00-22:30",
      jeudi: "12:30-14:30, 19:00-22:30",
      vendredi: "12:30-14:30, 19:00-22:30",
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
    nom: "Le Cinq",
    description: "Restaurant 3 étoiles au Four Seasons George V, cuisine du chef Christian Le Squer, luxe et raffinement absolu.",
    cuisine: "Française",
    prixMoyen: 395.0,
    noteGoogle: 4.8,
    latitude: 48.868182,
    longitude: 2.300882,
    adresse: "31 Avenue George V, 75008 Paris",
    telephone: "+33 1 49 52 71 54",
    url: "https://www.fourseasons.com/paris/dining/restaurants/le-cinq",
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
    id: "resto_004",
    nom: "L'Ambroisie",
    description: "Restaurant 3 étoiles du chef Bernard Pacaud, institution gastronomique Place des Vosges depuis 1988.",
    cuisine: "Française",
    prixMoyen: 350.0,
    noteGoogle: 4.7,
    latitude: 48.855606,
    longitude: 2.365492,
    adresse: "9 Place des Vosges, 75004 Paris",
    telephone: "+33 1 42 78 51 45",
    url: "https://www.ambroisie-paris.com",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "Fermé",
      mardi: "12:00-13:30, 20:00-21:30",
      mercredi: "12:00-13:30, 20:00-21:30",
      jeudi: "12:00-13:30, 20:00-21:30",
      vendredi: "12:00-13:30, 20:00-21:30",
      samedi: "12:00-13:30, 20:00-21:30",
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
    id: "resto_005",
    nom: "Pierre Gagnaire",
    description: "Restaurant 3 étoiles du chef Pierre Gagnaire, cuisine innovante et créative, association audacieuse de saveurs.",
    cuisine: "Française Contemporaine",
    prixMoyen: 380.0,
    noteGoogle: 4.6,
    latitude: 48.873245,
    longitude: 2.307599,
    adresse: "6 Rue Balzac, 75008 Paris",
    telephone: "+33 1 58 36 12 50",
    url: "https://www.pierregagnaire.com",
    isFavorite: false,
    etoilesMichelin: 3,
    horaires: {
      lundi: "12:00-13:30, 19:30-21:30",
      mardi: "12:00-13:30, 19:30-21:30",
      mercredi: "12:00-13:30, 19:30-21:30",
      jeudi: "12:00-13:30, 19:30-21:30",
      vendredi: "12:00-13:30, 19:30-21:30",
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
    id: "resto_006",
    nom: "Le Pré Catelan",
    description: "Restaurant 3 étoiles du chef Frédéric Anton, pavillon Napoléon III au cœur du Bois de Boulogne.",
    cuisine: "Française",
    prixMoyen: 310.0,
    noteGoogle: 4.6,
    latitude: 48.864286,
    longitude: 2.254842,
    adresse: "Route de Suresnes, 75016 Paris",
    telephone: "+33 1 44 14 41 14",
    url: "https://www.restaurant-leprecatelan.com",
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
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_007",
    nom: "Guy Savoy",
    description: "Restaurant 3 étoiles Michelin de Guy Savoy à la Monnaie de Paris, cuisine française d'exception.",
    cuisine: "Française",
    prixMoyen: 395.0,
    noteGoogle: 4.7,
    latitude: 48.857222,
    longitude: 2.340278,
    adresse: "11 Quai de Conti, 75006 Paris",
    telephone: "+33 1 43 80 40 61",
    url: "https://www.guysavoy.com",
    isFavorite: false,
    etoilesMichelin: 3,
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
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_008",
    nom: "Septime",
    description: "Restaurant 1 étoile Michelin du chef Bertrand Grébaut, cuisine créative de saison dans le 11ème arrondissement.",
    cuisine: "Française Contemporaine",
    prixMoyen: 95.0,
    noteGoogle: 4.5,
    latitude: 48.853165,
    longitude: 2.381686,
    adresse: "80 Rue de Charonne, 75011 Paris",
    telephone: "+33 1 43 67 38 29",
    url: "https://www.septime-charonne.fr",
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
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_009",
    nom: "Frenchie",
    description: "Restaurant 1 étoile Michelin de Gregory Marchand, cuisine néo-bistrot créative avec influences internationales.",
    cuisine: "Française Contemporaine",
    prixMoyen: 78.0,
    noteGoogle: 4.3,
    latitude: 48.866287,
    longitude: 2.342108,
    adresse: "5 Rue du Nil, 75002 Paris",
    telephone: "+33 1 40 39 96 19",
    url: "https://www.frenchie-restaurant.com",
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
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_010",
    nom: "David Toutain",
    description: "Restaurant 2 étoiles Michelin du chef David Toutain, cuisine inventive et graphique près des Invalides.",
    cuisine: "Française Contemporaine",
    prixMoyen: 140.0,
    noteGoogle: 4.6,
    latitude: 48.857222,
    longitude: 2.311944,
    adresse: "29 Rue Surcouf, 75007 Paris",
    telephone: "+33 1 45 50 11 10",
    url: "https://www.davidtoutain.com",
    isFavorite: false,
    etoilesMichelin: 2,
    horaires: {
      lundi: "Fermé",
      mardi: "Fermé",
      mercredi: "12:00-14:00, 19:30-21:30",
      jeudi: "12:00-14:00, 19:30-21:30",
      vendredi: "12:00-14:00, 19:30-21:30",
      samedi: "12:00-14:00, 19:30-21:30",
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
    id: "resto_011",
    nom: "Le Meurice Alain Ducasse",
    description: "Restaurant 3 étoiles Michelin au Meurice, décor somptueux inspiré de Versailles, cuisine d'Alain Ducasse.",
    cuisine: "Française",
    prixMoyen: 395.0,
    noteGoogle: 4.7,
    latitude: 48.865447,
    longitude: 2.328183,
    adresse: "228 Rue de Rivoli, 75001 Paris",
    telephone: "+33 1 44 58 10 55",
    url: "https://www.dorchestercollection.com/paris/le-meurice/restaurants-bars/le-meurice-alain-ducasse",
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
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_012",
    nom: "Mirazur",
    description: "Restaurant 3 étoiles Michelin à Menton, élu meilleur restaurant du monde 2019, vue mer et potager en terrasses.",
    cuisine: "Méditerranéenne",
    prixMoyen: 360.0,
    noteGoogle: 4.8,
    latitude: 43.774311,
    longitude: 7.491889,
    adresse: "30 Avenue Aristide Briand, 06500 Menton",
    telephone: "+33 4 92 41 86 86",
    url: "https://www.mirazur.fr",
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
    id: "resto_013",
    nom: "Le Petit Nice",
    description: "Restaurant 3 étoiles Michelin à Marseille, cuisine méditerranéenne du chef Gérald Passédat sur la corniche.",
    cuisine: "Méditerranéenne",
    prixMoyen: 295.0,
    noteGoogle: 4.7,
    latitude: 43.268768,
    longitude: 5.351363,
    adresse: "Anse de Maldormé, 13007 Marseille",
    telephone: "+33 4 91 59 25 92",
    url: "https://www.passedat.fr",
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
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_014",
    nom: "La Mère Brazier",
    description: "Institution lyonnaise 2 étoiles Michelin du chef Mathieu Viannay, cuisine bourgeoise revisitée.",
    cuisine: "Française",
    prixMoyen: 130.0,
    noteGoogle: 4.5,
    latitude: 45.770595,
    longitude: 4.829359,
    adresse: "12 Rue Royale, 69001 Lyon",
    telephone: "+33 4 78 23 17 20",
    url: "https://www.lamerebrazier.fr",
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
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=80"
  },
  {
    id: "resto_015",
    nom: "L'Auberge du Pont de Collonges",
    description: "Restaurant 3 étoiles Michelin, maison emblématique de Paul Bocuse, temple de la cuisine lyonnaise.",
    cuisine: "Française",
    prixMoyen: 225.0,
    noteGoogle: 4.6,
    latitude: 45.833458,
    longitude: 4.839458,
    adresse: "40 Rue de la Plage, 69660 Collonges-au-Mont-d'Or",
    telephone: "+33 4 72 42 90 90",
    url: "https://www.bocuse.fr",
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
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=400&q=80",
      "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=400&q=80"
    ],
    featuredImageUrl:
      "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80"
  }
];

// Index rapide par id
const byId = new Map(restaurants.map((r) => [r.id, r]));

/** Utilitaires */
function isValidRestaurant(obj) {
  if (!obj || typeof obj !== "object") return false;
  const required = [
    "id", "nom", "description", "cuisine", "prixMoyen", "noteGoogle",
    "latitude", "longitude", "adresse", "telephone", "url"
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

// GET /restaurants
app.get("/restaurants", (req, res) => {
  res.json(Array.from(byId.values()));
});

// GET /restaurants/:id
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

// POST /restaurants
app.post("/restaurants", (req, res) => {
  if (!isValidRestaurant(req.body)) {
    return res.status(400).json({
      error: "Bad Request",
      message: "Champs requis: id, nom, description, cuisine, prixMoyen, noteGoogle, latitude, longitude, adresse, telephone, url"
    });
  }

  const restaurant = normalizeRestaurant(req.body);
  const exists = byId.has(restaurant.id);
  byId.set(restaurant.id, restaurant);

  return res.status(exists ? 200 : 201).json(restaurant);
});

// PUT /restaurants/:id
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

// DELETE /restaurants/:id
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

// POST /restaurants/:id/toggle-favorite
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

// GET /restaurants/search
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

// GET /restaurants/favorites
app.get("/restaurants/favorites", (req, res) => {
  const favorites = Array.from(byId.values()).filter((r) => r.isFavorite);
  res.json(favorites);
});

// GET /health
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
  console.log(`   Nombre de restaurants: ${byId.size}`);
  console.log(`   GET    http://localhost:${PORT}/restaurants`);
  console.log(`   GET    http://localhost:${PORT}/restaurants/:id`);
  console.log(`   POST   http://localhost:${PORT}/restaurants`);
  console.log(`   PUT    http://localhost:${PORT}/restaurants/:id`);
  console.log(`   DELETE http://localhost:${PORT}/restaurants/:id`);
  console.log(`   POST   http://localhost:${PORT}/restaurants/:id/toggle-favorite`);
  console.log(`   GET    http://localhost:${PORT}/restaurants/search`);
  console.log(`   GET    http://localhost:${PORT}/restaurants/favorites`);
});