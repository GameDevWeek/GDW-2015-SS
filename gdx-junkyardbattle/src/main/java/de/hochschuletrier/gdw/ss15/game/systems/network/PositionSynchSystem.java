package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hherm on 22/09/2015.
 */
public class PositionSynchSystem extends EntitySystem implements EntityListener {


    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

    private ImmutableArray<Entity> entities;
    ServerGame game;
    private Family family;

    public PositionSynchSystem(ServerGame game,int priotity){
        super(priotity);
        this.game = game;
        family = Family.all(PositionSynchComponent.class).get();
        entities = game.get_Engine().getEntitiesFor(family);
    }

    @Override
    public void update(float deltaTime){
        for(int i = 0; i < entities.size(); ++i)
        {
            Entity ent = entities.get(i);
            //PhysixBodyComponent physComp = ComponentMappers.physixBody.get(ent);
            //physComp.setGravityScale(0);
            //System.out.println(physComp.getPosition());

            PositionSynchComponent comp = ComponentMappers.positionSynch.get(ent);
            if(ComponentMappers.position.has(ent))
            {
                PositionComponent pos = ComponentMappers.position.get(ent);
                //System.out.println("New movment gecoginced: x"+pos.x+ " y"+pos.y);
                if(pos.x != comp.lastX || pos.y != comp.lastY || pos.rotation != comp.lastRot)
                {
                    comp.lastSendTimer.Update();
                    if(comp.lastSendTimer.get_CounterMilliseconds()>42)
                    {
                        comp.lastSendTimer.StartCounter();
                        comp.lastX=pos.x;
                        comp.lastY=pos.y;
                        comp.lastRot=pos.rotation;

                        //System.out.println("befor send");
                        EntityUpdatePacket pack = new EntityUpdatePacket(comp.networkID,comp.lastX,comp.lastY,comp.lastRot);
                        SendPacketServerEvent.emit(pack,false);
                    }
                }
            }
        }
    }

    @Override
    public void addedToEngine(Engine engine){
        engine.addEntityListener(family, this);
    }

    @Override
    public void removedFromEngine(Engine engine){

    }

    @Override
    public void entityAdded(Entity entity) {

        Entity exept = null;
        if(ComponentMappers.client.has(entity))
        {//es ist ein neuer client -> diese alle bestehenden sync objects senden
            //System.out.println("Send all enteties to new Player");
            Serverclientsocket client = ComponentMappers.client.get(entity).client;
            InitEntityPacket initPacket = new InitEntityPacket(0,"",0,0,0);
            for(int i=0;i<entities.size();i++)
            {
                Entity sendEnd = entities.get(i);
                PositionSynchComponent sendComp = ComponentMappers.positionSynch.get(sendEnd);
                initPacket.entityID = sendComp.networkID;
                initPacket.xPos = sendComp.lastX;
                initPacket.yPos = sendComp.lastY;
                initPacket.rotation = sendComp.lastRot;
                if(sendEnd == entity) {//eigener spieler
                    //System.out.println("Send own player to client");
                    initPacket.name = "clientOwnPlayer";
                    exept=entity;
                }
                else
                {
                    //System.out.println("Send other player to client");
                    initPacket.name = sendComp.clientName;
                }
                //System.out.println();
                client.sendPacketSave(initPacket,true);
            }


        }

        PositionComponent comp = ComponentMappers.position.get(entity);
        if(comp == null)
        {
            logger.error("Sync component zu entity one position hinzugefuegt");
            return;
        }
        //allen speielern neue entit mitteilen
        InitEntityPacket packet = new InitEntityPacket(ComponentMappers.positionSynch.get(entity).networkID,
                ComponentMappers.positionSynch.get(entity).clientName, comp.x, comp.y, comp.rotation);
        SendPacketServerEvent.emit(packet, true, exept);
    }

    @Override
    public void entityRemoved(Entity entity) {
        //System.out.println("Position sync component removed");
        SimplePacket spacket = new SimplePacket(SimplePacket.SimplePacketId.RemoveEntity.getValue(),ComponentMappers.positionSynch.get(entity).networkID);
        SendPacketServerEvent.emit(spacket, true,entity);

    }
}
