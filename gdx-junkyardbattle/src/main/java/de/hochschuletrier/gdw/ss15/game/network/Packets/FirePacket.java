package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by oliver on 24.09.15.
 */
public class FirePacket extends Packet {

    public float channeltime = 0;

    public FirePacket() {
        super(PacketIds.Fire.getValue());
    }

    public FirePacket(float channelTime){
        this();
        this.channeltime = channelTime;
    }
    
    public void set(float channelTime) {
        this.channeltime = channelTime;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeFloat(channeltime);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        channeltime = input.readFloat();
    }

    @Override
    public int getSize() {
        return (Float.SIZE) / 8;
    }
}
