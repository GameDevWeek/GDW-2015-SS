package de.hochschuletrier.gdw.ss15.events;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class ComeToBaseEvent {

    public static interface Listener
    {
        void onCollisionEvent(PhysixContact physixContact);
    }

    public static void emit(PhysixContact physixContact) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onCollisionEvent(physixContact);
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
