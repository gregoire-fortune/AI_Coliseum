package baibars;

import aic2025.user.*;

public class UnitPlayer {
    
    // Variables pour l'anti-oscillation
    private Location dernierePosition = null;
    private Location avantDernierePosition = null;
    private int compteurMemePosition = 0;
    private boolean etatBloqueDetecte = false;
    
    // Services Baibars
    private BaibarsIntelligence renseignement = new BaibarsIntelligence();
    private BaibarsMovement mouvement = new BaibarsMovement();
    private BaibarsCombat combat = new BaibarsCombat();
    private BaibarsTactics tactiques = new BaibarsTactics();
    private BaibarsResources ressources = new BaibarsResources();
    
    public void run(UnitController uc) {
        
        while (true) {
            try {
                // Vérifier si on est sur la carte
                if (!uc.isOnMap()) {
                    BedInfo[] lits = uc.getAllBedsInfo();
                    for (BedInfo lit : lits) {
                        if (uc.canSpawn(lit.getLocation())) {
                            uc.spawn(lit.getLocation());
                        }
                    }
                    uc.yield();
                    continue;
                }
                
                // Mettre à jour la mémoire de mouvement
                mettreAJourMemoire(uc);
                
                // Déterminer la stratégie selon la phase actuelle
                int phase = tactiques.determinerPhaseActuelle(uc);
                String priorite = tactiques.determinerPrioriteAction(uc);
                boolean phaseCritique = tactiques.phaseFinaleCritique(uc);
                
                // Adapter le comportement selon la phase
                if (phaseCritique) {
                    // Phase finale : attaque tous azimuts
                    executerOffensiveFinale(uc);
                } else if (tactiques.momentOffensif(uc)) {
                    // Phase offensive : priorité aux attaques
                    executerPhaseOffensive(uc);
                } else {
                    // Phase préparatoire : équipement et positionnement
                    executerPhasePreparatoire(uc);
                }
                
            } catch (Exception e) {
                // En cas d'erreur, ne rien faire
            }
            
            uc.yield();
        }
    }
    
    /**
     * Phase préparatoire : équipement et exploration rapide
     */
    private void executerPhasePreparatoire(UnitController uc) {
        try {
            // Priorité absolue : Collecter des ressources pour le poids
            if (uc.canGather()) {
                uc.gather();
                return; // Collecte prioritaire, ne rien faire d'autre ce tour
            }
            
            // Priorité 2 : Vérifier s'il y a des ressources adjacentes et s'y déplacer
            Location position = uc.getLocation();
            for (Direction dir : Direction.values()) {
                if (dir == Direction.ZERO) continue;
                try {
                    Location cible = position.add(dir);
                    if (uc.canMove(dir) && uc.canSenseLocation(cible)) {
                        // Se déplacer vers une case que l'on peut explorer
                        uc.move(dir);
                        return;
                    }
                } catch (Exception e) {
                    // Continuer la recherche
                }
            }
            
            // Priorité 3 : Chercher des ressources à proximité avec BaibarsRessources
            Location ressourcesProches = ressources.trouverRessourcesLePlusProche(uc);
            if (ressourcesProches != null && !ressourcesProches.equals(uc.getLocation())) {
                if (mouvement.seDeplacerVersCibleIntelligent(uc, ressourcesProches, dernierePosition, Direction.ZERO)) {
                    return;
                }
            }
            
            // Priorité 4 : Fabriquer des outils de base
            combat.fabriquerOutilsDeBase(uc);
            
            // Priorité 5 : Chercher des lits seulement si on a une hache
            if (uc.hasCraftable(Craftable.AXE)) {
                StructureInfo litEnnemi = renseignement.trouverLitEnnemiLePlusProche(uc);
                if (litEnnemi != null) {
                    seDeplacerVersLitIntelligent(uc, litEnnemi.getLocation());
                    combat.attaquePrioritaire(uc, litEnnemi.getLocation());
                    return;
                }
            }
            
            // Priorité 6 : Exploration pour trouver plus de ressources
            mouvement.explorationTactique(uc);
            
        } catch (Exception e) {
            mouvement.explorationTactique(uc);
        }
    }
    
    /**
     * Phase offensive : attaque prioritaire des lits MAIS toujours collecter
     */
    private void executerPhaseOffensive(UnitController uc) {
        try {
            // Priorité 1 : TOUJOURS collecter des ressources si possible
            if (uc.canGather()) {
                uc.gather();
                return; // Collecte prioritaire même en phase offensive
            }
            
            // Chercher des lits ennemis en priorité
            StructureInfo litEnnemi = renseignement.trouverLitEnnemiLePlusProche(uc);
            
            if (litEnnemi != null) {
                Location cible = litEnnemi.getLocation();
                
                // S'assurer d'avoir une arme
                if (!uc.hasCraftable(Craftable.AXE) && !uc.hasCraftable(Craftable.PICKAXE)) {
                    combat.fabriquerOutilsDeBase(uc);
                }
                
                // Se déplacer vers le lit de manière déterminée
                seDeplacerVersLitIntelligent(uc, cible);
                
                // Attaquer si possible
                combat.attaquePrioritaire(uc, cible);
            } else {
                // Pas de lit visible : chercher des ressources d'abord
                Location ressourcesProches = ressources.trouverRessourcesLePlusProche(uc);
                if (ressourcesProches != null && !ressourcesProches.equals(uc.getLocation())) {
                    mouvement.seDeplacerVersCibleIntelligent(uc, ressourcesProches, dernierePosition, Direction.ZERO);
                } else {
                    // Exploration pour trouver ressources ou lits
                    mouvement.explorationTactique(uc);
                }
            }
        } catch (Exception e) {
            mouvement.explorationTactique(uc);
        }
    }
    
