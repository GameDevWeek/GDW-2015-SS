package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class WeaponCharging {

    public static interface Listener {
    	//fireChannelAmount between 0 and 1
        void onWeaponCharging(float fireChannelAmount);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(float fireChannelAmount) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onWeaponCharging(fireChannelAmount);
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