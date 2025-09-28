package meuli3;

import aic2025.user.*;

public class UnitPlayer {

    // Este array será útil
    Direction[] directions = Direction.values();

    // Helpers
    MovementHelper moveHelper = new MovementHelper();
    BedHandler bedHandler = new BedHandler();
    CraftHelper craftHelper = new CraftHelper();
    ResourceFinder resourceFinder = new ResourceFinder();

    public void run(UnitController uc) {
        // Código que se ejecuta solo al inicio de la vida de la unidad

        while (true) {
            // Código que se ejecuta en cada ronda

            /* Si no está en el mapa, busca una cama vacía para aparecer */
            if (!uc.isOnMap()){
                BedInfo[] beds = uc.getAllBedsInfo();
                for (BedInfo bed : beds){
                    if (uc.canSpawn(bed.getLocation())) uc.spawn(bed.getLocation());
                }
                uc.yield();
                continue;
            }
                
            // PRIORIDAD SIMPLE: si vemos una cama enemiga y tenemos AXE y podemos usarlo ahí,
            // usar el AXE y finalizar el turno; en cualquier otro caso continuar con la lógica normal.
            // Manejo de camas enemigas: intentar romper o acercarse
            try {
                StructureInfo nearestBed = bedHandler.findNearestEnemyBed(uc);
                if (nearestBed != null) {
                    Location bedLoc = nearestBed.getLocation();
                    boolean used = bedHandler.tryUseAxeOnBed(uc, bedLoc);
                    if (used) { uc.yield(); continue; }

                    // no pudimos usar AXE ahora -> mover hacia la cama
                    Direction to = uc.getLocation().directionTo(bedLoc);
                    if (moveHelper.moveTowards(uc, to)) { uc.yield(); continue; }
                    if (moveHelper.randomFallbackMove(uc)) { uc.yield(); continue; }
                }
            } catch (Exception ex) {
                // ignorar errores
            }
            // --- INICIO DE LA LÓGICA MODIFICADA ---

            // Objetivo 1: Intentar crear y colocar un plano de cama (BED_BLUEPRINT)
            // Flujo:
            // 1) Si se puede craftar el blueprint, craftarlo.
            // 2) Si la unidad tiene el craftable BED_BLUEPRINT, buscar una casilla adyacente libre
            //    y usar el craftable allí con canUseCraftable/useCraftable.
            // Usamos las APIs documentadas en UnitController: canCraft, craft, hasCraftable, canUseCraftable, useCraftable.
            // didAction: indicador general de si ya hemos hecho una acción este turno
            boolean didAction = false;

            if (!uc.isOnMap()) {
                // Si no estamos en el mapa no intentamos construir
            } else {
                // Si estamos en el último frame (round 1800), fabricar todo lo posible
                try {
                    if (uc.getRound() >= 1800) {
                        int swords = craftHelper.craftMaxSwords(uc);
                        int shovels = craftHelper.craftMaxShovels(uc);
                        if (swords > 0 || shovels > 0) didAction = true;
                    }
                } catch (Exception e) {
                    // ignorar
                }

                // Paso 1: crear blueprint si es posible
                craftHelper.tryCraft(uc, Craftable.BED_BLUEPRINT);

                // Intentar fabricar un AXE si tenemos los materiales necesarios (1 WOOD + 2 STONE)
                if (craftHelper.ensureAxe(uc)) {
                    didAction = true;
                }

                // Intentar fabricar un PICKAXE si tenemos 2 WOOD y 1 STONE y no tenemos pico
                if (craftHelper.ensurePickaxe(uc)) {
                    didAction = true;
                }

                // ...existing code...
                

                // Paso 2: si tenemos un BED_BLUEPRINT, intentar colocarlo en una casilla adyacente
                if (craftHelper.placeBedBlueprint(uc, directions)) {
                    didAction = true;
                }
            }

            // Objetivo: Mejor movimiento aleatorio.
            // Priorizar recoger si hay materiales disponibles; si no, elegir al azar
            // entre las direcciones en las que se puede mover (excluyendo ZERO).
            // Intentar gather solo si el material actual es WOOD o STRING.
            if (uc.canGather()) {
                aic2025.user.Material mat = uc.senseMaterialAtLocation(uc.getLocation());
                // Recoger madera, piedra o limpiar césped. Recoger cobre sólo si tenemos pico.
                if (mat == aic2025.user.Material.WOOD || mat == aic2025.user.Material.STONE || mat == aic2025.user.Material.GRASS
                        || (mat == aic2025.user.Material.COPPER && uc.hasCraftable(Craftable.PICKAXE))) {
                    uc.gather();
                    didAction = true;
                }
            }

                // Si no hemos recogido nada, primero buscar materiales visibles (WOOD, STONE o COPPER)
            if (!didAction) {
                int vision = uc.getVisionRange();
                float radiusSq = (float)vision * (float)vision;

                Location myLoc = uc.getLocation();
                Location best = null;
                int bestDist = Integer.MAX_VALUE;

                // Si tenemos AXE, priorizamos buscar STRING y usar el AXE
                if (uc.hasCraftable(Craftable.AXE)) {
                    Location bestString = resourceFinder.findNearestMaterial(uc, aic2025.user.Material.STRING, radiusSq);
                    if (bestString != null) {
                        try {
                            if (craftHelper.tryUseCraftableOn(uc, Craftable.AXE, bestString)) {
                                didAction = true;
                            } else {
                                // mover hacia el objetivo o fallback
                                if (!moveHelper.moveToLocation(uc, bestString)) {
                                    moveHelper.randomMove(uc);
                                }
                            }
                        } catch (Exception e) {
                            // ignorar fallos y seguir
                        }
                    }
                }

                // Si no hicimos acción con AXE, seguir buscando WOOD/STONE como antes
                if (!didAction) {
                    Location woodLoc = resourceFinder.findNearestMaterial(uc, aic2025.user.Material.WOOD, radiusSq);
                    Location stoneLoc = resourceFinder.findNearestMaterial(uc, aic2025.user.Material.STONE, radiusSq);
                    Location copperLoc = null;
                    // Buscar cobre sólo si tenemos pico
                    if (uc.hasCraftable(Craftable.PICKAXE)) {
                        copperLoc = resourceFinder.findNearestMaterial(uc, aic2025.user.Material.COPPER, radiusSq);
                    }

                    // elegir el más cercano entre madera, piedra y (si aplica) cobre
                    best = null;
                    int bestD = Integer.MAX_VALUE;
                    if (woodLoc != null) {
                        int d = myLoc.distanceSquared(woodLoc);
                        if (d < bestD) { bestD = d; best = woodLoc; }
                    }
                    if (stoneLoc != null) {
                        int d = myLoc.distanceSquared(stoneLoc);
                        if (d < bestD) { bestD = d; best = stoneLoc; }
                    }
                    if (copperLoc != null) {
                        int d = myLoc.distanceSquared(copperLoc);
                        if (d < bestD) { bestD = d; best = copperLoc; }
                    }

                    if (best != null) {
                        try {
                            // Si estamos en la casilla objetivo, intentar usar PICKAXE (si aplica) o gather
                            if (myLoc.equals(best)) {
                                // intentar usar el pickaxe si lo tenemos y podemos usarlo ahí
                                if (uc.hasCraftable(Craftable.PICKAXE) && craftHelper.tryUseCraftableOn(uc, Craftable.PICKAXE, best)) {
                                    didAction = true;
                                } else if (uc.canGather()) {
                                    uc.gather();
                                    didAction = true;
                                }
                            } else {
                                if (!moveHelper.moveToLocation(uc, best)) {
                                    moveHelper.randomMove(uc);
                                }
                            }
                        } catch (Exception e) {
                            // ignorar y fallback
                            if (!moveHelper.moveToLocation(uc, best)) moveHelper.randomMove(uc);
                        }
                    } else {
                        // No hay materiales visibles: movimiento aleatorio
                        moveHelper.randomMove(uc);
                    }
                }
            }

            // --- FIN DE LA LÓGICA MODIFICADA ---

            uc.yield(); // Fin del turno
        }
    }
}