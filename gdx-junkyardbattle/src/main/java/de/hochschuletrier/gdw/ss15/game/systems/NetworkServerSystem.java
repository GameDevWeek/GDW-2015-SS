package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.*;

import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.util.ArrayList;

public class NetworkServerSystem extends EntitySystem implements EntityListener {

    private Serversocket m_Serversocket = null;
    private ServerGame m_game = null;
    private ArrayList<Entity> clients = new ArrayList<>();

    /**
     * Konstruktoren
     */
    public NetworkServerSystem(ServerGame game) {
        this(game, 0);
    }

    public NetworkServerSystem(ServerGame game, int priority) {
        super(priority);
        this.m_game = game;
    }

    /**
     * Init funktion
     */
    public void init(Serversocket ssocket) {
        m_Serversocket = ssocket;
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
        while (m_Serversocket.isNewClientAvaliable()) {
            addClient();
        }
    }

    public void addClient(){
        Entity entity = createClient();

        clients.add(entity);

        InitEntityPacket packet = new InitEntityPacket(ComponentMappers.positionSynch.get(entity).networkID,
                "clientOwnPlayer", 0, 0, 0);
        ComponentMappers.client.get(entity).client.sendPacketSave(packet);
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

    /**
     * Create a new Client
     */
    private Entity createClient() {
        return m_game.createEntity("player", 0, 0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(PositionSynchComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void removedFromEngine(Engine engine){
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        InitEntityPacket packet = new InitEntityPacket(ComponentMappers.positionSynch.get(entity).networkID,
                ComponentMappers.positionSynch.get(entity).clientName, 100, 100, 0);
        sendPacketToAllSave(packet);
    }

    @Override
    public void entityRemoved(Entity entity) {
        clients.remove(entity);
    }

}
