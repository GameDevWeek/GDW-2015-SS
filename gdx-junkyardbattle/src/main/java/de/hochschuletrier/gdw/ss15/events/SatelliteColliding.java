package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class SatelliteColliding {

    public static interface Listener {
        void onSatelliteColliding();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit() {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onSatelliteColliding();
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
