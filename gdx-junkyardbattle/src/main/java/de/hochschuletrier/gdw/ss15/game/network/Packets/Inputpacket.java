package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by lukas on 22.09.15.
 */
public class Inputpacket extends Packet
{
    public Inputpacket()
    {
        super(PacketIds.Input.getValue());
    }

    public void AddEvents(int even,long timestamp) {

    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {

    }

    @Override
    public int getSize() {
        return -1;
    }
}
