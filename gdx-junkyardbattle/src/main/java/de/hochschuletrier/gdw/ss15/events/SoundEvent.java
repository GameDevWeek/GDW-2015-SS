package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class SoundEvent {

    public static interface Listener {
        void onSoundEvent(String sound, Entity entity, boolean b);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(String sound, Entity entity, boolean b) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onSoundEvent(sound, entity, b);
        }
        listeners.end();
    }

    public static void emit(String sound, Entity entity)
    {
        emit(sound, entity, false);
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