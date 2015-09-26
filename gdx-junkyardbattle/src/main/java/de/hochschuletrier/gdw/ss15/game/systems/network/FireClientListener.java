package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
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
        System.out.println("registered spawnbullet listener");
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        NetworkReceivedNewPacketClientEvent.unregisterListener(PacketIds.SpawnBullet, this);
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        System.out.println("received packet");
        try {
            SpawnBulletPacket packet = (SpawnBulletPacket) pack;

            FireServerListener.createProjectile(ent, packet.rotation);
            //Entity et = game.createEntity("projectile", packet.position.x, packet.position.y);
            //FireServerListener.createProjectile(et, packet.rotation);

        }catch (ClassCastException ex){}

    }

}
