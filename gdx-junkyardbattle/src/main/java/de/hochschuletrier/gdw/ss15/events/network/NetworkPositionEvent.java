package de.hochschuletrier.gdw.ss15.events.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by lukas on 22.09.15.
 */
public class NetworkPositionEvent {
    public static interface Listener {
        void onNetworkPositionEvent(Entity entity,float posX,float posY,float rotation);
    }

    private static final SnapshotArray<Listener> listenersServer = new SnapshotArray();
    private static final SnapshotArray<Listener> listenersClient = new SnapshotArray();

    public static void emit(Entity entity,float posX,float posY,float rotation,boolean server) {
        if(server) {
            Object[] items = listenersServer.begin();
            for (int i = 0, n = listenersServer.size; i < n; i++) {
                ((Listener) items[i]).onNetworkPositionEvent(entity, posX, posY, rotation);
            }
            listenersServer.end();
        }
        else
        {

        }
    }

    public static void registerServerListener(Listener listener) {
        listenersServer.add(listener);
    }

    public static void registerCerverListener(Listener listener) {
        listenersServer.add(listener);
    }

    public static void unregisterServerListener(Listener listener)
    {
        listenersClient.removeValue(listener, true);
    }

    public static void unregisterAll()
    {
        listenersClient.clear();
    }
}