    /**
     * Phase finale critique : offensive totale
     */
    private void executerOffensiveFinale(UnitController uc) {
        try {
            // En phase finale, attaquer à tout prix
            StructureInfo litEnnemi = renseignement.trouverLitEnnemiLePlusProche(uc);
            
            if (litEnnemi != null) {
                // Mouvement direct vers le lit, même risqué
                Location cible = litEnnemi.getLocation();
                
                // Essayer d'attaquer d'abord
                if (combat.attaquePrioritaire(uc, cible)) {
                    return; // Attaque réussie
                }
                
                // Sinon se rapprocher au maximum
                Direction directionLit = uc.getLocation().directionTo(cible);
                if (uc.canMove(directionLit)) {
                    uc.move(directionLit);
                } else {
                    // Essayer les directions adjacentes
                    Direction[] alternatives = {
                        directionLit.rotateLeft(),
                        directionLit.rotateRight(),
                        directionLit.rotateLeft().rotateLeft(),
                        directionLit.rotateRight().rotateRight()
                    };
                    
                    for (Direction dir : alternatives) {
                        if (uc.canMove(dir)) {
                            uc.move(dir);
                            break;
                        }
                    }
                }
            } else {
                // Exploration ultra-aggressive pour trouver des lits
                mouvement.explorationTactique(uc);
            }
        } catch (Exception e) {
            // En derniers recours : mouvement aléatoire
            mouvement.explorationTactique(uc);
        }
    }
    
    private void mettreAJourMemoire(UnitController uc) {
        try {
            Location positionActuelle = uc.getLocation();
            
            if (dernierePosition != null && dernierePosition.equals(positionActuelle)) {
                compteurMemePosition++;
                if (compteurMemePosition >= 3) {
                    etatBloqueDetecte = true;
                }
            } else {
                compteurMemePosition = 0;
                etatBloqueDetecte = false;
            }
            
            avantDernierePosition = dernierePosition;
            dernierePosition = positionActuelle;
            
        } catch (Exception e) {
            compteurMemePosition = 0;
            etatBloqueDetecte = false;
        }
    }
    
    private void seDeplacerVersLitIntelligent(UnitController uc, Location cible) {
        try {
            // Anti-oscillation renforcé
            if (etatBloqueDetecte || (dernierePosition != null && avantDernierePosition != null)) {
                // Si on oscille entre deux positions, forcer une direction différente
                if (dernierePosition.equals(avantDernierePosition)) {
                    Direction[] directionsSecours = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
                    for (Direction dir : directionsSecours) {
                        Location nouvelleDest = uc.getLocation().add(dir);
                        if (uc.canMove(dir) && !nouvelleDest.equals(dernierePosition) && !nouvelleDest.equals(avantDernierePosition)) {
                            uc.move(dir);
                            etatBloqueDetecte = false;
                            return;
                        }
                    }
                }
                
                // Utiliser l'exploration anti-oscillation
                mouvement.explorationTactique(uc);
                etatBloqueDetecte = false;
                return;
            }
            
            // Utiliser le système de mouvement intelligent de Baibars
            if (mouvement.seDeplacerVersCibleIntelligent(uc, cible, dernierePosition, Direction.ZERO)) {
                return; // Mouvement réussi
            }
            
            // Si le mouvement intelligent échoue, essayer une approche directe
            Direction directionCible = uc.getLocation().directionTo(cible);
            if (uc.canMove(directionCible)) {
                Location nouvelleDest = uc.getLocation().add(directionCible);
                if (dernierePosition == null || !nouvelleDest.equals(dernierePosition)) {
                    uc.move(directionCible);
                    return;
                }
            }
            
            // En dernier recours, exploration pour contourner l'obstacle
            mouvement.explorationTactique(uc);
            
        } catch (Exception e) {
            // Fallback: mouvement simple sans oscillation
            try {
                Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
                for (Direction dir : directions) {
                    Location nouvelleDest = uc.getLocation().add(dir);
                    if (uc.canMove(dir) && (dernierePosition == null || !nouvelleDest.equals(dernierePosition))) {
                        uc.move(dir);
                        break;
                    }
                }
            } catch (Exception e2) {
                // Ne rien faire si tout échoue
            }
        }
    }
    
    private void explorerTerrain(UnitController uc) {
        try {
            Direction[] directions = Direction.values();
            
            for (Direction dir : directions) {
                if (dir != Direction.ZERO && uc.canMove(dir)) {
                    Location nouvellePos = uc.getLocation().add(dir);
                    
                    if (avantDernierePosition == null || !nouvellePos.equals(avantDernierePosition)) {
                        uc.move(dir);
                        return;
                    }
                }
            }
            
        } catch (Exception e) {
            // Ne rien faire si tout échoue
        }
    }
}