package automate.states;

import aic2025.user.Craftable;
import aic2025.user.Material;
import aic2025.user.MaterialInfo;
import aic2025.user.UnitController;
import automate.UnitPlayer;
import automate.tools.results.*;

public class Gather extends State {
    UnitPlayer player;
    UnitController uc;

    public Gather(UnitPlayer unitPlayer){
        name = "Gather";
        player = unitPlayer;
        uc = player.uc;
        init();
    }

    @Override
    public void init(){};

    public float getMaterialScore(Material material){
        // Todos: Update to give more value to rare materials / to materials matching crafts
        int[] scores = {
             0, //  0: Dirt
             0, //  1: Void
             1, //  2: Grass
             0, //  3: Water
             20, //  4: String
             1, //  5: Potato
             12, //  6: Wood
             1, //  7: leather
             2, //  8: Stone
            11, //  9: Copper
            12, // 10: Iron
            13, // 11: Gold
            14  // 12: Diamond
        };
        return scores[material.ordinal()];
    }

    public float getMaterialGatherCooldown(Material material){
        Craftable tool = material.gatheringTool();
        if(tool == null){
            player.err("Can't gather material " + material);
            return 99;
        }

        if(player.haveCraftable(tool)){
            return 1;
        }

        return 10;
    }

    @Override
    public Result play(){
        if (uc.getUnitInfo().getCurrentActionCooldown() > 1){
            return new Warn("Cooldown to high");
        }

        // Get the material with the best score (Gain / time to collect) to gather.
        float bestScore = 0;
        MaterialInfo bestMaterial = null;

        for(MaterialInfo material : uc.senseMaterials(20)){
            if(player.isAbleGather(material.getMaterial())) {
                float score = getMaterialScore(material.getMaterial()) / getMaterialGatherCooldown(material.getMaterial());
                if (score > bestScore) {
                    bestScore = score;
                    bestMaterial = material;
                }
            }
        }

        // Collect item or move to it
        if(bestMaterial == null){
            return new Ok("No materials to gather");
        }

        if(player.tryGather(bestMaterial)){
            return new Ok("Succeed to gather " + bestMaterial.getMaterial().name());
        }

        player.debug("Move result : " + player.pathfinder.move(bestMaterial.getLocation()).info);

        if(player.tryGather(bestMaterial)){
            return new Ok("Succeed to gather after moving " + bestMaterial.getMaterial().name());
        }

        return new Pending("Try to move to " + bestMaterial.getLocation().toString() + " for " + bestMaterial.getMaterial().name());
    };
}
