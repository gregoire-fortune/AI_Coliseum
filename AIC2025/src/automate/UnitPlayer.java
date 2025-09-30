package automate;

import aic2025.user.*;
import automate.fast.CraftableFifo;
import automate.states.*;
import automate.tools.Pathfinder;
import automate.tools.results.*;

public class UnitPlayer {

    // All variables need to be static to be accessible from different states
    public UnitController uc;
    public Location spawnLoc;
    public int lastInit = 0;
    public int spawnRound = 0;
    public Pathfinder pathfinder;

    // Contains stuff requested to be build
    public CraftableFifo buildOrder = new CraftableFifo();


    public void run(UnitController unitController) {
        // Code to be executed only at the beginning of the unit's lifespan
        uc = unitController;
        buildOrder.add(Craftable.AXE);
        buildOrder.add(Craftable.PICKAXE);

        important("Starting at round " + uc.getRound());

        // Instantiate your different states
        State init = new Init(this);
        State spawn = new Spawn(this);
        State emergencyHeal = new EmergencyHeal(this);
        State craft = new Craft(this);
        State buildBed = new BuildBed(this);
        State attack = new Attack(this);
        State explore = new Explore(this);
        State gather = new Gather(this);
        State endTurn = new EndTurn(this);
        State currentState = init;

        important("Done initializing at " + uc.getEnergyUsed() + " of round " + uc.getRound());

        while (true) {
            header(currentState.name + " [" + uc.getRound() + " - " + (int)(uc.getPercentageOfEnergyUsed() * 10000) + "/10.000]");
            Result result = currentState.play();
            print("<= " + result.info);

            if (result.code == 2) { // Lock
                // We need to stay in this state
                // For example, during multi-turn strategy. We will manually close the turn.
                print("Lock: End turn and resume to state.");
                endTurn.play();
                init.play();

            } else if (result.code == 1) { // Pending
                // We need to go back to init
                print("Skipping to end of turn");
                currentState = endTurn;

            } else { // Ok, Err, Warn
                if(currentState.name.equals("Init")) {
                    if (uc.isOnMap()) {
                        currentState = emergencyHeal;
                    } else {
                        currentState = spawn;
                    }

                } else if (currentState.name.equals("EmergencyHeal")) {
                    currentState = craft;

                } else if (currentState.name.equals("Craft")) {
                    currentState = buildBed;

                } else if (currentState.name.equals("BuildBed")) {
                    currentState = attack;

                } else if (currentState.name.equals("Attack")) {
                    currentState = gather;

                } else if (currentState.name.equals("Gather")) {
                    currentState = explore;

                } else if (currentState.name.equals("Explore")) {
                    currentState = endTurn;

                } else if (currentState.name.equals("Spawn")) {
                    currentState = endTurn;

                } else if (currentState.name.equals("EndTurn")) {
                    currentState = init;

                } else {
                    err("Err: State " + currentState.name + " dont have transition. Ending turn.");
                    currentState = endTurn;
                }
                header("");
            }
        }
    }

    // ------------------ Utils ------------------

    public void _debug(String msg){
        if(uc.getRound() < 500 && uc.isOnMap()) {
            uc.println(uc.getID() + msg);
        }
    }
    public void important(String msg){_debug(msg);}          // New turn, Err
    public void header(String msg)   {_debug("\t" + msg);}      // Changing state, state return
    public void print(String msg)    {_debug("\t\t" + msg);}     // Inside state print
    public void debug(String msg)    {_debug("\t\t\t" + msg);}   // Debug stuff to help

    public void warn(String msg)     {_debug("\tWW: " + msg);}
    public void err(String msg)      {_debug("\tEE: " + msg);}

    // ------------------ Craft ------------------

    public boolean haveCraftable(Craftable tool){
        // We assume that tool is not null
        int[][] tools = uc.getUnitInfo().getCarriedCraftables();
        return tools[tool.ordinal()][0] > 0
                || tools[tool.ordinal()][1] > 0
                || tools[tool.ordinal()][2] > 0
                || tools[tool.ordinal()][3] > 0
                ;
    }

    // Don't have the craft and not already queue
    public boolean needQueueCraft(Craftable craft){
        return buildOrder.count[craft.ordinal()] == 0 && !haveCraftable(craft);
    }


    // ------------------ Materials ------------------

    public boolean isAbleGather(Material material){
        return material.canBeGathered() || (material.gatheringTool() != null && haveCraftable(material.gatheringTool()));
    }

    public boolean tryGather(MaterialInfo material){
        Craftable tool = material.getMaterial().gatheringTool();

        if(haveCraftable(tool)){
            if(uc.canUseCraftable(tool, material.getLocation())){
                debug("Done gathering");
                uc.useCraftable(tool, material.getLocation());
                return true;
            }else{
                debug("Can't use tool (maybe cooldown ?)");
            }
        } else if (material.getMaterial().canBeGathered() && material.getLocation().equals(uc.getLocation())) {
            if(uc.canGather()){
                debug("Done pickup");
                uc.gather();
                return true;
            }else{
                debug("Can't pickup (maybe cooldown ?)");
            }
        }else{
            debug("Don't have tool and position " + uc.getLocation() + " far from " + material.getLocation());
        }

        return false;
    }

    // ------------------ Materials ------------------
    Craftable[] byDamageDesc = {Craftable.SWORD, Craftable.BOW, Craftable.AXE, Craftable.PICKAXE, Craftable.SHOVEL};
    int[] craftableDamage = {
            0,0,0,0,0,
            0,0,1,1,1,
            0,2,0,3,0,
            0,0,0,0,0,
            0,0,0,0,0,
    };

    public int possibleDamageTo(Location target){
        for(Craftable c : byDamageDesc){
            if(uc.hasCraftable(c) && uc.canUseCraftable(c, target)){
                return craftableDamage[c.ordinal()];
            }
        }
        return 0;
    }

    public Result attackTarget(Location target){
        for(Craftable c : byDamageDesc){
            if(uc.hasCraftable(c) && uc.canUseCraftable(c, target)){
                uc.useCraftable(c, target);
                return new Ok("Attack on " + target + " using " + c);
            }
        }
        return new Warn("Can't attack " + target);
    }
}
