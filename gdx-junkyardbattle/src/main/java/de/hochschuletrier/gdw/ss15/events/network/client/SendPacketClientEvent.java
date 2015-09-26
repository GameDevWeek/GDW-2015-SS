package de.hochschuletrier.gdw.ss15.events.network.client;

/**
 * Created by lukas on 22.09.15.
 */

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 22.09.15.
 */
public class SendPacketClientEvent{
    public static interface Listener {
        void onSendSClientPacket(Packet pack,boolean save);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();


    public static void emit(Packet pack,boolean save) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSendSClientPacket(pack,save);
        }
        listeners.end();
    }

    public static void registerListener(Listener listener){
        listeners.add(listener);
    }

    public static void unregisterListener(Listener listener){
        listeners.removeValue(listener, true);
    }
    
    public static void unregisterAll() {
        listeners.clear();
    }

}
