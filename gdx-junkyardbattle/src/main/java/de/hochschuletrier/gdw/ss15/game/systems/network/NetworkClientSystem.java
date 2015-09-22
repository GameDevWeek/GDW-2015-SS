package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.NetworkPositionEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedDeleteEntity;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewEntity;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.network.client.NetworkIDComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by lukas on 21.09.15.
 */

public class NetworkClientSystem extends EntitySystem implements EntityListener {

    private MyTimer timer = new MyTimer(true);
    private long lastAddedEntityID = 0;
    private HashMap<Long, Entity> hashMap = new HashMap();
    private ImmutableArray<Entity> entities;
    private Family family;

    Game game = null;
    ClientConnection connection = Main.getInstance().getClientConnection();
    long lastNetworkTimestamp = 0;

    private static final Logger logger = LoggerFactory.getLogger(NetworkClientSystem.class);

    public void socketDisconnected()
    {

    }

    public NetworkClientSystem(Game game,int priority) {

        super(priority);
        this.game=game;
        family = Family.all(NetworkIDComponent.class).get();
        entities = game.getEngine().getEntitiesFor(family);

    }

    @Override
    public void update(float deltaTime)
    {
        /*
        //TODO check all input components
        timer.Update();
        if(timer.get_CounterMilliseconds() > 200){
            InputMovPaket inputPacket = new InputMovPaket(game.getInputSystem().keyDown(Input.Keys.W),
                    game.getInputSystem().keyDown(Input.Keys.S),
                    game.getInputSystem().keyDown(Input.Keys.A),
                    game.getInputSystem().keyDown(Input.Keys.D));
            connection.getSocket().sendPacketUnsave(inputPacket);
            timer.ResetTimer();
        }
        */

        Clientsocket socket = connection.getSocket();
        if(socket != null)
        {
            while(socket.isPacketAvaliable())
            {
                //System.out.println("Received packet");
                ReceivedPacket(socket.getReceivedPacket());
            }
        }
    }

    private void ReceivedPacket(Packet pack)
    {
        //System.out.println("received packet");
        if(pack.getPacketId()== PacketIds.InitEntity.getValue())
        {
            InitEntityPacket iPacket = (InitEntityPacket) pack;
            //logger.info("Spawned entitiy with name: "+iPacket.name);


            lastAddedEntityID = iPacket.entityID;
            Entity ent = game.createEntity(iPacket.name,0,0);

            ComponentMappers.position.get(ent).x = iPacket.xPos;
            ComponentMappers.position.get(ent).y = iPacket.yPos;
            ComponentMappers.position.get(ent).rotation = iPacket.rotation;

            NetworkReceivedNewEntity.emit(ent);
        }
        else if(pack.getPacketId() == PacketIds.Position.getValue())
        {//positino update packet
            if(pack.getTimestamp()>lastNetworkTimestamp)
            {//synccompoent
                lastNetworkTimestamp = pack.getTimestamp();
                EntityPacket ePacket = (EntityPacket) pack;

                Entity ent = hashMap.get(ePacket.entityID);
                if(ent!=null) {
                    NetworkPositionEvent.emit(ent, ePacket.xPos, ePacket.yPos, ePacket.rotation, false);

                    ComponentMappers.position.get(ent).x = ePacket.xPos;
                    ComponentMappers.position.get(ent).y = ePacket.yPos;
                    ComponentMappers.position.get(ent).rotation = ePacket.rotation;
                }
            }
        }
        else if(pack.getPacketId()==PacketIds.Simple.getValue())
        {
            SimplePacket sPacket = (SimplePacket)pack;
            if(sPacket.m_SimplePacketId == SimplePacket.SimplePacketId.RemoveEntity.getValue())
            {
                Entity ent = hashMap.get(sPacket.m_Moredata);
                if(ent!=null) {
                    //entety deleted
                    NetworkReceivedDeleteEntity.emit(ent);
                    hashMap.remove(sPacket.m_Moredata);
                    game.getEngine().removeEntity(ent);
                }
            }
        }
    }


    public void dispose(){
    }


    @Override
    public void addedToEngine(Engine engine){
        engine.addEntityListener(family, this);
    }

    @Override
    public void removedFromEngine(Engine engine){
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        //System.out.print("Super epic entity id:"+ lastAddedEntityID);
        ComponentMappers.networkID.get(entity).networkID = lastAddedEntityID;
        hashMap.put(lastAddedEntityID, entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        hashMap.remove(ComponentMappers.networkID.get(entity).networkID);
    }
}
