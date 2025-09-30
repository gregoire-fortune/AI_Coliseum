package automate.states;

import aic2025.user.UnitController;
import automate.UnitPlayer;
import automate.tools.results.*;

public class EndTurn extends State {
    UnitPlayer player;
    UnitController uc;

    public EndTurn(UnitPlayer unitPlayer){
        name = "EndTurn";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    @Override
    public Result play(){
        // This will be called at each end of turn

        if(player.lastInit != uc.getRound()){
            player.print("Warning: lastInit wasn't during the same turn. Maybe bytecode overflow ? (" + player.lastInit + " vs " + uc.getRound() + ")");
            return new Warn("Skipping uc.yield");
        }

        player._debug("----------------------------------------------------------------------");
        uc.yield();
        return new Ok("End of turn done.");
    };
}
