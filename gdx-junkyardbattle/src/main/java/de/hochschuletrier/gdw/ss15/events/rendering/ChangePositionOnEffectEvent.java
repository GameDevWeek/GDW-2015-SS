/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.events.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 *
 * @author Julien Saevecke
 */
public class ChangePositionOnEffectEvent {
      
    public static interface Listener {
        void onChangePositionOnEffect(Vector2 position, Entity entity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Vector2 position, Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onChangePositionOnEffect(position, entity);
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
