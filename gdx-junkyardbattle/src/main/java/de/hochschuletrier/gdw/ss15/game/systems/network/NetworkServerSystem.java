package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.*;

import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class NetworkServerSystem extends EntitySystem implements SendPacketServerEvent.Listener{

    private Serversocket serverSocket = null;
    private ServerGame game = null;
    private ImmutableArray<Entity> clients;

    /**
     * Konstruktoren
     */
    public NetworkServerSystem(ServerGame game) {
        this(game, 0);
    }

    public NetworkServerSystem(ServerGame game, int priority) {
        super(priority);
        this.game = game;
        Family moveFamily = Family.all(ClientComponent.class).get();
        clients = game.get_Engine().getEntitiesFor(moveFamily);
    }

    /**
     * Init funktion
     */
    public void init(Serversocket ssocket) {
        serverSocket = ssocket;
    }

    /**
     * Anzahl der Clients zurueck geben
     */
    public int getClientNumbers() {
        return clients.size();
    }

    @Override
    public void update(float deltaTime) {
        //System.out.println("jfsdklfjsdaöklfjsdöklf rennt");
        while (serverSocket.isNewClientAvaliable()) {
            game.createEntity("player", 0, 0);
        }

        for(Entity client:clients)
        {
            Serverclientsocket sock = ComponentMappers.client.get(client).client;
            if(sock.isPacketAvaliable())
            {
                ReceivedPacket(sock.getReceivedPacket(),client);
                //todo set position in sync component
            }
        }
    }

    private void ReceivedPacket(Packet pack,Entity ent)
    {
        if(pack.getPacketId() == PacketIds.Position.getValue())
        {
            //System.out.println("Received position update");
            EntityPacket ePacket = (EntityPacket)pack;
            PositionComponent posComp = ComponentMappers.position.get(ent);
            //System.out.println("Received position: "+ePacket.xPos+" "+ePacket.yPos);
            posComp.x+=ePacket.xPos;
            posComp.y+=ePacket.yPos;
            //posComp.rotation=ePacket.rotation;
        }
    }


    /**
     * Packet an alle Senden
     */
    public void sendPacketToAllSave(Packet packet) {
        for (Entity entity : clients) {
            ComponentMappers.client.get(entity).client.sendPacketSave(packet);
        }
    }

    public void sendPacketToAllSave(Packet packet, long except){
        int i = 0;
        for(Entity entity : clients){
            if(ComponentMappers.positionSynch.get(entity).networkID != except){
                ComponentMappers.client.get(entity).client.sendPacketSave(packet, (i++ < 1));
            }
        }
    }


    @Override
    public void addedToEngine(Engine engine) {
        SendPacketServerEvent.registerListener(this);
    }

    @Override
    public void removedFromEngine(Engine engine){
        SendPacketServerEvent.unregisterListener(this);
    }


    public void onSendServerPacket(Packet pack,boolean save,Entity exept)
    {
        int i = 0;
        for(Entity entity : clients){
            if(entity != exept){
                if(save) {
                    ComponentMappers.client.get(entity).client.sendPacketSave(pack, (i++ < 1));
                }
                else
                {
                    ComponentMappers.client.get(entity).client.sendPacketUnsave(pack, (i++ < 1));
                }
            }
        }
    }

}
