package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 22.09.15.
 */
public class SendPacketServerEvent{
    public static interface Listener {
        void onSendServerPacket(Packet pack,boolean save,Entity exept);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Packet pack,boolean save,Entity exept) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSendServerPacket(pack,save,exept);
        }
        listeners.end();
    }

    public static void emit(Packet pack,boolean save) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onSendServerPacket(pack,save,null);
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
