package de.hochschuletrier.gdw.ss14.game.systems;

import com.badlogic.ashley.core.*;
import de.hochschuletrier.gdw.ss14.game.ComponentMappers;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.game.components.ClientComponent;
import de.hochschuletrier.gdw.ss14.game.components.PositionSynchComponent;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.Serversocket;

import java.util.ArrayList;

public class ClientSystem extends EntitySystem implements EntityListener {

    private Serversocket m_Serversocket = null;
    private Game m_game = null;
    private ArrayList<Entity> clients = new ArrayList<>();

    /**
     * Konstruktoren
     */
    public ClientSystem(Game game){
        this(game, 0);
    }

    public ClientSystem(Game game, int priority){
        super(priority);
        this.m_game = game;
        init();
    }

    /**
     * Init funktion
     */
    private void init(){
        if(m_Serversocket.open()){
            System.out.println("Server wurde gestartet.");
        }
    }

    /**
     * Anzahl der Clients zurueck geben
     */
    public int getClientNumbers(){
        return clients.size();
    }

    @Override
    public void update(float deltaTime){
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
        Family family = Family.all(ClientComponent.class, PositionSynchComponent.class).get();
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
