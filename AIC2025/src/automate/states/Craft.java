package automate.states;

import aic2025.user.*;
import automate.UnitPlayer;
import automate.tools.results.*;

public class Craft extends State {
    UnitPlayer player;
    UnitController uc;

    public Craft(UnitPlayer unitPlayer){
        name = "Craft";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    @Override
    public Result play(){
        // ------------------- Add items to build order -------------------
        if(player.needQueueCraft(Craftable.BED_BLUEPRINT) && uc.getAllBedsInfo().length < 30){
            player.buildOrder.add(Craftable.BED_BLUEPRINT);
            player.debug("Adding bed to buildOrder");
        }

        if(player.needQueueCraft(Craftable.BAKED_POTATO)){
            player.buildOrder.add(Craftable.BAKED_POTATO);
            player.debug("Adding potato to buildOrder");
        }

        if(player.needQueueCraft(Craftable.BOW) && uc.getRound() - player.spawnRound > 60){
            player.buildOrder.add(Craftable.BOW);
            player.debug("Adding bow to buildOrder");
        }


        // ------------------- Build items -------------------
        if(player.buildOrder.size == 0){
            return new Ok("Nothing to craft");
        }

        if(!uc.canAct()){
            return new Ok("Cooldown : " + uc.getUnitInfo().getCurrentActionCooldown());
        }

        for (int i = 0; i < player.buildOrder.size; i++) {
            Craftable craft = player.buildOrder.data[i];

            int count = 0;
            for (Material material : Material.values()) {
                count = uc.getNumberOfMaterials(material) - GameConstants.craftingRecipe[craft.ordinal()][material.ordinal()];
                if (count < 0) {
                    player.debug("\tCrafting " + craft.name() + " missing \t" + count + "\t" + material.name() + " out of " + GameConstants.craftingRecipe[craft.ordinal()][material.ordinal()]);
                    break;
                }
            }

            if (count < 0){
                continue;
            }

            if (!uc.canCraft(craft)) {
                return new Warn("Can't craft. Unknown reason");
            }

            uc.craft(craft);
            player.buildOrder.popAt(i);
            return new Ok(craft.name() + " crafted !");
        }

        return new Ok("Nothing crafted from " + player.buildOrder.size + " items in queue");
    };
}
