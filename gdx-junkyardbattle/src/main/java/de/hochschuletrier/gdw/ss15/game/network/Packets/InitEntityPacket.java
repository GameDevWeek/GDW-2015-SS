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

    public InitEntityPacket(){
        super(PacketIds.InitEntity.getValue());
    }

    public InitEntityPacket(long entityID, String name){
        super(PacketIds.InitEntity.getValue());
        this.entityID = entityID;
        this.name = name;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeLong(entityID);
        dataOutput.writeChars(name);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        entityID = input.readLong();
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
