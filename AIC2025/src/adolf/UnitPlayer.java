package adolf;

import aic2025.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();

    public void run(UnitController uc) {
        // Code to be executed only at the beginning of the unit's lifespan
        
        int mapWidth = uc.getMapWidth();
        int mapHeight = uc.getMapHeight();
        int centerX = mapWidth / 2;
        int centerY = mapHeight / 2;

        Location centerLocation = new Location(centerX, centerY);
        BedInfo[] beds = uc.getAllBedsInfo();

        Location firstBed = beds[0].getLocation();
        int firstBedX = firstBed.x;
        int firstBedY = firstBed.y;

        Location firstEnemyBed = new Location(mapWidth - firstBedX - 1, mapHeight - firstBedY - 1);
        int firstEnemyBedX = firstEnemyBed.x;
        int firstEnemyBedY = firstEnemyBed.y;


        //Pre-Message
        int firstEnemyBedMessage = firstEnemyBedX * 100 + firstEnemyBedY;
        


        while (true) {
            // Code to be executed every round

            /*If not on the map, find an empty bed and spawn there*/
            if (!uc.isOnMap()){
                beds = uc.getAllBedsInfo();
                for (BedInfo bed : beds){
                    if (uc.canSpawn(bed.getLocation())) uc.spawn(bed.getLocation());
                }
            }

            uc.println(firstEnemyBedMessage);

            /*Move in a random direction*/
            int randomDir = (int)(uc.getRandomDouble()*8);
            Direction dir = Direction.values()[randomDir];
            if (uc.canMove(dir)) uc.move(dir);

            /*Try crafting a random craftable*/
            int randomCraftable = (int)(uc.getRandomDouble()*Craftable.values().length);
            if (uc.canCraft(Craftable.values()[randomCraftable])) uc.craft(Craftable.values()[randomCraftable]);

            /*Try gathering*/
            if (uc.canGather()) uc.gather();


            uc.yield(); // End of turn
        }
    }
}