package automate.states;

import automate.UnitPlayer;
import automate.tools.results.Result;

public abstract class State extends UnitPlayer {
    public String name = "SkillTemplate";
    public abstract void init();
    public abstract Result play();
}
