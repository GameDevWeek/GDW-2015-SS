package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputEvent {

    public static interface Listener{
        void onInputEvent(String action, Entity entity, int xPos, int yPos, int timestamp);
    }

    /*

    public String actionName;
    public int timeStamp;
    public int xPos;
    public int yPos;


    public InputEvent(String actionName, int timeStamp, int xPos, int yPos) {
        this.actionName = actionName;
        this.timeStamp = timeStamp;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public InputEvent() {
        this.actionName = null;
        this.timeStamp = 0;
        this.xPos = 0;
        this.yPos = 0;
    }

    public InputEvent(String actionName, int timeStamp) {
        this.actionName = actionName;
        this.timeStamp = timeStamp;
        this.xPos = 0;
        this.yPos = 0;
    }
    */

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    // name ändern???
    public static void emit(String action, Entity entity, int xPos, int yPos, int timeStamp) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onInputEvent(action, entity,xPos,yPos,timeStamp);
        }
        listeners.end();
    }

    public static void register(Listener listener) {
        listeners.add(listener);
    }

    public static void unregister(Listener listener) {
        listeners.removeValue(listener, true);
    }

    public static void unregisterAll() {
        listeners.clear();
    }


}
