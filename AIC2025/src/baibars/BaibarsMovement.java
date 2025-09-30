package baibars;

import aic2025.user.*;

/**
 * Service de mouvement tactique de Baibars - Version simplifiée
 * Spécialisé dans la mobilité et l'évitement d'oscillation
 */
public class BaibarsMovement {
    
    private int directionIndex = 0;
    private int toursSansChangement = 0;
    private Location dernierePositionExploration = null;
    
    /**
     * Se déplace vers une cible de manière intelligente en évitant l'oscillation
     */
    public boolean seDeplacerVersCibleIntelligent(UnitController uc, Location cible, Location dernierePosition, Direction derniereDirection) {
        try {
            Location maPosition = uc.getLocation();
            
            // Si on est déjà sur la cible, ne pas bouger
            if (maPosition.equals(cible)) {
                return true;
            }
            
            Direction directionOptimale = maPosition.directionTo(cible);
            
            // Essayer d'abord la direction optimale
            if (uc.canMove(directionOptimale)) {
                Location nouvellePos = maPosition.add(directionOptimale);
                // Éviter de revenir à la position précédente
                if (dernierePosition == null || !nouvellePos.equals(dernierePosition)) {
                    uc.move(directionOptimale);
                    return true;
                }
            }
            
            // Si la direction optimale est bloquée, essayer les directions adjacentes
            Direction[] alternatives = {
                directionOptimale.rotateLeft(),
                directionOptimale.rotateRight(),
                directionOptimale.rotateLeft().rotateLeft(),
                directionOptimale.rotateRight().rotateRight()
            };
            
            for (Direction dir : alternatives) {
                if (uc.canMove(dir)) {
                    Location nouvellePos = maPosition.add(dir);
                    if (dernierePosition == null || !nouvellePos.equals(dernierePosition)) {
                        uc.move(dir);
                        return true;
                    }
                }
            }
            
            // En dernier recours, n'importe quelle direction sauf celle d'où on vient
            Direction[] toutesDirections = Direction.values();
            for (Direction dir : toutesDirections) {
                if (dir != Direction.ZERO && uc.canMove(dir)) {
                    Location nouvellePos = maPosition.add(dir);
                    if (dernierePosition == null || !nouvellePos.equals(dernierePosition)) {
                        uc.move(dir);
                        return true;
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Exploration tactique méthodique avec anti-oscillation renforcée
     */
    public boolean explorationTactique(UnitController uc) {
        try {
            Location positionActuelle = uc.getLocation();
            
            // Détecter l'immobilité
            if (dernierePositionExploration != null && dernierePositionExploration.equals(positionActuelle)) {
                toursSansChangement++;
                if (toursSansChangement >= 2) {
                    // Forcer un changement radical de direction
                    directionIndex = (directionIndex + 2) % 8;
                    toursSansChangement = 0;
                }
            } else {
                toursSansChangement = 0;
            }
            
            dernierePositionExploration = positionActuelle;
            
            // Exploration en spirale depuis le centre de la carte
            Location centre = new Location(uc.getMapWidth()/2, uc.getMapHeight()/2);
            Direction versLeCentre = positionActuelle.directionTo(centre);
            
            // Si on est dans un coin (distance > 10 du centre), aller vers le centre
            int distanceDuCentre = Math.abs(positionActuelle.x - centre.x) + Math.abs(positionActuelle.y - centre.y);
            if (distanceDuCentre > 10) {
                if (uc.canMove(versLeCentre)) {
                    uc.move(versLeCentre);
                    return true;
                }
                // Si bloqué, essayer les directions adjacentes au mouvement vers le centre
                Direction[] adjacentes = {versLeCentre.rotateLeft(), versLeCentre.rotateRight()};
                for (Direction dir : adjacentes) {
                    if (uc.canMove(dir)) {
                        uc.move(dir);
                        return true;
                    }
                }
            }
            
            // Exploration normale : rotation systématique des directions
            Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, 
                                     Direction.NORTHEAST, Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.NORTHWEST};
            
            // Essayer 3 directions consécutives pour éviter les blocages
            for (int i = 0; i < 3; i++) {
                Direction dir = directions[(directionIndex + i) % directions.length];
                if (uc.canMove(dir)) {
                    uc.move(dir);
                    // Changer de direction principale tous les quelques mouvements
                    if (i == 0) {
                        directionIndex = (directionIndex + 1) % directions.length;
                    }
                    return true;
                }
            }
            
            // Si toujours bloqué, essayer toutes les directions
            for (Direction dir : directions) {
                if (uc.canMove(dir)) {
                    uc.move(dir);
                    directionIndex = (directionIndex + 2) % directions.length; // Saut plus important
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Mouvement de repli tactique
     */
    public boolean mouvementDeRepli(UnitController uc) {
        try {
            // Essayer de se déplacer dans une direction aléatoire pour sortir du blocage
            return explorationTactique(uc);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Évite un ennemi en se déplaçant dans la direction opposée
     */
    public boolean eviterEnnemi(UnitController uc, Location positionEnnemi) {
        try {
            Location maPosition = uc.getLocation();
            Direction directionEnnemi = maPosition.directionTo(positionEnnemi);
            Direction directionOpposee = directionEnnemi.opposite();
            
            if (uc.canMove(directionOpposee)) {
                uc.move(directionOpposee);
                return true;
            }
            
            // Si impossible, essayer une direction perpendiculaire
            Direction[] perpendiculaires = {directionOpposee.rotateLeft(), directionOpposee.rotateRight()};
            for (Direction dir : perpendiculaires) {
                if (uc.canMove(dir)) {
                    uc.move(dir);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}