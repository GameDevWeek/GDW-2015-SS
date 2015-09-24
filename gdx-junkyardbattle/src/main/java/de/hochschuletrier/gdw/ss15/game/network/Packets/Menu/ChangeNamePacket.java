package de.hochschuletrier.gdw.ss15.game.network.Packets.Menu;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hherm on 24/09/2015.
 */
public class ChangeNamePacket extends Packet {

    public String name;

    public ChangeNamePacket(){
        super(PacketIds.ChangeName.getValue());
    }

    public ChangeNamePacket(String name){
        super(PacketIds.ChangeName.getValue());
        this.name = name;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeChars(name);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        StringBuffer buffer = new Stringbuffer();
    }

    @Override
    public int getSize() {
        return 0;
    }
}
