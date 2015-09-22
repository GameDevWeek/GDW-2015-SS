package de.hochschuletrier.gdw.ss15.game.network.Packets;

import com.badlogic.ashley.core.Entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;

public class EntityPacket extends Packet {

    public float xPos;
    public float yPos;
    public float rotation;
    public long entityID;

    public EntityPacket(){
        super(PacketIds.Position.getValue());
    }

    public EntityPacket(long entityID, float xPos, float yPos, float rotation){
        super(PacketIds.Position.getValue());
        this.entityID = entityID;
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeLong(entityID);
        dataOutput.writeFloat(xPos);
        dataOutput.writeFloat(yPos);
        dataOutput.writeFloat(rotation);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        entityID = input.readLong();
        xPos = input.readFloat();
        yPos = input.readFloat();
        rotation = input.readFloat();
    }

    @Override
    public int getSize() {
        return ((Long.SIZE+(3*Float.SIZE))/8);
    }
}
