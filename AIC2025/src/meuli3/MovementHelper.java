package meuli3;

import aic2025.user.*;

public class MovementHelper {

    private final Direction[] directions = Direction.values();

    // Intenta mover hacia la dirección indicada, devuelve true si se movió
    public boolean moveTowards(UnitController uc, Direction dir) {
        try {
            if (dir != null && dir != Direction.ZERO && uc.canMove(dir)) {
                uc.move(dir);
                return true;
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }

    // Movimiento fallback: moverse en cualquier dirección posible
    public boolean randomFallbackMove(UnitController uc) {
        try {
            for (Direction d : directions) {
                if (d != Direction.ZERO && uc.canMove(d)) {
                    uc.move(d);
                    return true;
                }
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }

    // Mover hacia una ubicación objetivo (calcula la dirección y usa moveTowards)
    public boolean moveToLocation(UnitController uc, Location loc) {
        try {
            if (loc == null) return false;
            Direction to = uc.getLocation().directionTo(loc);
            return moveTowards(uc, to);
        } catch (Exception e) {
            return false;
        }
    }

    // Movimiento aleatorio escogiendo una dirección válida de forma pseudoaleatoria
    public boolean randomMove(UnitController uc) {
        try {
            java.util.List<Direction> options = new java.util.ArrayList<>();
            for (Direction dir : directions) {
                if (dir != Direction.ZERO && uc.canMove(dir)) options.add(dir);
            }
            if (options.isEmpty()) return false;
            int idx = (int)(uc.getRandomDouble() * options.size());
            if (idx >= options.size()) idx = options.size() - 1;
            uc.move(options.get(idx));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
