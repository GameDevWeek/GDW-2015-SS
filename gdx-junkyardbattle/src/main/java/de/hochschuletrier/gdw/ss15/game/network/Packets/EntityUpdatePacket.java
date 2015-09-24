package de.hochschuletrier.gdw.ss15.game.network.Packets;

import com.badlogic.ashley.core.Entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;

public class EntityUpdatePacket extends Packet {

    public float xPos;
    public float yPos;
    public float velocityX;
    public float velocityY;
    public float rotation;
    public long entityID;

    public EntityUpdatePacket(){
        super(PacketIds.EntityUpdate.getValue());
    }

    public EntityUpdatePacket(long entityID, float xPos, float yPos, float velocityX, float velocityY, float rotation){
        super(PacketIds.EntityUpdate.getValue());
        this.entityID = entityID;
        this.xPos = xPos;
        this.yPos = yPos;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.rotation = rotation;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeLong(entityID);
        dataOutput.writeFloat(xPos);
        dataOutput.writeFloat(yPos);
        dataOutput.writeFloat(velocityX);
        dataOutput.writeFloat(velocityY);
        dataOutput.writeFloat(rotation);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        entityID = input.readLong();
        xPos = input.readFloat();
        yPos = input.readFloat();
        velocityX = input.readFloat();
        velocityY = input.readFloat();
        rotation = input.readFloat();
    }

    @Override
    public int getSize() {
        return ((Long.SIZE+(5*Float.SIZE))/8);
    }
}
