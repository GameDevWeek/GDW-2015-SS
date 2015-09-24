package de.hochschuletrier.gdw.ss15.game.network.Packets.Menu;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hherm on 24/09/2015.
 */
public class MenuInfoPacket extends Packet {

    public int happening;
    public long bla;
    public String name;

    public MenuInfoPacket(){
        super(PacketIds.MenuInfo.getValue());
    }

    public MenuInfoPacket(int happening, long bla, String name){
        super(PacketIds.MenuInfo.getValue());
        this.happening = happening;
        this.bla = bla;
        this.name = name;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeInt(happening);
        dataOutput.writeLong(bla);
        dataOutput.writeChars(name);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        happening = input.readInt();
        bla = input.readLong();

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
