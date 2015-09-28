package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class ComeToBaseEvent {

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<>();

    public static interface Listener
    {
        void onComeToBase(Entity player, Entity base);
    }


    public static void emit(Entity player, Entity base) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            if (ComponentMappers.player.get(player).teamID == ComponentMappers.basePoint.get(base).teamID) {
                ((Listener) items[i]).onComeToBase(player, base);
            }
        }
        listeners.end();
    }

    public static void register(Listener listener){
        listeners.add(listener);
    }
    public static void unregister(Listener listener) {
        listeners.removeValue(listener, true);
    }

    public static void unregisterAll() {
        listeners.clear();
    }
}
