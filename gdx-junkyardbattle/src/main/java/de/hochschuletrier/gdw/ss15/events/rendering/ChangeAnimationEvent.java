package de.hochschuletrier.gdw.ss15.events.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;

public class ChangeAnimationEvent {

    public static interface Listener {
        void onChangeAnimation(AnimationState state, Entity entity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(AnimationState state, Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onChangeAnimation(state, entity);
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