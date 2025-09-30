package adolf;

import aic2025.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();
    
    // Variables globales pour éviter les recalculs
    private int mapWidth;
    private int mapHeight;
    private int centerX;
    private int centerY;
    private Location centerLocation;
    private Location firstBed;
    private Location firstEnemyBed;
    private int firstEnemyBedMessage;
    private String detectedSymmetryType;
    private boolean isInitialized = false;

    /**
     * Détermine la position du lit ennemi en fonction du type de symétrie de la carte
     * @param uc le UnitController
     * @param myBed la position de notre lit
     * @return la position estimée du lit ennemi
     */
    public Location calculateEnemyBedPosition(UnitController uc, Location myBed) {
        // Calculs directs des trois symétries possibles
        int centralX = mapWidth - myBed.x - 1;
        int centralY = mapHeight - myBed.y - 1;
        int horizontalY = mapHeight - myBed.y - 1;
        int verticalX = mapWidth - myBed.x - 1;
        
        // Distances au carré pour éviter sqrt()
        int centerDx = centerX - myBed.x;
        int centerDy = centerY - myBed.y;
        int myDist = centerDx * centerDx + centerDy * centerDy;
        
        // Distance centrale
        centerDx = centerX - centralX;
        centerDy = centerY - centralY;
        int centralDiff = Math.abs(myDist - (centerDx * centerDx + centerDy * centerDy));
        
        // Distance horizontale
        centerDx = centerX - myBed.x;
        centerDy = centerY - horizontalY;
        int horizontalDiff = Math.abs(myDist - (centerDx * centerDx + centerDy * centerDy));
        
        // Distance verticale
        centerDx = centerX - verticalX;
        centerDy = centerY - myBed.y;
        int verticalDiff = Math.abs(myDist - (centerDx * centerDx + centerDy * centerDy));
        
        // Sélection directe de la meilleure symétrie
        if (horizontalDiff < centralDiff && horizontalDiff <= verticalDiff) {
            detectedSymmetryType = "horizontale";
            return new Location(myBed.x, horizontalY);
        }
        if (verticalDiff < centralDiff) {
            detectedSymmetryType = "verticale";
            return new Location(verticalX, myBed.y);
        }
        detectedSymmetryType = "centrale";
        return new Location(centralX, centralY);
    }

    public void run(UnitController uc) {
        // Initialisation une seule fois
        if (!isInitialized) {
            mapWidth = uc.getMapWidth();
            mapHeight = uc.getMapHeight();
            centerX = mapWidth >> 1; // Division par 2 optimisée
            centerY = mapHeight >> 1;
            centerLocation = new Location(centerX, centerY);
            firstBed = uc.getAllBedsInfo()[0].getLocation();
            firstEnemyBed = calculateEnemyBedPosition(uc, firstBed);
            firstEnemyBedMessage = firstEnemyBed.x * 100 + firstEnemyBed.y;
            isInitialized = true;
        }
        
        while (true) {
            // Spawn si pas sur la carte
            if (!uc.isOnMap()) {
                BedInfo[] beds = uc.getAllBedsInfo();
                for (BedInfo bed : beds) {
                    if (uc.canSpawn(bed.getLocation())) {
                        uc.spawn(bed.getLocation());
                        break;
                    }
                }
            }

            // Action principale - message compact
            uc.println(firstEnemyBedMessage);

            // Mouvement aléatoire
            Direction dir = directions[(int)(uc.getRandomDouble() * 8)];
            if (uc.canMove(dir)) uc.move(dir);

            // Craft aléatoire
            Craftable[] craftables = Craftable.values();
            Craftable craftable = craftables[(int)(uc.getRandomDouble() * craftables.length)];
            if (uc.canCraft(craftable)) uc.craft(craftable);

            // Gather si possible
            if (uc.canGather()) uc.gather();

            uc.yield();
        }
    }
}