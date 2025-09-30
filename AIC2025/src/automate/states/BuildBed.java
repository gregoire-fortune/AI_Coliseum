package automate.states;

import aic2025.user.*;
import automate.UnitPlayer;
import automate.tools.results.*;

public class BuildBed extends State {
    UnitPlayer player;
    UnitController uc;

    public BuildBed(UnitPlayer unitPlayer){
        name = "BuildBed";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    Location locationForBed(){
        // Chose a place to build a bed
        for (MaterialInfo material : uc.senseMaterials(2)){
            if(uc.canUseCraftable(Craftable.BED_BLUEPRINT, material.getLocation())){
                return material.getLocation();
            }
        }
        return null;

    }

    @Override
    public Result play(){
        if (uc.canAct()){
            return new Ok("Cooldown to high");
        }
        if (uc.getUnitInfo().getCarriedCraftables()[Craftable.BED_BLUEPRINT.ordinal()][0] == 0){
            return new Ok("Need a bed blueprint, cf. Craft state");
        }

        Location location = locationForBed();
        if(location == null){
            return new Warn("No location to build a bed");
        }

        if(!uc.canUseCraftable(Craftable.BED_BLUEPRINT, location)){
            return new Warn("Can't use bed blueprint on location " + location);
        }

        uc.useCraftable(Craftable.BED_BLUEPRINT, location);

        return new Ok("Build bed on " + location);
    };
}
