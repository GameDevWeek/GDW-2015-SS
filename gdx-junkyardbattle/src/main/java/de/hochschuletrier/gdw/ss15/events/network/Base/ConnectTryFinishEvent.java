package de.hochschuletrier.gdw.ss15.events.network.Base;

/**
 * Created by lukas on 22.09.15.
 */

import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 22.09.15.
 */
public class ConnectTryFinishEvent {
    public static interface Listener {
        void onConnectFinishPacket(boolean status);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();


    public static void emit(boolean status) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onConnectFinishPacket(status);
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
