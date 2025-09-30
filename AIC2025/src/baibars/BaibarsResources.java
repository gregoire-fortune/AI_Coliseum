package baibars;

import aic2025.user.*;

/**
 * Module de gestion des ressources pour le clan Baibars
 * Version simplifiée pour éviter les erreurs d'API
 */
public class BaibarsResources {
    
    /**
     * Trouve une direction vers des ressources si possible
     */
    public Location trouverRessourcesLePlusProche(UnitController uc) {
        try {
            Location position = uc.getLocation();
            
            // D'abord vérifier les cases adjacentes
            for (Direction dir : Direction.values()) {
                if (dir == Direction.ZERO) continue;
                try {
                    Location cible = position.add(dir);
                    if (uc.canSenseLocation(cible)) {
                        return cible; // Retourner une case explorable
                    }
                } catch (Exception e) {
                    // Continuer
                }
            }
            
            // Ensuite chercher dans un rayon plus large
            int rayon = 5; // Rayon fixe pour éviter les erreurs d'API
            Location meilleure = null;
            int meilleureDistance = Integer.MAX_VALUE;
            
            for (int dx = -rayon; dx <= rayon; dx++) {
                for (int dy = -rayon; dy <= rayon; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    
                    try {
                        Location cible = new Location(position.x + dx, position.y + dy);
                        if (uc.canSenseLocation(cible)) {
                            int distance = Math.abs(dx) + Math.abs(dy);
                            if (distance < meilleureDistance) {
                                meilleureDistance = distance;
                                meilleure = cible;
                            }
                        }
                    } catch (Exception e) {
                        // Continuer la recherche
                    }
                }
            }
            
            return meilleure;
            
        } catch (Exception e) {
            return null;
        }
    }
}