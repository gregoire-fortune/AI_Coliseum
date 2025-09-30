package meuli3;

import aic2025.user.*;

public class ResourceFinder {

    // Busca la ubicación más cercana de un material dado dentro del radio
    public Location findNearestMaterial(UnitController uc, Material mat, float radiusSq) {
        try {
            Location[] locs = uc.senseMaterials(mat, radiusSq);
            if (locs == null || locs.length == 0) return null;
            Location myLoc = uc.getLocation();
            Location best = null;
            int bestDist = Integer.MAX_VALUE;
            for (Location l : locs) {
                if (l == null) continue;
                int d = myLoc.distanceSquared(l);
                if (d < bestDist) { bestDist = d; best = l; }
            }
            return best;
        } catch (Exception e) {
            return null;
        }
    }

}
