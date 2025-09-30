package automate.states;

import aic2025.user.Direction;
import aic2025.user.Location;
import aic2025.user.UnitController;
import automate.UnitPlayer;
import automate.tools.results.*;

public class Explore extends State {
    UnitPlayer player;
    UnitController uc;
    Location destination;

    public Explore(UnitPlayer unitPlayer){
        name = "Explore";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){
        updateDestination();
    };

    @Override
    public Result play(){
        player.debug("I am playing " + name);

        if (uc.getUnitInfo().getCurrentMovementCooldown() > 0){
            return new Warn("Moving cooldown too high");
        }

        Result result = player.pathfinder.move(destination);
        if(result.code < 0){
            player.print("Exploring return " + result.info);
            updateDestination();
            return result;
        }
        return new Ok("Done, going to " + destination + "(actually at " + uc.getLocation() + ")");
    };

    void updateDestination(){
        destination = new Location(
                (int)(uc.getRandomDouble() * uc.getMapWidth()),
                (int)(uc.getRandomDouble() * uc.getMapHeight())
        );
        player.print("Updating destination : " + destination + " (Actually at " + uc.getLocation() + ")");
    }

}
