package de.hochschuletrier.gdw.ss15.events.network.client;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.util.HashMap;

/**
 * Created by lukas on 23.09.15.
 */

public class NetworkReceivedNewPacketClientEvent {
        public static interface Listener {
            void onReceivedNewPacket(Packet pack);
        }

        private static final HashMap<Short,SnapshotArray<Listener>> map = new HashMap<Short,SnapshotArray<Listener>>();


        public static void emit(Packet pack) {
            SnapshotArray<Listener> liste = map.get(pack.getPacketId());
            if(liste != null) {
                Object[] items = liste.begin();
                for (int i = 0, n = liste.size; i < n; i++) {
                    ((Listener) items[i]).onReceivedNewPacket(pack);
                }
                liste.end();
            }
        }

        public static void registerListener(PacketIds id, Listener listener){
            SnapshotArray<Listener> liste = map.get(id.getValue());
            if(liste == null)
            {
                liste = new SnapshotArray<Listener>();
                map.put(id.getValue(),liste);
            }
            liste.add(listener);
        }

        public static void unregisterListener(PacketIds id,Listener listener) {
            SnapshotArray<Listener> liste = map.get(id.getValue());
            if (liste != null) {
                liste.removeValue(listener, true);
                if (liste.size == 0) {
                    map.remove(id.getValue());
                }
            }
        }
    }
