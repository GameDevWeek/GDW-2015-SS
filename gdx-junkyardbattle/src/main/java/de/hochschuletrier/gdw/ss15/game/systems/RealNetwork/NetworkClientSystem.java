package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.network.client.NetworkIDComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by lukas on 21.09.15.
 */

public class NetworkClientSystem extends EntitySystem implements EntityListener, DoNotTouchPacketEvent.Listener {


    private static final TestListenerClient testlistener = new TestListenerClient();
    private long lastAddedEntityID = 0;
    private HashMap<Long, Entity> hashMap = new HashMap();
    private ImmutableArray<Entity> entities;
    private Family family;

    private LinkedList<Packet> packetBuffer = new LinkedList<>();

    boolean fistUpdate = true;

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
        if(fistUpdate)
        {
            fistUpdate=false;
            SendPacketClientEvent.emit(new SimplePacket(SimplePacket.SimplePacketId.StartGame.getValue(),0),true);
        }


        //TODO check all input components

        while(!packetBuffer.isEmpty()){
            ReceivedPacket(packetBuffer.removeFirst());
        }
    }

    private void ReceivedPacket(Packet pack)
    {
        if(pack.getPacketId()== PacketIds.InitEntity.getValue())
        {
            InitEntityPacket iPacket = (InitEntityPacket) pack;
//            logger.info("Spawned entitiy with name: "+iPacket.name);

            lastAddedEntityID = iPacket.entityID;

            boolean WasLowId = lastAddedEntityID<0;
            lastAddedEntityID = Math.abs(lastAddedEntityID);

            //System.out.println(iPacket.xPos + " "+ iPacket.yPos);

            Entity ent = game.createEntity(iPacket.name,iPacket.xPos,iPacket.yPos);

            if(WasLowId) {
                ComponentMappers.player.get(ent).teamID = 0;
            }

            //if(iPacket.name.equals())

            ComponentMappers.position.get(ent).rotation = iPacket.rotation;
            //TODO find for rotatoin

            NetworkReceivedNewPacketClientEvent.emit(pack,ent);
        }
        else if(pack.getPacketId() == PacketIds.EntityUpdate.getValue())
        {//positino update packet
            EntityUpdatePacket euPacket = (EntityUpdatePacket) pack;
            Entity ent = hashMap.get(euPacket.entityID);
            if(ent!=null)
            {

                NetworkReceivedNewPacketClientEvent.emit(pack, ent);

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
                    hashMap.remove(sPacket.m_Moredata);
                    game.getEngine().removeEntity(ent);
                }
            }
            NetworkReceivedNewPacketClientEvent.emit(pack,null);
        }
        else if(pack.getPacketId() == PacketIds.SpawnBullet.getValue()){
            SpawnBulletPacket packet = (SpawnBulletPacket) pack;
            Entity e = hashMap.get(packet.bulletID);
            if(e != null)
                NetworkReceivedNewPacketClientEvent.emit(pack, e);
//            else
        }
        else if(pack.getPacketId() == PacketIds.Health.getValue())
        {
            HealthPacket packet = (HealthPacket)pack;
            
            Entity ent = hashMap.get(packet.id);
            if(ent!=null) {
                NetworkReceivedNewPacketClientEvent.emit(pack,ent);
            }
        }
        else if(pack.getPacketId() == PacketIds.ReceiveShootClient.getValue())
        {
            //System.out.println("Received packet");
            ReciveShotPacketClient rPack = (ReciveShotPacketClient) pack;
            Entity ent = hashMap.get(rPack.networkId);
            if(ent!=null) {
                NetworkReceivedNewPacketClientEvent.emit(pack, ent);
            }
        }
        else 
        {
            NetworkReceivedNewPacketClientEvent.emit(pack, null);
        }
    }


    public void dispose(){
    }


    @Override
    public void addedToEngine(Engine engine){
        engine.addEntityListener(family, this);
        DoNotTouchPacketEvent.registerListener(this);
    }

    @Override
    public void removedFromEngine(Engine engine){
        engine.removeEntityListener(this);
        DoNotTouchPacketEvent.unregisterListener(this);
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

    @Override
    public void onDoNotTouchPacket(Packet pack) {
        packetBuffer.addLast(pack);
    }
}

