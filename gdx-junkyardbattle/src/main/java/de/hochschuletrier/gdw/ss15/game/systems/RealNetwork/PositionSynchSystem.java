package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SpawnBulletPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
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

    private EntityUpdatePacket pack = new EntityUpdatePacket();

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
            PhysixBodyComponent physComp = ComponentMappers.physixBody.get(ent);
            //physComp.setGravityScale(0);

            PositionSynchComponent comp = ComponentMappers.positionSynch.get(ent);
            if(!comp.inited)
            {
                InitEnitity(ent);
                comp.inited=true;
            }



            if(ComponentMappers.position.has(ent))
            {
                PositionComponent pos = ComponentMappers.position.get(ent);

                comp.lastSendTimer.Update();
                if(comp.lastSendTimer.get_CounterMilliseconds()>comp.updateDuration)
                {
                    comp.lastSendTimer.StartCounter();
                    comp.lastX=physComp.getPosition().x;
                    comp.lastY=physComp.getPosition().y;
                    comp.lastVelocityX =physComp.getLinearVelocity().x;
                    comp.lastVelocityY =physComp.getLinearVelocity().y;
                    comp.lastRot=pos.rotation;

                    pack.entityID = comp.networkID;
                    pack.xPos = comp.lastX;
                    pack.yPos = comp.lastY;
                    pack.velocityX = comp.lastVelocityX;
                    pack.velocityY = comp.lastVelocityY;
                    pack.rotation = comp.lastRot;

                    pack.setTimestamp(MyTimer.get_TimestampNanoseconds());

                    //System.out.println(comp.sendSave);
                    SendPacketServerEvent.emit(pack, comp.sendSave);
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


    }

    public void InitEnitity(Entity entity)
    {
        Entity exept = null;
        if(ComponentMappers.client.has(entity))
        {//es ist ein neuer client -> diese alle bestehenden sync objects senden
            Serverclientsocket client = ComponentMappers.client.get(entity).client;
            InitEntityPacket initPacket = new InitEntityPacket(0,"",0,0,0,0,0);
            for(int i=0;i<entities.size();i++)
            {
                Entity sendEnd = entities.get(i);
                PositionSynchComponent sendComp = ComponentMappers.positionSynch.get(sendEnd);
                PhysixBodyComponent phcomp = ComponentMappers.physixBody.get(entity);
                initPacket.entityID = sendComp.networkID;
                initPacket.xPos = phcomp.getX();
                initPacket.yPos = phcomp.getY();
                initPacket.rotation = phcomp.getAngle();
                initPacket.veloX = sendComp.lastVelocityX;
                initPacket.veloY = sendComp.lastVelocityY;
                if(sendEnd == entity) {//eigener spieler
                    initPacket.name = "clientOwnPlayer";
                    exept=entity;
                }
                else
                {
                    initPacket.name = sendComp.clientName;
                }
                FlagPacketIfPlayer(initPacket,entity);
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

        PhysixBodyComponent phcomp = ComponentMappers.physixBody.get(entity);

        PositionSynchComponent synComp = ComponentMappers.positionSynch.get(entity);

        //send Init packet to all Players
        InitEntityPacket packet;
        packet = new InitEntityPacket(synComp.networkID, synComp.clientName, phcomp.getX(), phcomp.getY(), comp.rotation, phcomp.getLinearVelocity().x, phcomp.getLinearVelocity().y);

        FlagPacketIfPlayer(packet,entity);//very ugly but fast fix hope no one ever finds it

        SendPacketServerEvent.emit(packet, true, exept);

        if(ComponentMappers.bullet.has(entity)){
            PositionSynchComponent sendComp = ComponentMappers.positionSynch.get(entity);
            BulletComponent bullet = ComponentMappers.bullet.get(entity);

            SpawnBulletPacket spawnBulletPacket = new SpawnBulletPacket();
            spawnBulletPacket.bulletID = sendComp.networkID;
            spawnBulletPacket.rotation = bullet.rotation;
            spawnBulletPacket.power = bullet.power;
            spawnBulletPacket.playerRotation = bullet.playerrotation;
            spawnBulletPacket.playerPosition = bullet.playerpos;
            SendPacketServerEvent.emit(spawnBulletPacket, true);

        }
    }

    public void FlagPacketIfPlayer(InitEntityPacket pack,Entity ent)
    {
        if(ComponentMappers.player.has(ent) && ComponentMappers.player.get(ent).teamID==0)
        {
            pack.entityID*=-1;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        SimplePacket spacket = new SimplePacket(SimplePacket.SimplePacketId.RemoveEntity.getValue(),ComponentMappers.positionSynch.get(entity).networkID);
        SendPacketServerEvent.emit(spacket, true,entity);

    }
}
