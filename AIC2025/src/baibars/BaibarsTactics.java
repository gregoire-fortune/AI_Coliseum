package baibars;

import aic2025.user.*;

/**
 * Service tactique de Baibars - Version rapide pour 2000 tours
 * Phases accélérées pour maximiser l'efficacité en partie courte
 */
public class BaibarsTactics {
    
    // Phases tactiques optimisées pour 2000 tours
    public final int PHASE_RECONNAISSANCE = 1;     // Tours 0-400 : Explorer et s'équiper
    public final int PHASE_CONSOLIDATION = 2;      // Tours 401-800 : Renforcer position
    public final int PHASE_HARCELEMENT = 3;        // Tours 801-1500 : Attaques rapides
    public final int PHASE_ASSAUT_FINAL = 4;       // Tours 1501-2000 : Offensive totale
    
    /**
     * Détermine la phase tactique actuelle selon le tour - Version ultra rapide
     */
    public int determinerPhaseActuelle(UnitController uc) {
        try {
            int tourActuel = uc.getRound();
            int tourMax = 2000; // Partie de 2000 tours
            
            // Répartition temporelle ultra-accélérée pour plus d'agressivité
            if (tourActuel <= 200) {
                return PHASE_RECONNAISSANCE;    // 10% du temps : exploration rapide
            } else if (tourActuel <= 500) {
                return PHASE_CONSOLIDATION;     // 15% du temps : équipement
            } else if (tourActuel <= 1600) {
                return PHASE_HARCELEMENT;       // 55% du temps : attaques continues
            } else {
                return PHASE_ASSAUT_FINAL;      // 20% du temps : offensive finale
            }
        } catch (Exception e) {
            return PHASE_RECONNAISSANCE; // Par défaut
        }
    }
    
    /**
     * Calcule la position optimale pour attaquer un lit ennemi
     */
    public Location calculerPositionLitEnnemi(UnitController uc, Location positionLit) {
        try {
            Location maPosition = uc.getLocation();
            
            // Se rapprocher au maximum du lit pour l'attaquer
            Direction directionVersLit = maPosition.directionTo(positionLit);
            Location positionOptimale = maPosition.add(directionVersLit);
            
            return positionOptimale;
        } catch (Exception e) {
            return positionLit;
        }
    }
    
    /**
     * Évalue l'urgence d'une action selon la phase et le temps restant
     */
    public int evaluerUrgence(UnitController uc) {
        try {
            int tourActuel = uc.getRound();
            int tourMax = 2000;
            float progressionPartie = (float)tourActuel / (float)tourMax;
            
            if (progressionPartie < 0.2f) {
                return 1; // Faible urgence - exploration
            } else if (progressionPartie < 0.4f) {
                return 2; // Urgence modérée - équipement
            } else if (progressionPartie < 0.75f) {
                return 3; // Urgence élevée - attaques
            } else {
                return 4; // Urgence maximale - tout ou rien
            }
        } catch (Exception e) {
            return 2;
        }
    }
    
    /**
     * Détermine si c'est le moment de passer à l'offensive
     */
    public boolean momentOffensif(UnitController uc) {
        try {
            int tourActuel = uc.getRound();
            return tourActuel >= 200; // À partir du tour 200, être agressif (plus tôt)
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Détermine si c'est la phase finale critique
     */
    public boolean phaseFinaleCritique(UnitController uc) {
        try {
            int tourActuel = uc.getRound();
            return tourActuel >= 1600; // 400 derniers tours = phase critique (plus tôt)
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Calcule le temps restant en pourcentage
     */
    public float calculerTempsRestant(UnitController uc) {
        try {
            int tourActuel = uc.getRound();
            int tourMax = 2000;
            return 1.0f - ((float)tourActuel / (float)tourMax);
        } catch (Exception e) {
            return 0.5f;
        }
    }
    
    /**
     * Détermine la priorité d'action selon la phase
     */
    public String determinerPrioriteAction(UnitController uc) {
        try {
            int phase = determinerPhaseActuelle(uc);
            
            switch (phase) {
                case 1: // RECONNAISSANCE
                    return "EXPLORER_EQUIPER";
                case 2: // CONSOLIDATION  
                    return "RENFORCER_POSITION";
                case 3: // HARCELEMENT
                    return "ATTAQUER_LITS";
                case 4: // ASSAUT_FINAL
                    return "OFFENSIVE_TOTALE";
                default:
                    return "EXPLORER_EQUIPER";
            }
        } catch (Exception e) {
            return "EXPLORER_EQUIPER";
        }
    }
}