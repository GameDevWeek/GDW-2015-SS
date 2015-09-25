package de.hochschuletrier.gdw.ss15.events.network.server;

import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by hherm on 24/09/2015.
 * !!!!!!!DO NOT TOUCH!!!!!!!
 */
public class DoNotTouchServerPacketEvent {

    public static interface Listener{
        void onDoNotTouchServerPacket(Packet pack);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Packet pack) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onDoNotTouchServerPacket(pack);
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
