package automate.states;

import aic2025.user.*;
import automate.UnitPlayer;
import automate.tools.results.*;

public class Spawn extends State {
    UnitPlayer player;
    UnitController uc;

    public Spawn(UnitPlayer unitPlayer){
        name = "Spawn";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    @Override
    public Result play(){
        if(uc.isOnMap()){
            return new Err("Unit already on map");
        }

        BedInfo[] beds = uc.getAllBedsInfo();
        for (BedInfo bed : beds){
            if (uc.canSpawn(bed.getLocation())) {
                uc.spawn(bed.getLocation());
                player.spawnRound = uc.getRound();
                return new Ok("Unit spawned");
            }
        }

        return new Ok("Waiting for beds");
    };
}
