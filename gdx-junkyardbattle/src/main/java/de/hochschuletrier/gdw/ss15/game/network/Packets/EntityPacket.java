package de.hochschuletrier.gdw.ss15.game.network.Packets;

import com.badlogic.ashley.core.Entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;

public class EntityPacket extends Packet {

    Entity m_entity;

    public EntityPacket(){
        super(PacketIds.Position.getValue());
    }

    public EntityPacket(Entity entity){
        super(PacketIds.Position.getValue());
        this.m_entity = entity;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {

    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {

    }

    @Override
    public int getSize() {
        return 0;
    }
}
