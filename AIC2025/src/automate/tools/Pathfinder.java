// Adapted from https://github.com/chenyx512/battlecode24/blob/main/src/bot1/PathFinder.java
package automate.tools;

import aic2025.user.*;
import automate.fast.FastLocSet;
import automate.tools.results.*;
public class Pathfinder {
    int bugPathIndex = 0;


    Boolean rotateRight = null; //if I should rotate right or left
    //Boolean rotateRightAux = null;
    Location lastObstacleFound = null; //latest obstacle I've found in my way

    Location lastCurrent = null;
    int INF = 999;
    int DEBUG_BUGPATH = 0; // Debug level

    int minDistToTarget = INF; //minimum distance I've been to the enemy while going around an obstacle
    Location minLocationToTarget = null;
    Location prevTarget = null; //previous target
    Direction[] dirs = Direction.values();
    //HashSet<Integer> states = new HashSet<>();

    int[][] states;

    Location myLoc;
    //boolean[] canMoveArray;
    int round;

    int turnsMovingToObstacle = 0;
    final int MAX_TURNS_MOVING_TO_OBSTACLE = 2;
    final int MIN_DIST_RESET = 3;

    
    UnitController rc;

    int H,W;

    public Pathfinder(UnitController rc){
        this.rc = rc;
        this.uc = rc;
        H = rc.getMapHeight();
        W = rc.getMapWidth();
        states = new int[W][H];
    }

    UnitController uc;

    Direction[] opposite = {
            Direction.SOUTH,
            Direction.SOUTHEAST,
            Direction.EAST,
            Direction.NORTHEAST,
            Direction.NORTH,
            Direction.NORTHWEST,
            Direction.WEST,
            Direction.SOUTHWEST,
            Direction.ZERO
    };

    public Result moveAwayFrom(Location loc){
        Location opposite = uc.getLocation();
        opposite.x += (uc.getLocation().x -  loc.x);
        opposite.y += (uc.getLocation().y -  loc.y);
        return move(opposite);
    }

    public Result move(Location loc) {
        if (!uc.canMove())
            return new Warn("Can't move");
        if(loc == null)
            return new Err("Asking to move to null location");

        Location before = uc.getLocation();
        moveTo(loc);
        if (uc.getLocation().equals(before)) {
            return new Warn("Move failed (still at same position)");
        }else{
            return new Ok("Now at " + uc.getLocation());
        }
    }

    public boolean onMap(Location loc){
        return loc.x < W && loc.x > 0 && loc.y < H && loc.y > 0;
    }

    // ----------------------------- Bugnav -----------------------------

        void update(){
            if (!rc.canMove()) return;
            myLoc = rc.getLocation();
            round = rc.getRound();
        }


        void moveTo(Location target){

            //No target? ==> bye!
            if (!rc.canMove()) return;
            if (target == null) target = rc.getLocation();
            //if (DEBUG == 1)
            //rc.setIndicatorLine(rc.getLocation(), target, 255, 0, 255);

            update();
            //if (target == null) return;


            //different target? ==> previous data does not help!
            if (prevTarget == null){
                if (DEBUG_BUGPATH == 1) uc.println("Previous target is null! reset!");
                resetPathfinding();
                rotateRight = null;
                //rotateRightAux = null;
            }


            else {
                int distTargets = target.distanceSquared(prevTarget);
                if (distTargets > 0) {
                    if (DEBUG_BUGPATH == 1) uc.println("Different target!! Reset!");
                    if (distTargets >= MIN_DIST_RESET){
                        rotateRight = null;
                        //rotateRightAux = null;
                        resetPathfinding();
                    }
                    else{
                        if (DEBUG_BUGPATH == 1) uc.println("Different target!! Soft Reset!");
                        softReset(target);
                    }
                }
            }


            //Update data
            prevTarget = target;

            checkState();
            myLoc = rc.getLocation();



            int d = myLoc.distanceSquared(target);
            if (d == 0){
                return;
            }

            //If I'm at a minimum distance to the target, I'm free!
            if (d < minDistToTarget){
                if (DEBUG_BUGPATH == 1) uc.println("resetting on d < mindist");
                resetPathfinding();
                minDistToTarget = d;
                minLocationToTarget = myLoc;
            }

            //If there's an obstacle I try to go around it [until I'm free] instead of going to the target directly
            Direction dir = myLoc.directionTo(target);
            if (lastObstacleFound == null){
                if (tryGreedyMove()){
                    if (DEBUG_BUGPATH == 1) uc.println("No obstacle and could move greedily :)");
                    resetPathfinding();
                    return;
                }
            }
            else{
                dir = myLoc.directionTo(lastObstacleFound);
                //rc.setIndicatorDot(lastObstacleFound, 0, 255, 0);
                //if (lastCurrent != null) rc.setIndicatorDot(lastCurrent, 255, 0, 0);
            }



                if (rc.canMove(dir)){
                    rc.move(dir);
                    if (lastObstacleFound != null) {
                        if (DEBUG_BUGPATH == 1) uc.println("Could move to obstacle?!");
                        ++turnsMovingToObstacle;
                        lastObstacleFound = rc.getLocation().add(dir);
                        if (turnsMovingToObstacle >= MAX_TURNS_MOVING_TO_OBSTACLE){
                            if (DEBUG_BUGPATH == 1) uc.println("obstacle reset!!");
                            resetPathfinding();
                        } else if (!onMap(lastObstacleFound)){
                            if (DEBUG_BUGPATH == 1) uc.println("obstacle reset!! - out of the map");
                            resetPathfinding();
                        }
                    }
                    return;
                } else turnsMovingToObstacle = 0;

                checkRotate(dir);

                if (DEBUG_BUGPATH == 1) uc.println(rotateRight + " " + dir.name());


                //I rotate clockwise or counterclockwise (depends on 'rotateRight'). If I try to go out of the map I change the orientation
                //Note that we have to try at most 16 times since we can switch orientation in the middle of the loop. (It can be done more efficiently)
                int i = 16;
                while (i-- > 0) {
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                        return;
                    }
                    Location newLoc = myLoc.add(dir);
                    if (!onMap(newLoc)) rotateRight = !rotateRight;
                        //If I could not go in that direction and it was not outside of the map, then this is the latest obstacle found
                    else lastObstacleFound = newLoc;
                    if (rotateRight) dir = dir.rotateRight();
                    else dir = dir.rotateLeft();
                }


