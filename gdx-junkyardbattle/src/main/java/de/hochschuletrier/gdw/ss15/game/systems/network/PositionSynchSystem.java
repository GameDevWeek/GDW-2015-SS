package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;

/**
 * Created by hherm on 22/09/2015.
 */
public class PositionSynchSystem extends EntitySystem implements EntityListener {

    private ImmutableArray<Entity> entities;
    ServerGame game;

    public PositionSynchSystem(ServerGame game,int priotity){
        super(priotity);
        this.game = game;
        Family moveFamily = Family.all(ClientComponent.class).get();
        entities = game.get_Engine().getEntitiesFor(moveFamily);
    }

    @Override
    public void update(float deltaTime){
        for(int i = 0; i < entities.size(); ++i)
        {
            Entity ent = entities.get(i);
            PositionSynchComponent comp = ComponentMappers.positionSynch.get(ent);
            if(ComponentMappers.position.has(ent))
            {
                PositionComponent pos = ComponentMappers.position.get(ent);
                if(pos.x != comp.lastX || pos.y != comp.lastY || pos.rotation != comp.lastRot)
                {
                    comp.lastSendTimer.Update();
                    if(comp.lastSendTimer.get_CounterMilliseconds()>200)
                    {
                        //System.out.println("New movment gecoginced: x"+pos.x+ " y"+pos.y);

                        comp.lastSendTimer.StartCounter();
                        comp.lastX=pos.x;
                        comp.lastY=pos.y;
                        comp.lastRot=pos.rotation;

                        EntityPacket pack = new EntityPacket(comp.networkID,comp.lastX,comp.lastY,comp.lastRot);
                        SendPacketServerEvent.emit(pack,false);
                    }
                }
            }
        }
    }

    @Override
    public void addedToEngine(Engine engine){

    }

    @Override
    public void removedFromEngine(Engine engine){

    }

    @Override
    public void entityAdded(Entity entity) {
        //allen speielern neue entit mitteilen
        InitEntityPacket packet = new InitEntityPacket(ComponentMappers.positionSynch.get(entity).networkID,
                ComponentMappers.positionSynch.get(entity).clientName, 100, 100, 0);
        SendPacketServerEvent.emit(packet, true);

        if(ComponentMappers.client.has(entity))
        {//es ist ein neuer client -> diese alle bestehenden sync objects senden
            System.out.print("Send all enteties to new Player");
            Serverclientsocket client = ComponentMappers.client.get(entity).client;
            InitEntityPacket initPacket = new InitEntityPacket(0,"",0,0,0);
            for(int i=0;i<entities.size();i++)
            {
                //Entity sendEnd = entities.get(i);
                PositionSynchComponent sendComp = ComponentMappers.positionSynch.get(entities.get(i));
                initPacket.entityID = sendComp.networkID;
                initPacket.xPos = sendComp.lastX;
                initPacket.yPos = sendComp.lastY;
                initPacket.rotation = sendComp.lastRot;
                client.sendPacketSave(initPacket,true);
            }
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
