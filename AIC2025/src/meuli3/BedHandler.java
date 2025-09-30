package meuli3;

import aic2025.user.*;

public class BedHandler {

    // Busca la cama enemiga más cercana en visión. Devuelve null si no encuentra.
    public StructureInfo findNearestEnemyBed(UnitController uc) {
        try {
            int vision = uc.getVisionRange();
            float radiusSq = (float)vision * (float)vision;
            StructureInfo[] enemyBeds = uc.senseStructures(radiusSq, uc.getOpponent(), StructureType.BED);
            if (enemyBeds == null || enemyBeds.length == 0) return null;

            Location myLoc = uc.getLocation();
            StructureInfo nearest = null;
            int bestDist = Integer.MAX_VALUE;
            for (StructureInfo si : enemyBeds) {
                if (si == null) continue;
                Location loc = si.getLocation();
                int d = myLoc.distanceSquared(loc);
                if (d < bestDist) { bestDist = d; nearest = si; }
            }
            return nearest;
        } catch (Exception e) {
            return null;
        }
    }

    // Intenta romper la cama con AXE en la localización dada. Devuelve true si ejecutó la acción.
    public boolean tryUseAxeOnBed(UnitController uc, Location bedLoc) {
        try {
            if (bedLoc == null) return false;
            if (uc.hasCraftable(Craftable.AXE) && uc.canUseCraftable(Craftable.AXE, bedLoc)) {
                uc.useCraftable(Craftable.AXE, bedLoc);
                return true;
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }
}
