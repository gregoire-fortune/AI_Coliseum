# Package Baibars - Bot AI Coliseum 2025

## Architecture Modulaire

Le bot Baibars utilise une architecture modulaire inspirée des tactiques militaires du sultan Baibars Ier (1223-1277), conquistador légendaire du Moyen-Orient.

### Fichiers du Package

#### **UnitPlayer.java** 
- **Rôle** : Contrôleur principal et coordinateur
- **Fonction** : Orchestre tous les services et gère le cycle de vie du bot
- **Statut** : ✅ ESSENTIEL - Point d'entrée principal

#### **BaibarsIntelligence.java**
- **Rôle** : Service de renseignement et reconnaissance
- **Fonction** : Détection des ennemis, lits adverses et analyse tactique
- **Statut** : ✅ ESSENTIEL - Reconnaissance battlefield

#### **BaibarsMovement.java**
- **Rôle** : Service de mouvement tactique
- **Fonction** : Pathfinding, anti-oscillation, exploration méthodique
- **Statut** : ✅ ESSENTIEL - Mobilité et positionnement

#### **BaibarsCombat.java**
- **Rôle** : Service de combat et fabrication
- **Fonction** : Craft d'armes, attaque de structures, gestion combat
- **Statut** : ✅ ESSENTIEL - Capacité offensive

#### **BaibarsTactics.java**
- **Rôle** : Service de stratégie temporelle
- **Fonction** : Gestion des phases, timing optimal, adaptation 2000-tours
- **Statut** : ✅ ESSENTIEL - Planification stratégique

#### **BaibarsResources.java**
- **Rôle** : Service de gestion des ressources
- **Fonction** : Localisation et optimisation de la collecte
- **Statut** : ✅ ESSENTIEL - Économie et accumulation

## Système de Phases

1. **Phase Préparatoire** (tours 0-200) : Collecte prioritaire et équipement
2. **Phase Offensive** (tours 200-1600) : Attaque coordonnée des lits ennemis  
3. **Phase Finale** (tours 1600-2000) : Offensive tous azimuts

## Stratégies Clés

- **Anti-oscillation** : Système de mémoire pour éviter les mouvements répétitifs
- **Collecte prioritaire** : Focus absolu sur l'accumulation de poids (Total Weight)
- **Exploration intelligente** : Évitement des coins, retour vers le centre
- **Combat adaptatif** : Fabrication d'armes et attaque ciblée

## Performance

- ✅ Compilation réussie
- ✅ Victoires contre demoplayer (par Total Weight)
- ✅ Anti-oscillation fonctionnel  
- ⚠️ Optimisation collecte en cours

---

*"La victoire appartient à celui qui sait quand attaquer et quand se retirer"* - Inspiré des tactiques de Baibars