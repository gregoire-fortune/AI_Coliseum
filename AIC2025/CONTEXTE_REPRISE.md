# 🎯 CONTEXTE COMPLET - Bot Baibars AI Coliseum 2025

## 📋 PROMPT DE REPRISE POUR L'ASSISTANT IA

Copier-coller ce texte dans votre prochaine conversation avec GitHub Copilot :

---

**CONTEXTE PROJET :** Je développe un bot IA pour AI Coliseum 2025 appelé "Baibars" inspiré du sultan militaire. Le bot est dans `c:\Users\Utilisateur\IdeaProjects\AI_Coliseum\AIC2025\src\baibars\` sur Windows avec PowerShell.

**ARCHITECTURE ACTUELLE :**
- `UnitPlayer.java` : Contrôleur principal avec phases tactiques
- `BaibarsIntelligence.java` : Service de reconnaissance ennemie 
- `BaibarsMovement.java` : Service de mouvement anti-oscillation
- `BaibarsCombat.java` : Service de combat et fabrication d'armes
- `BaibarsTactics.java` : Service de stratégie temporelle (phases 0-200-1600-2000)
- `BaibarsResources.java` : Service de gestion des ressources

**COMPILATION :** `ant compile -Dpackage=baibars`
**TEST :** `ant run -Dpackage1=baibars -Dpackage2=demoplayer -Dmap=Basic1`

**PROBLÈMES IDENTIFIÉS :**
1. ✅ RÉSOLU : Oscillation et mouvements erratiques (système anti-oscillation implémenté)
2. ✅ RÉSOLU : Architecture chaotique (fichiers organisés et renommés)
3. ⚠️ EN COURS : Bot perd par "Total Weight" - collecte de ressources insuffisante

**DERNIERS DÉVELOPPEMENTS :**
- Architecture modulaire Baibars complète et fonctionnelle
- Système de phases accéléré pour parties de 2000 tours
- Anti-oscillation avec retour vers centre de carte si dans un coin
- Priorité absolue à la collecte (uc.gather() avec return immédiat)
- Exploration intelligente évitant les coins de map

**OBJECTIF PRINCIPAL :** Optimiser la stratégie de collecte pour gagner par "Total Weight" contre demoplayer.

**STYLE DE CODE :** Thème militaire avec références à Baibars, commentaires en français, architecture modulaire.

Aide-moi à continuer le développement du bot Baibars là où nous nous sommes arrêtés.

---

## 📊 HISTORIQUE DES SESSIONS DE DÉVELOPPEMENT

### Session 1 (29 Sept 2025)
**Demande initiale :** "comment un uc peut taper un lit adin de le détruire"

**Développements :**
1. ✅ Création de l'architecture modulaire Baibars (6 services)
2. ✅ Implémentation système anti-oscillation 
3. ✅ Phase-based strategy avec timing ultra-accéléré (200/500/1600/2000)
4. ✅ Premier succès contre demoplayer sur Basic2
5. ✅ Résolution des mouvements erratiques (feedback utilisateur)
6. ✅ Réorganisation complète du package (suppression fichiers inutiles)

**Victoires :** 1 victoire contre demoplayer (Basic1, Total Weight)
**Défaites :** Plusieurs défaites par Total Weight (collecte insuffisante)

**Dernière compilation réussie :** ✅ 6 fichiers source
**Dernier test :** ❌ Défaite vs demoplayer sur Basic1 par Total Weight

## 🔧 COMMANDES ESSENTIELLES

```powershell
# Navigation
cd "c:\Users\Utilisateur\IdeaProjects\AI_Coliseum\AIC2025"

# Compilation
ant compile -Dpackage=baibars

# Tests
ant run -Dpackage1=baibars -Dpackage2=demoplayer -Dmap=Basic1
ant run -Dpackage1=baibars -Dpackage2=demoplayer -Dmap=Basic2  
ant run -Dpackage1=baibars -Dpackage2=demoplayer -Dmap=testBed

# Vérification structure
Get-ChildItem "c:\Users\Utilisateur\IdeaProjects\AI_Coliseum\AIC2025\src\baibars\"
```

## 🚨 POINTS D'ATTENTION

1. **API AIC2025 :** Utiliser `import aic2025.user.*;` - pas de variables statiques autorisées
2. **Phases :** Timing critique pour 2000 tours (10%/15%/55%/20% répartition)  
3. **Anti-oscillation :** Système avec mémoire 3-positions implémenté
4. **Collecte :** Priorité absolue avec `return` après chaque `uc.gather()`

## 📈 PROCHAINES ÉTAPES SUGGÉRÉES

1. **Analyser pourquoi Total Weight insuffisant** (debug collecte)
2. **Optimiser BaibarsResources.java** (meilleure détection ressources)
3. **Tester contre autres adversaires** (meuli3, nullplayer)
4. **Affiner timing des phases** si nécessaire
5. **Optimiser pathfinding vers ressources**

## 🎖️ PHILOSOPHIE BAIBARS

*"La patience dans la collecte, la vitesse dans l'attaque, la ruse dans le mouvement"*

Le bot Baibars privilégie :
- 🧠 Intelligence tactique (phases adaptatives)
- ⚡ Rapidité d'exécution (2000 tours)
- 🎯 Objectifs clairs (Total Weight > destruction lits)
- 🛡️ Stabilité (anti-oscillation)
- 💰 Économie de guerre (collecte prioritaire)

---

**Date de création :** 29 septembre 2025  
**Statut :** Architecture stable, optimisation collecte nécessaire  
**Branche Git :** Baibars  
**Prêt pour reprise de développement :** ✅