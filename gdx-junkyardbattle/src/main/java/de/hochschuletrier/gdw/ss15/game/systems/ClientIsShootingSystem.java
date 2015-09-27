package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.ClientIsShootingComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.ReciveShotPacketClient;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 27.09.15.
 */
public class ClientIsShootingSystem extends IteratingSystem implements NetworkReceivedNewPacketClientEvent.Listener {

    private Game game = null;

    public ClientIsShootingSystem(Game gaem) {

        super(Family.all(ClientIsShootingComponent.class).get(), GameConstants.PRIORITY_CLIENT_SHOOTING_SYSTEM);
        game = gaem;
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.ReceiveShootClient,this);
    }
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ClientIsShootingComponent comp = ComponentMappers.ClientIsSchooting.get(entity);
        if(comp.shooted == true)
        {
            comp.shooted = false;
            //System.out.println("shoot");
            //todo wirht codee to shoot
        }
        boolean wasHigth = comp.Gathertime > 0;
        comp.Gathertime -= deltaTime;
        //System.out.println(comp.Gathertime);
        if(comp.Gathertime<=0 && wasHigth)
        {
            //System.out.println("gather stop");
            //todo stop harvesting
        }

        if(comp.onGather)
        {
            if(comp.Gathertime<=0)
            {
                //System.out.println("gather start");
                //Todo start harvesting
            }
            comp.onGather = false;
            comp.Gathertime = 0.300f;

        }

    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        ClientIsShootingComponent comp = ComponentMappers.ClientIsSchooting.get(ent);
        ReciveShotPacketClient rPack = (ReciveShotPacketClient)pack;
        if(rPack.what == 1)
        {
            comp.shooted = true;
        }
        if(rPack.what == 2)
        {
            comp.onGather = true;
        }
    }
}
