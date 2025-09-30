package baibars;

import aic2025.user.*;

/**
 * Service de combat de Baibars - Version simplifiée
 * Spécialisé dans la fabrication d'armes et les attaques tactiques
 */
public class BaibarsCombat {
    
    /**
     * Garantit qu'on a une hache, la fabrique si nécessaire
     */
    public boolean garantirHache(UnitController uc) {
        try {
            if (uc.hasCraftable(Craftable.AXE)) {
                return true;
            }
            
            if (uc.canCraft(Craftable.AXE)) {
                uc.craft(Craftable.AXE);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Garantit qu'on a une pioche, la fabrique si nécessaire
     */
    public boolean garantirPioche(UnitController uc) {
        try {
            if (uc.hasCraftable(Craftable.PICKAXE)) {
                return true;
            }
            
            if (uc.canCraft(Craftable.PICKAXE)) {
                uc.craft(Craftable.PICKAXE);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Exécute une frappe de harcèlement sur une cible
     */
    public boolean executerFrappeHarcelement(UnitController uc, Location cible) {
        try {
            // Utiliser la hache si possible
            if (uc.hasCraftable(Craftable.AXE) && uc.canUseCraftable(Craftable.AXE, cible)) {
                uc.useCraftable(Craftable.AXE, cible);
                return true;
            }
            
            // Utiliser la pioche en dernier recours
            if (uc.hasCraftable(Craftable.PICKAXE) && uc.canUseCraftable(Craftable.PICKAXE, cible)) {
                uc.useCraftable(Craftable.PICKAXE, cible);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Fabrique des outils de base si on a les ressources
     */
    public boolean fabriquerOutilsDeBase(UnitController uc) {
        try {
            boolean progres = false;
            
            // Essayer de fabriquer une hache d'abord
            if (!uc.hasCraftable(Craftable.AXE) && uc.canCraft(Craftable.AXE)) {
                uc.craft(Craftable.AXE);
                progres = true;
            }
            
            // Puis une pioche
            if (!uc.hasCraftable(Craftable.PICKAXE) && uc.canCraft(Craftable.PICKAXE)) {
                uc.craft(Craftable.PICKAXE);
                progres = true;
            }
            
            return progres;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Évalue si on peut engager le combat contre un ennemi
     */
    public boolean peutEngagerCombat(UnitController uc, Location ennemi) {
        try {
            // Vérifier qu'on a au moins une arme
            boolean aArme = uc.hasCraftable(Craftable.AXE) || uc.hasCraftable(Craftable.PICKAXE);
            
            // Vérifier la distance
            int distance = uc.getLocation().distanceSquared(ennemi);
            boolean assezProche = distance <= 2; // À portée d'attaque
            
            return aArme && assezProche;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Attaque prioritaire avec la meilleure arme disponible
     */
    public boolean attaquePrioritaire(UnitController uc, Location cible) {
        try {
            // Priorité 1: Hache
            if (uc.hasCraftable(Craftable.AXE) && uc.canUseCraftable(Craftable.AXE, cible)) {
                uc.useCraftable(Craftable.AXE, cible);
                return true;
            }
            
            // Priorité 2: Pioche
            if (uc.hasCraftable(Craftable.PICKAXE) && uc.canUseCraftable(Craftable.PICKAXE, cible)) {
                uc.useCraftable(Craftable.PICKAXE, cible);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}