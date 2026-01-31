# League of Legends Champion Analytics

> Un système d'analyse de données avancé pour les champions de League of Legends, développé en Scala

[![Scala](https://img.shields.io/badge/Scala-DC322F?style=for-the-badge&logo=scala&logoColor=white)](https://www.scala-lang.org/)
[![JSON](https://img.shields.io/badge/JSON-000000?style=for-the-badge&logo=json&logoColor=white)](https://www.json.org/)

## 📋 Table des matières

- [À propos](#-à-propos)
- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Structure du projet](#-structure-du-projet)
- [Format des données](#-format-des-données)
- [Exemples de résultats](#-exemples-de-résultats)
- [Contributeur](#-contributeur)

## À propos

Ce projet est un outil d'analyse statistique complet pour les champions de League of Legends. Il charge des données JSON (même imparfaites), les nettoie, les valide, puis génère des statistiques détaillées sous plusieurs formats.

**Objectifs du projet :**
- Traiter efficacement de grandes quantités de données
- Gérer les erreurs de parsing et les doublons
- Fournir des analyses statistiques pertinentes
- Générer des rapports exploitables

## Fonctionnalités

### Traitement des données
- **Chargement robuste** : Parse les fichiers JSON même avec des erreurs
- **Validation stricte** : Vérifie la cohérence des données (rôles, tiers, taux, etc.)
- **Déduplication** : Supprime automatiquement les doublons par ID
- **Gestion d'erreurs** : Rapporte précisément les erreurs de parsing

### Analyses statistiques

#### Distributions
- Répartition par rôle (Top, Jungle, Mid, ADC, Support)
- Répartition par tier (S, A, B, C, D)
- Répartition par archetype

#### Top classements
- Top 10 Win Rate
- Top 10 Pick Rate
- Top 10 Ban Rate
- Top 10 KDA (Kill/Death/Assist ratio)
- Top 10 champions les plus difficiles

#### Moyennes par rôle
- Win rate moyen
- Difficulté moyenne
- Or moyen (avgGold)
- Dégâts moyens (avgDamage)

#### Statistiques globales
- Classe (archetype) la plus populaire
- Métriques de performance du traitement

### Formats de sortie
- **data_clean.json** : Données nettoyées et validées
- **results.json** : Toutes les statistiques au format JSON structuré
- **report.txt** : Rapport textuel détaillé et formaté

## Architecture

Le projet suit une architecture modulaire avec séparation des responsabilités :

```
┌─────────────┐
│   Main.scala│  ← Point d'entrée
└──────┬──────┘
       │
       ├─────────────────┬─────────────────┬──────────────────┐
       ▼                 ▼                 ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌─────────────┐  ┌─────────────┐
│DataManager   │  │ChampionStats │  │Validator    │  │OutputGen    │
│.scala        │  │.scala        │  │.scala       │  │erator.scala │
└──────────────┘  └──────────────┘  └─────────────┘  └─────────────┘
       │                 │                 │                  │
       └─────────────────┴─────────────────┴──────────────────┘
                              │
                              ▼
                      ┌──────────────┐
                      │Champion.scala│  ← Modèle de données
                      └──────────────┘
```

### Modules

| Module | Responsabilité |
|--------|---------------|
| **Champion.scala** | Case class représentant un champion + calcul KDA |
| **DataManager.scala** | Chargement, nettoyage et sauvegarde des données |
| **Validator.scala** | Validation des contraintes métier |
| **ChampionStats.scala** | Calculs statistiques (distributions, tops, moyennes) |
| **OutputGenerator.scala** | Génération des rapports (console, JSON, texte) |
| **Main.scala** | Orchestration du pipeline de traitement |

## Installation

### Prérequis

- **Scala** 2.13+ ou 3.x
- **SBT** (Scala Build Tool) 1.5+
- **Java** JDK 11+

### Dépendances

Le projet utilise **Circe** pour le parsing JSON :

```scala
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1"
)
```

### Installation

1. **Cloner le repository**
```bash
git clone <repository-url>
cd league-champions-analytics
```

2. **Compiler le projet**
```bash
sbt compile
```

3. **Exécuter les tests** (si disponibles)
```bash
sbt test
```

## Utilisation

### Exécution basique

```bash
# Utilise data/data_dirty.json par défaut
sbt run

# Spécifier un fichier d'entrée
sbt "run data/custom_data.json"
```

### Structure des dossiers

```
project/
├── data/
│   ├── data_dirty.json      # Fichier d'entrée (avec erreurs possibles)
│   └── data_clean.json      # Généré : données nettoyées
└── output/
    ├── results.json         # Généré : statistiques JSON
    └── report.txt          # Généré : rapport textuel
```

### Sortie console

```
=== TRAITEMENT DE : data/data_dirty.json ===

Nombre total d'entrées : 150
Erreurs de parsing : 5
Total valides : 142
Doublons : 3
Champions uniques : 139

✓ Fichier clean sauvegardé: data/data_clean.json

=== STATISTIQUES DÉTAILLÉES ===

Distribution des roles : 
  Top : 28
  Jungle : 27
  Mid : 30
  ADC : 26
  Support : 28

...

Traitement effectué en 0.342 secondes
```

## 📁 Structure du projet

```
.
├── Champion.scala           # Case class du champion
├── ChampionStats.scala      # Fonctions statistiques
├── DataManager.scala        # Gestion I/O et nettoyage
├── Main.scala              # Point d'entrée
├── OutputGenerator.scala   # Génération des sorties
├── Validator.scala         # Validation des données
├── data/
│   ├── data_dirty.json    # Données brutes
│   └── data_clean.json    # Données nettoyées (généré)
└── output/
    ├── results.json       # Statistiques JSON (généré)
    └── report.txt        # Rapport texte (généré)
```

## Format des données

### Entrée JSON (Champion)

```json
{
  "id": 1,
  "name": "Aatrox",
  "role": "Top",
  "difficulty": 7,
  "releaseYear": 2013,
  "pickRate": 12.5,
  "banRate": 8.3,
  "winRate": 51.2,
  "gamesPlayed": 15000,
  "avgKills": 6.2,
  "avgDeaths": 5.1,
  "avgAssists": 7.8,
  "avgGold": 11500,
  "avgDamage": 23000,
  "tier": "A",
  "archetype": "Fighter"
}
```

### Règles de validation

| Champ | Contrainte |
|-------|-----------|
| `role` | Doit être : Top, Jungle, Mid, ADC, ou Support |
| `tier` | Doit être : S, A, B, C, ou D |
| `difficulty` | Entre 1 et 10 |
| `releaseYear` | Entre 2009 et 2025 |
| `pickRate`, `banRate`, `winRate` | Entre 0 et 100 |
| `avgKills`, `avgDeaths`, `avgAssists` | ≥ 0 |

### Sortie results.json

Structure complète avec :
- Statistiques de parsing
- Distributions (rôles, tiers, classes)
- Tops 10 (win rate, pick rate, ban rate, KDA)
- Moyennes par rôle
- Champions les plus difficiles
- Classe la plus populaire

Exemple :
```json
{
  "statistics": {
    "total_champions_parsed": 150,
    "total_champions_valid": 142,
    "parsing_errors": 5,
    "duplicates_removed": 3
  },
  "champions_by_role": {
    "Top": 28,
    "Jungle": 27,
    "Mid": 30,
    "ADC": 26,
    "Support": 28
  },
  "highest_win_rate": [
    {
      "name": "Skarner",
      "role": "Jungle",
      "winRate": 54.8,
      "tier": "S"
    }
  ]
}
```

##  Exemples de résultats

### Top 10 Win Rate
```
1. Skarner : 54.8% (Tier S)
2. Amumu : 53.2% (Tier S)
3. Rammus : 52.9% (Tier A)
...
```

### Win Rate moyen par rôle
```
Mid : 49.8%
ADC : 49.5%
Top : 49.2%
Support : 48.9%
Jungle : 48.7%
```

### Top 10 KDA
```
1. Yuumi : 5.23 KDA (2.1/3.8/17.8)
2. Sona : 4.87 KDA (2.3/4.2/18.2)
3. Janna : 4.65 KDA (1.9/3.9/16.2)
...
```

##  Concepts Scala utilisés

- **Case classes** : Modélisation immutable des données
- **Pattern matching** : Gestion des Either pour les erreurs
- **Collections** : `map`, `filter`, `groupBy`, `sortBy`, `take`
- **For comprehensions** : Chaînage élégant des Either
- **Higher-order functions** : `flatMap`, `fold`
- **Generic auto-derivation** : Circe pour le JSON
- **Functional error handling** : `Try`, `Either`

##  Points d'apprentissage

Ce projet démontre :
-  Gestion robuste des erreurs en Scala
-  Parsing et validation de JSON avec Circe
-  Architecture modulaire et séparation des responsabilités
-  Programmation fonctionnelle (immutabilité, compositions)
-  Traitement de données à grande échelle
-  Génération multi-format de rapports

##  Contributeurs

**Rayan ZERIRI & Romain REN** - Projet académique Scala

