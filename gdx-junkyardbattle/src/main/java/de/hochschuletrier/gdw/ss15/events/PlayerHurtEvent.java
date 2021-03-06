package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class PlayerHurtEvent {

    private static final SnapshotArray<Listener> listeners = new SnapshotArray<>();

    public static interface Listener
    {
        void onPlayerHurt(Entity projectile, Entity hurtPlayer);
    }

    public static void emit(Entity projectile, Entity hurtPlayer) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onPlayerHurt(projectile, hurtPlayer);
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
