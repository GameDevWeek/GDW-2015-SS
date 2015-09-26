package de.hochschuletrier.gdw.ss15.game.network.Packets.Menu;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by hherm on 24/09/2015.
 */
public class MenuePlayerChangedPacket extends Packet {

    public int id;
    public boolean team;
    public String name;

    public MenuePlayerChangedPacket(){
        super(PacketIds.MenuInfo.getValue());
    }

    public MenuePlayerChangedPacket(int id, boolean team, String name){
        super(PacketIds.MenuInfo.getValue());
        this.team = team;
        this.id = id;
        this.name = name;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeInt(id);
        dataOutput.writeBoolean(team);
        dataOutput.writeChars(name);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        id = input.readInt();
        team = input.readBoolean();

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