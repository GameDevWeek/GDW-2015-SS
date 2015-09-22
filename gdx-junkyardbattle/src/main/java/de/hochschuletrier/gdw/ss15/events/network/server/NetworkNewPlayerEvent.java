package de.hochschuletrier.gdw.ss15.events.network.server;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 22.09.15.
 */
public class NetworkNewPlayerEvent{
    public static interface Listener {
        void onNetworkNewPacket(Entity ent);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Entity ent) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onNetworkNewPacket(ent);
        }
        listeners.end();
    }

    public static void registerListener(Listener listener){
        listeners.add(listener);
    }

    public static void unregisterListener(Listener listener){
        listeners.removeValue(listener, true);
    }

}

