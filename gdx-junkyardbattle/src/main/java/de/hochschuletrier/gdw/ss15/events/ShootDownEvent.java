package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Martin on 21.09.2015.
 */
public class ShootDownEvent {

    int xPos;
    int yPos;
    int timeStamp;

    public ShootDownEvent(int xPos, int yPos, int timeStamp) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.timeStamp = timeStamp;
        //System.out.println("ShootDownEvent\nxPos: " + this.xPos + " yPos: " + this.yPos + " timeStamp: " + this.timeStamp);
    }

    public static interface Listener{
        void onShootDownEvent(Entity entity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    // name ändern???
    public static void emit(int xPos, int yPos, int timeStamp) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onShootDownEvent(null);
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
