package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hherm on 23/09/2015.
 */
public class MovementPacket extends Packet {

    public float xPos;
    public float yPos;
    public float rotation;

    public MovementPacket(){
        super(PacketIds.Movement.getValue());
    }

    public MovementPacket(float xPos, float yPos, float rotation){
        super(PacketIds.Movement.getValue());
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
    }
    
    public void set(float xPos, float yPos, float rotation) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeFloat(xPos);
        dataOutput.writeFloat(yPos);
        dataOutput.writeFloat(rotation);
       // dataOutput.writeChars();
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        xPos = input.readFloat();
        yPos = input.readFloat();
        rotation = input.readFloat();

        //input.a
    }

    @Override
    public int getSize() {
        return ((3*Float.SIZE)/8);
    }
}
