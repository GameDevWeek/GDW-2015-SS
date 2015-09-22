package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data;

import com.badlogic.ashley.core.Entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EntityPacket extends Packet {

    Entity m_entity;

    public EntityPacket(){
        super(PacketIds.Entity.getValue());
    }

    public EntityPacket(Entity entity){
        super(PacketIds.Entity.getValue());
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