                if  (rc.canMove(dir)){
                    rc.move(dir);
                    return;
                }

        }

        boolean tryGreedyMove(){
                //if (rotateRightAux != null) return false;
                Location myLoc = rc.getLocation();
                Direction dir = myLoc.directionTo(prevTarget);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    return true;
                }
                int dist = myLoc.distanceSquared(prevTarget);
                int dist1 = INF, dist2 = INF;
                Direction dir1 = dir.rotateRight();
                Location newLoc = myLoc.add(dir1);
                if (rc.canMove(dir1)) dist1 = newLoc.distanceSquared(prevTarget);
                Direction dir2 = dir.rotateLeft();
                newLoc = myLoc.add(dir2);
                if (rc.canMove(dir2)) dist2 = newLoc.distanceSquared(prevTarget);
                if (dist1 < dist && dist1 < dist2) {
                    //rotateRightAux = true;
                    rc.move(dir1);
                    return true;
                }
                if (dist2 < dist && dist2 < dist1) {
                    ;//rotateRightAux = false;
                    rc.move(dir2);
                    return true;
                }

            return false;
        }

        //TODO: check remaining cases
        //TODO: move obstacle if can move to obstacle lol
        void checkRotate(Direction dir) {
            if (rotateRight != null) return;
            Direction dirLeft = dir;
            Direction dirRight = dir;
            int i = 8;
            while (--i >= 0) {
                if (!rc.canMove(dirLeft)) dirLeft = dirLeft.rotateLeft();
                else break;
            }
            i = 8;
            while (--i >= 0){
                if (!rc.canMove(dirRight)) dirRight = dirRight.rotateRight();
                else break;
            }
            int distLeft = myLoc.add(dirLeft).distanceSquared(prevTarget), distRight = myLoc.add(dirRight).distanceSquared(prevTarget);
            if (distRight < distLeft) rotateRight = true;
            else rotateRight = false;
        }

        //clear some of the previous data
        void resetPathfinding(){
            if (DEBUG_BUGPATH == 1) uc.println("reset!");
            lastObstacleFound = null;
            minDistToTarget = INF;
            ++bugPathIndex;
            turnsMovingToObstacle = 0;
        }

        void softReset(Location target){
        /*if (rc.getType() == RobotType.AMPLIFIER){
            resetPathfinding();
            return;
        }*/
            if (DEBUG_BUGPATH == 1) uc.println("soft reset!");
            if (minLocationToTarget != null) minDistToTarget = minLocationToTarget.distanceSquared(target);
            else resetPathfinding();
        }

        void checkState(){
            int x,y;
            if (lastObstacleFound == null) {
                x = 61;
                y = 61;
            }
            else{
                x = lastObstacleFound.x;
                y = lastObstacleFound.y;
            }
            int state = (bugPathIndex << 14) | (x << 8) |  (y << 2);
            if (rotateRight != null) {
                if (rotateRight) state |= 1;
                else state |= 2;
            }
            if (states[myLoc.x][myLoc.y] == state){
                resetPathfinding();
            }

            states[myLoc.x][myLoc.y] = state;
        }

    }