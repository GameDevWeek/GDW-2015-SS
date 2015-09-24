package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class WeaponUncharged {

    public static interface Listener {
        void onWeaponUncharged();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit() {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onWeaponUncharged();
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