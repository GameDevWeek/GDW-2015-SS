package de.hochschuletrier.gdw.ss15.game.utils;

import java.util.ArrayList;

/**
 * Created by oliver on 23.09.15.
 */
public class TimerSystem {

    private static ArrayList<Timer> timers;

    public void Update(float deltaTime){

        timers.forEach((t)->t.update(deltaTime));

    }


}
