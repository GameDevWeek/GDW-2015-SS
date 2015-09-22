package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Martin on 21.09.2015.
 */
public class ShootDownEvent {

    public static interface Listener{
        void onInputEvent(String action, Entity entity, int xPos, int yPos, int timestamp);
    }

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
