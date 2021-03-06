package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.*;

import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.util.LinkedList;

public class NetworkServerSystem extends EntitySystem implements SendPacketServerEvent.Listener{


    private static final TestListenerServer testlistener = new TestListenerServer();
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
    public void init() {
    }

    /**
     * Anzahl der Clients zurueck geben
     */
    public int getClientNumbers() {
        return clients.size();
    }

    @Override
    public void update(float deltaTime) {

        LinkedList<Entity> toDelete = new LinkedList<>();
        for(int i=0; i < clients.size(); ++i)
        {
            Entity client = clients.get(i);
            Serverclientsocket sock = ComponentMappers.client.get(client).client;
            if(!sock.isConnected())
            {//client lost connection
                game.get_Engine().removeEntity(client);
                toDelete.addLast(client);
                continue;
            }

            if(sock.isPacketAvaliable())
            {
                ReceivedPacket(sock.getReceivedPacket(),client);
            }
        }

        while (!toDelete.isEmpty())
        {
            game.get_Engine().removeEntity(toDelete.poll());
        }
    }

    private void ReceivedPacket(Packet pack,Entity ent)
    {
        NetworkReceivedNewPacketServerEvent.emit(pack,ent);
        

        
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
        for(int j=0;j<clients.size();j++)
        {
            Entity entity = clients.get(j);
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
