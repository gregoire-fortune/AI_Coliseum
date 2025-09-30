package automate.states;

import aic2025.user.UnitController;
import automate.UnitPlayer;
import automate.tools.Pathfinder;
import automate.tools.results.*;

public class Init extends State {
    UnitPlayer player;
    UnitController uc;

    public Init(UnitPlayer unitPlayer){
        name = "Init";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){
        // This will be called only once at the beginning of the game
        player.pathfinder = new Pathfinder(uc);
    };

    @Override
    public Result play(){
        // This will be called at each new turn
        if(player.lastInit != uc.getRound() - 1){
            player.warn("lastInit wasn't during last turn. (" + player.lastInit + " vs " + uc.getRound() + ")");
        }
        player.lastInit = uc.getRound();

        return new Ok("Turn init done");
    };
}
