package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class MiningEvent {

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<>();

    public static interface Listener

    {
        void onMiningEvent(Entity player, Entity mineable, float channelTime);
    }

    public static void emit(Entity player, Entity mineable, float channelTime) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onMiningEvent(player, mineable, channelTime);
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
