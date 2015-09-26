package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class PlayerDiedEvent {
    public static interface Listener {
        //fireChannelAmount between 0 and 1
        void onPlayerDied(Entity player);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Entity player) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onPlayerDied(player);
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
