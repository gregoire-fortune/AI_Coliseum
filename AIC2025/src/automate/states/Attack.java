package automate.states;

import aic2025.user.*;
import automate.UnitPlayer;
import automate.tools.results.*;

public class Attack extends State {
    UnitPlayer player;
    UnitController uc;

    public Attack(UnitPlayer unitPlayer){
        name = "Attack";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    public float playerDPS(UnitInfo unit){
        // TODOS: Take tools into account
        return 100 / (float)unit.getWeight();
    }

    @Override
    public Result play(){
        // Target player with the highest DPS
        float bestDPS = 0;
        Location target = null;

        for(UnitInfo enemies: uc.senseUnits(20, uc.getOpponent())){
            float dps = playerDPS(enemies);
            if (dps > bestDPS){
                target = enemies.getLocation();
                bestDPS = dps;
            }
        }

        for(StructureInfo struct: uc.senseStructures(20, uc.getOpponent(), StructureType.BED)){
            float dps = 1;
            if (dps > bestDPS){
                target = struct.getLocation();
                bestDPS = dps;
            }
        }

        if(target == null){
            return new Ok("No enemies");
        }

        // If in reach, attack and get out of reach
        // Or if we have more DPS, engage him
        if(player.possibleDamageTo(target) > 0){
            player.debug(player.attackTarget(target).info);
        }

        if (playerDPS(uc.getUnitInfo()) + 2 > bestDPS) {
            player.debug("Attack move: " + player.pathfinder.move(target).info);
        }

        if(player.possibleDamageTo(target) > 0){
            player.debug(player.attackTarget(target).info);
        }

        player.debug("Move away : " + player.pathfinder.moveAwayFrom(target));

        return new Ok("Enemies detected !");
    };
}
