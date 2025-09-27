package alexandre;

import aic2025.user.*;

public class UnitPlayer {

    //This array will be useful
    Direction[] directions = Direction.values();

    public void run(UnitController uc) {
        // Code to be executed only at the beginning of the unit's lifespan
        int wood = 0;
        int wool = 0;
        boolean matFind = false;
        Location matLoc = null;
        Location botLoc = null;
        //while(uc.getRound() < 100) {
        while(true) {
            if (!uc.isOnMap()) {
                BedInfo[] beds = uc.getAllBedsInfo();
                for (BedInfo bed : beds) {
                    if (uc.canSpawn(bed.getLocation())) uc.spawn(bed.getLocation());
                }
            }
            if (!matFind && wood < 2) {
                MaterialInfo[] material = uc.senseMaterials(uc.getVisionRange());
                for (MaterialInfo m : material) {
                    if (m.getMaterial() == Material.WOOD) {
                        matLoc = m.getLocation();
                        matFind = true;
                        break;
                    }
                }
            }
            else if(!matFind){
                MaterialInfo[] material = uc.senseMaterials(uc.getVisionRange());
                for (MaterialInfo m : material) {
                    if (m.getMaterial() == Material.STRING) {
                        matLoc = m.getLocation();
                        matFind = true;
                        break;
                    }
                }
                if(!matFind) {
                    for (MaterialInfo m : material) {
                        if (m.getMaterial() == Material.WOOD) {
                            matLoc = m.getLocation();
                            matFind = true;
                            break;
                        }
                    }
                }
            }
            boolean move = false;
            botLoc = uc.getLocation();
            if(matFind) {
                if (matLoc.equals(botLoc)) {
                    if (uc.canGather()) {
                        uc.gather();
                        wood += 1;
                        matFind = false;
                    }
                    move = true;
                } else if (uc.canMove(uc.getLocation().directionTo(matLoc))) {
                    uc.move(uc.getLocation().directionTo(matLoc));
                }
            }
            else{
                if(uc.canMove(uc.getLocation().directionTo(new Location(uc.getMapWidth()/2,uc.getMapHeight()/2)))) {
                    uc.move(uc.getLocation().directionTo(new Location(uc.getMapWidth()/2,uc.getMapHeight()/2)));
                }
                else if(uc.canMove(uc.getLocation().directionTo(uc.getBed()))){
                    uc.move(uc.getLocation().directionTo(uc.getBed()));
                }
            }
            uc.yield();
        }
//        while (true) {
//            // Code to be executed every round
//            if(uc.canBroadcastMessage(wood)){
//                uc.broadcastMessage(wood);
//            }
//            /*If not on the map, find an empty bed and spawn there*/
//            if (!uc.isOnMap()){
//                BedInfo[] beds = uc.getAllBedsInfo();
//                for (BedInfo bed : beds){
//                    if (uc.canSpawn(bed.getLocation())) uc.spawn(bed.getLocation());
//                }
//            }
//
//            /*Move in a random direction*/
//            int randomDir = (int)(uc.getRandomDouble()*8);
//            Direction dir = Direction.values()[randomDir];
//            if (uc.canMove(dir)) uc.move(dir);
//
//            /*Try crafting a random craftable*/
//            int randomCraftable = (int)(uc.getRandomDouble()*Craftable.values().length);
//            if (uc.canCraft(Craftable.values()[randomCraftable])) uc.craft(Craftable.values()[randomCraftable]);
//
//            /*Try gathering*/
//            if (uc.canGather()) uc.gather();
//
//
//            uc.yield(); // End of turn
//        }
    }
}