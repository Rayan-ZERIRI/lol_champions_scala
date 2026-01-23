# 🎮 Dataset 5 : League of Legends Champions

## 📋 Description

Statistiques de gameplay des champions de League of Legends basées sur les parties classées.

## 📊 Structure des données

### Fichiers fournis

- **data_clean.json** : 100 champions, données parfaites
- **data_dirty.json** : 500 champions avec :
  - Champs manquants (class, role)
  - Taux invalides (pickRate > 100%, winRate < 0%)
  - Difficulty hors limites (< 1 ou > 10)
  - Années de sortie impossibles (< 2009 ou > 2025)
  - Rôles/tiers invalides
  - Statistiques négatives
  - Doublons (même ID)
- **data_large.json** : 12 000 entrées (historique de patches)

## 🎯 Outputs attendus

[### 1. Statistiques générales

**Fichier** : `results.json`

```json
{
  "statistics": {
    "total_champions_parsed": ...,
    "total_champions_valid": ...,
    "parsing_errors": ...,
    "duplicates_removed": ...
  },
  "champions_by_role": {
    "Top": ...,
    "Jungle": ...,
    "Mid": ...,
    "ADC": ...,
    "Support": ...
  },
  "champions_by_tier": {
    "S": ...,
    "A": ...,
    "B": ...,
    "C": ...,
    "D": ...
  },
  "champions_by_class": {
    "Fighter": ...,
    "Mage": ...,
    "Assassin": ...,
    "Tank": ...,
    "Marksman": ...,
    "Support": ...
  },
  "highest_win_rate": [
    {
      "name": "...",
      "role": "...",
      "winRate": ...,
      "tier": "..."
    }
  ],
  "most_picked": [...],
  "most_banned": [...],
  "best_kda": [...],
  "average_win_rate_by_role": {
    "Top": ...,
    "Jungle": ...,
    "Mid": ...,
    "ADC": ...,
    "Support": ...
  },
  "average_difficulty_by_role": {
    "ADC": ...,
    "Mid": ...,
    "Jungle": ...,
    "Top": ...,
    "Support": ...
  },
  "most_popular_class": "...",
  "hardest_champions": [
    {"name": "...", "difficulty": ...}
  ]
}
```

### 2. Rapport texte

**Fichier** : `report.txt`

```
===============================================
  RAPPORT D'ANALYSE - LEAGUE OF LEGENDS
===============================================

📊 STATISTIQUES DE PARSING
---------------------------
- Entrées totales lues      : ...
- Entrées valides           : ...
- Erreurs de parsing        : ...
- Doublons supprimés        : ...

⚔️  RÉPARTITION PAR RÔLE
------------------------
- Support                   : ... champions
- Mid                       : ... champions
- Top                       : ... champions
- Jungle                    : ... champions
- ADC                       : ... champions

🏆 RÉPARTITION PAR TIER
------------------------
- S (Excellent)             : ... champions
- A (Très bon)              : ... champions
- B (Bon)                   : ... champions
- C (Moyen)                 : ... champions
- D (Faible)                : ... champions

🎭 RÉPARTITION PAR CLASSE
--------------------------
- Support                   : ... champions
- Mage                      : ... champions
- Fighter                   : ... champions
- Tank                      : ... champions
- Assassin                  : ... champions
- Marksman                  : ... champions

📈 TOP 10 - MEILLEUR WIN RATE
------------------------------
1. ...                      : ...% (Tier ...)
2. ...                      : ...% (Tier ...)
...

🔥 TOP 10 - PICK RATE
----------------------
1. ...                      : ...%
2. ...                      : ...%
...

🚫 TOP 10 - BAN RATE
---------------------
1. ...                      : ...%
2. ...                      : ...%
...

⭐ TOP 10 - MEILLEUR KDA
-------------------------
1. ...                      : ... KDA (.../.../....)
2. ...                      : ... KDA (.../.../....)
...

📊 MOYENNES PAR RÔLE
---------------------
WIN RATE MOYEN :
- Top                       : ...%
- Mid                       : ...%
- Support                   : ...%
- ADC                       : ...%
- Jungle                    : ...%

DIFFICULTÉ MOYENNE :
- ADC                       : .../10
- Mid                       : .../10
- Jungle                    : .../10
- Top                       : .../10
- Support                   : .../10

💰 ÉCONOMIE MOYENNE PAR RÔLE
-----------------------------
- ADC                       : ... or
- Mid                       : ... or
- Top                       : ... or
- Jungle                    : ... or
- Support                   : ... or

💥 DÉGÂTS MOYENS PAR RÔLE
--------------------------
- ADC                       : ...
- Mid                       : ...
- Top                       : ...
- Jungle                    : ...
- Support                   : ...

🎯 CHAMPIONS LES PLUS DIFFICILES
----------------------------------
1. ...                      : .../10
2. ...                      : .../10
...

🔝 CLASSE LA PLUS POPULAIRE
----------------------------
- ...                       : ... champions (...%)

⏱️  PERFORMANCE
---------------
- Temps de traitement       : ... secondes
- Entrées/seconde           : ...

===============================================
```

## 🔧 Transformations à implémenter

### Obligatoires

1. **Parsing et validation**
   - Parser les 3 fichiers
   - Valider : pickRate/banRate/winRate entre 0 et 100, difficulty entre 1 et 10
   - Rôles valides : "Top", "Jungle", "Mid", "ADC", "Support"
   - Tiers valides : "S", "A", "B", "C", "D"
   - Supprimer les doublons (par ID)

2. **Top 10**
   - Top 10 champions par win rate
   - Top 10 champions par pick rate
   - Top 10 champions par ban rate
   - Top 10 champions par KDA (calculé: (kills + assists) / deaths)

3. **Agrégations**
   - Compter champions par rôle
   - Compter champions par tier
   - Compter champions par classe
   - Win rate moyen par rôle
   - Difficulté moyenne par rôle

4. **Statistiques économiques**
   - Or moyen par rôle
   - Dégâts moyens par rôle

### Bonus (optionnel)

5. **Meta analysis**
   - Champions "contested" (pickRate + banRate > 30%)
   - Champions "sleeper OP" (winRate > 52% && pickRate < 5%)

6. **Analyse de difficulté**
   - Top 5 champions les plus difficiles
   - Corrélation difficulté/winRate

7. **Analyse par classe**
   - Classe la plus populaire
   - Classe avec le meilleur winRate moyen
   - Classe la plus bannie

## 💡 Conseil - Mesure de performance

```scala
val start = System.currentTimeMillis()
// ... traitement
val duration = (System.currentTimeMillis() - start) / 1000.0
println(f"Traitement effectué en $duration%.3f secondes")
```

## ✅ Critères de réussite

- ✅ Les 3 fichiers sont parsés sans erreur fatale
- ✅ Les rôles et tiers sont validés ou normalisés
- ✅ Les taux (%) sont dans la plage valide [0, 100]
- ✅ Le KDA est calculé correctement (gestion division par 0)
- ✅ Les 10 statistiques obligatoires sont présentes
- ✅ `results.json` est valide et bien formaté
- ✅ `report.txt` est lisible et structuré
- ✅ Le traitement de `data_large.json` prend < 10 secondes














