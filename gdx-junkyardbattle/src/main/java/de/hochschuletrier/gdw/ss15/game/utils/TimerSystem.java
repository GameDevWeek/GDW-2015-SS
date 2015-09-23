package de.hochschuletrier.gdw.ss15.game.utils;

import java.util.ArrayList;

/**
 * Created by oliver on 23.09.15.
 */
public class TimerSystem {

    private static ArrayList<Timer> timers = new ArrayList<>();

    public void update(float deltaTime){
        timers.forEach((t)->t.update(deltaTime));
    }


    public void addTimer(Timer t){
        timers.add(t);
    }

}
