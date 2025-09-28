package baibars;

import aic2025.user.*;

/**
 * Service de renseignement et détection de Baibars - Version simplifiée
 * Spécialisé dans la reconnaissance ennemie et l'analyse tactique
 */
public class BaibarsIntelligence {
    
    /**
     * Trouve le lit ennemi le plus proche
     */
    public StructureInfo trouverLitEnnemiLePlusProche(UnitController uc) {
        try {
            int vision = uc.getVisionRange();
            float radiusSq = (float)vision * (float)vision;
            StructureInfo[] structures = uc.senseStructures(radiusSq, uc.getOpponent(), StructureType.BED);
            
            if (structures == null || structures.length == 0) return null;
            
            Location maPosition = uc.getLocation();
            StructureInfo litLePlusProche = null;
            int distanceMin = Integer.MAX_VALUE;
            
            for (StructureInfo structure : structures) {
                int distance = maPosition.distanceSquared(structure.getLocation());
                if (distance < distanceMin) {
                    distanceMin = distance;
                    litLePlusProche = structure;
                }
            }
            
            return litLePlusProche;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Tente d'attaquer un lit avec une hache
     */
    public boolean attaquerLitAvecHache(UnitController uc, Location positionLit) {
        try {
            if (uc.hasCraftable(Craftable.AXE) && uc.canUseCraftable(Craftable.AXE, positionLit)) {
                uc.useCraftable(Craftable.AXE, positionLit);
                return true;
            }
        } catch (Exception e) {
            // Ignorer les erreurs d'attaque
        }
        return false;
    }
    
    /**
     * Détecte les unités ennemies dans la zone
     */
    public UnitInfo[] detecterUnitesEnnemies(UnitController uc, int rayon) {
        try {
            float radiusSq = (float)rayon * (float)rayon;
            return uc.senseUnits(radiusSq, uc.getOpponent());
        } catch (Exception e) {
            return new UnitInfo[0];
        }
    }
    
    /**
     * Évalue la menace dans une zone donnée
     */
    public int evaluerMenace(UnitController uc, Location position, int rayon) {
        try {
            int scoreMenace = 0;
            
            // Détecter les unités ennemies
            UnitInfo[] unitesEnnemies = detecterUnitesEnnemies(uc, rayon);
            scoreMenace += unitesEnnemies.length * 20; // 20 points par unité
            
            return Math.min(100, scoreMenace);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Détermine si une zone est sûre pour établir une base
     */
    public boolean zoneSure(UnitController uc, Location position, int rayon) {
        try {
            int scoreMenace = evaluerMenace(uc, position, rayon);
            return scoreMenace < 20; // Seuil de sécurité
        } catch (Exception e) {
            return false;
        }
    }
}