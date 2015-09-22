package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by lukas on 22.09.15.
 */
public class NetworkReceivedDeleteEntity{
    public static interface Listener {
        void onNetworkReceivedDeleteEntity(Entity entity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onNetworkReceivedDeleteEntity(entity);
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