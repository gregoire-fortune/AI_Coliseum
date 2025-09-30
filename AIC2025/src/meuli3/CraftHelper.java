package meuli3;

import aic2025.user.*;

public class CraftHelper {

    // Intenta craftear un craftable si es posible
    public boolean tryCraft(UnitController uc, Craftable craftable) {
        try {
            if (uc.canCraft(craftable)) {
                uc.craft(craftable);
                return true;
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }

    // Intenta crear un AXE sólo si no tenemos ya uno
    public boolean ensureAxe(UnitController uc) {
        try {
            if (!uc.hasCraftable(Craftable.AXE) && uc.canCraft(Craftable.AXE)) {
                uc.craft(Craftable.AXE);
                return true;
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }
    
    // Intenta fabricar un PICKAXE sólo si no tenemos ya uno y tenemos 2 WOOD y 1 STONE
    public boolean ensurePickaxe(UnitController uc) {
        try {
            if (uc.hasCraftable(Craftable.PICKAXE)) return false;
            // comprobar si podemos craftar directamente
            if (uc.canCraft(Craftable.PICKAXE)) {
                uc.craft(Craftable.PICKAXE);
                return true;
            }
            // no podemos craftar ahora - comprobar si tenemos los materiales en inventario
            int woodCount = uc.getNumberOfMaterials(aic2025.user.Material.WOOD);
            int stoneCount = uc.getNumberOfMaterials(aic2025.user.Material.STONE);
            if (woodCount >= 2 && stoneCount >= 1 && uc.canCraft(Craftable.PICKAXE)) {
                uc.craft(Craftable.PICKAXE);
                return true;
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }

    // ...existing code...

    // Intenta usar un craftable en una localización objetivo
    public boolean tryUseCraftableOn(UnitController uc, Craftable craftable, Location loc) {
        try {
            if (loc == null) return false;
            if (uc.canUseCraftable(craftable, loc)) {
                uc.useCraftable(craftable, loc);
                return true;
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }

    // Intenta colocar un BED_BLUEPRINT en una casilla adyacente
    public boolean placeBedBlueprint(UnitController uc, Direction[] directions) {
        try {
            if (!uc.hasCraftable(Craftable.BED_BLUEPRINT)) return false;
            for (Direction dir : directions) {
                if (dir == Direction.ZERO) continue;
                Location target = uc.getLocation().add(dir);
                if (!uc.canSenseLocation(target)) continue;
                if (uc.senseStructureAtLocation(target) != null) continue;
                if (uc.canUseCraftable(Craftable.BED_BLUEPRINT, target)) {
                    try {
                        uc.useCraftable(Craftable.BED_BLUEPRINT, target);
                        return true;
                    } catch (Exception e) {
                        // intentar otra dirección
                    }
                }
            }
        } catch (Exception e) {
            // ignorar
        }
        return false;
    }

    // Fabricar tantas SWORD como permitan los materiales (3 COPPER por SWORD)
    public int craftMaxSwords(UnitController uc) {
        int crafted = 0;
        try {
            // Seguir intentando mientras tengamos recursos y la API lo permita
            while (true) {
                // Si la API permite craftar directamente, usarla
                if (uc.canCraft(Craftable.SWORD)) {
                    uc.craft(Craftable.SWORD);
                    crafted++;
                    continue;
                }

                // comprobar materiales: 3 COPPER
                int copper = uc.getNumberOfMaterials(aic2025.user.Material.COPPER);
                if (copper >= 3 && uc.canCraft(Craftable.SWORD)) {
                    uc.craft(Craftable.SWORD);
                    crafted++;
                    continue;
                }

                break;
            }
        } catch (Exception e) {
            // ignorar y devolver lo que se haya fabricado
        }
        return crafted;
    }

    // Fabricar tantas SHOVEL como permitan los materiales (1 WOOD + 1 STONE por SHOVEL)
    public int craftMaxShovels(UnitController uc) {
        int crafted = 0;
        try {
            while (true) {
                if (uc.canCraft(Craftable.SHOVEL)) {
                    uc.craft(Craftable.SHOVEL);
                    crafted++;
                    continue;
                }

                int wood = uc.getNumberOfMaterials(aic2025.user.Material.WOOD);
                int stone = uc.getNumberOfMaterials(aic2025.user.Material.STONE);
                if (wood >= 1 && stone >= 1 && uc.canCraft(Craftable.SHOVEL)) {
                    uc.craft(Craftable.SHOVEL);
                    crafted++;
                    continue;
                }

                break;
            }
        } catch (Exception e) {
            // ignorar
        }
        return crafted;
    }


}
