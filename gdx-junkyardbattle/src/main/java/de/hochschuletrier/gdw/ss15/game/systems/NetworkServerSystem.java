package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.*;

import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;

import java.util.ArrayList;

public class NetworkServerSystem extends EntitySystem implements EntityListener {

    private Serversocket m_Serversocket = null;
    private ServerGame m_game = null;
    private ArrayList<Entity> clients = new ArrayList<>();

    /**
     * Konstruktoren
     */
    public NetworkServerSystem(ServerGame game){
        this(game, 0);
    }

    public NetworkServerSystem(ServerGame game, int priority){
        super(priority);
        this.m_game = game;
    }

    /**
     * Init funktion
     */
    public void init(Serversocket ssocket){
        m_Serversocket=ssocket;
    }

    /**
     * Anzahl der Clients zurueck geben
     */
    public int getClientNumbers(){
        return clients.size();
    }

    @Override
    public void update(float deltaTime){
        //System.out.println("jfsdklfjsdaöklfjsdöklf rennt");
        while(m_Serversocket.isNewClientAvaliable()){
            createClient();
            sendPacketToAll();
        }
    }

    private void sendPacketToAll(){
        for(Entity entity : clients){
            /**
             *
             * Hier muessen noch
             * daten gesendet werden
             * - Paket erstellen
             * - Paket packen (mehrere Komponenten in 1 Paket?
             *                 und welche Komponenten?
             * - Packet senden
             *
             */
        }
    }

    /**
     * Create a new Client
     */
    private void createClient(){
        m_game.createEntity("player", 0, 0);
    }

    @Override
    public void addedToEngine(Engine engine){
        Family family = Family.all(ClientComponent.class,PositionSynchComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        ComponentMappers.client.get(entity).client = m_Serversocket.getNewClient();
        clients.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        clients.remove(entity);
    }

}
