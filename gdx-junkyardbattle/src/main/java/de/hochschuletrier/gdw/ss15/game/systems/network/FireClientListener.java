package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SpawnBulletPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 24.09.15.
 *
 * Server received Fire Package
 */
public class FireClientListener extends EntitySystem implements NetworkReceivedNewPacketClientEvent.Listener{

    private Game game;

    public FireClientListener(Game game){
        super();
        this.game = game;
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.SpawnBullet, this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        NetworkReceivedNewPacketClientEvent.unregisterListener(PacketIds.SpawnBullet, this);
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        try {
            SpawnBulletPacket packet = (SpawnBulletPacket) pack;

            FireServerListener.createProjectile(ent, packet.rotation, packet.power);

        }catch (ClassCastException ex){}
    }
}
