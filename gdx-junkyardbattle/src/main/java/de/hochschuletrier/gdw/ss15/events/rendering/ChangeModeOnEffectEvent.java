/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.events.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.game.components.effects.EffectMode;

/**
 *
 * @author Julien Saevecke
 */
public class ChangeModeOnEffectEvent {
    
    public static interface Listener {
        void onChangeModeOnEffect(EffectMode mode, Entity entity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(EffectMode mode, Entity entity) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onChangeModeOnEffect(mode, entity);
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
