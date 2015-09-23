package de.hochschuletrier.gdw.ss15.game.utils;


import java.util.ArrayList;

/**
 * Created by oliver on 23.09.15.
 */
public class Timer {

    public interface Listener{
        public void onTimerExpired();
    }

    final float startTime;
    float timeLeft = 0f;
    boolean paused = true;
    boolean expired = false;
    ArrayList<Listener> listeners = new ArrayList<>();

    /**
     * @param time time in miliseconds
     */
    public Timer(float time){
        startTime = time;
    }

    public void addListener(Listener l){ listeners.add(l); }
    public void removeListener(Listener l){ listeners.add(l); }

    public void update(float deltaTime){
        if(paused) return;
        timeLeft -= deltaTime * 0.001f ;
        expired = timeLeft < 0;
        if(expired) paused = true;
    }

    public boolean isExpired(){ return expired; }
    public boolean isPaused(){ return paused; }

    public void start(){
        paused = false;
    }

    public void reset(){
        timeLeft = startTime;
        paused = true;
    }

    public void restart(){
        timeLeft = startTime;
        paused = false;
    }

}
