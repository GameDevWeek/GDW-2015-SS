package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hherm on 22/09/2015.
 */
public class InitEntityPacket extends Packet {

    public long entityID;
    public String name;
    public float xPos, yPos, rotation;

    public InitEntityPacket(){
        super(PacketIds.InitEntity.getValue());
    }

    public InitEntityPacket(long entityID, String name, float xPos, float yPos, float rotation){
        super(PacketIds.InitEntity.getValue());
        this.entityID = entityID;
        this.name = name;
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
        dataOutput.writeChars(name);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        entityID = input.readLong();
        xPos = input.readFloat();
        yPos = input.readFloat();
        rotation = input.readFloat();
        StringBuffer buffer = new StringBuffer();
        while(input.available() > 1){
            buffer.append(input.readChar());
        }
        name = buffer.toString();
    }

    @Override
    public int getSize() {
        return -1;
    }
}
