package automate.states;

import aic2025.user.*;
import automate.UnitPlayer;
import automate.tools.results.*;

public class EmergencyHeal extends State {
    UnitPlayer player;
    UnitController uc;

    public EmergencyHeal(UnitPlayer unitPlayer){
        name = "EmergencyHeal";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    @Override
    public Result play(){
        if(!player.haveCraftable(Craftable.BAKED_POTATO)){
            return new Ok("Dont have potato.");
        }

        if(uc.getUnitInfo().getHealth() <= 3){
            uc.useCraftable(Craftable.BAKED_POTATO, uc.getLocation());
            return new Ok("use potato on me");
        }

        for(UnitInfo allies: uc.senseUnits(5, uc.getTeam())){
            if(allies.getHealth() <= 3){
                uc.useCraftable(Craftable.BAKED_POTATO, allies.getLocation());
                return new Ok("use potato on allies at " + allies.getLocation());
            }
        }
        return new Ok("No need for potato");
    };
}
