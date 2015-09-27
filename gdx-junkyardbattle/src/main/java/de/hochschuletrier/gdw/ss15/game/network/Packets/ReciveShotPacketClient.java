package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by lukas on 27.09.15.
 */
public class ReciveShotPacketClient extends Packet {
    public ReciveShotPacketClient() {
        super(PacketIds.ReceiveShootClient.getValue());
    }

    public ReciveShotPacketClient(long id, int what) {
        super(PacketIds.ReceiveShootClient.getValue());
        networkId = id;
        this.what = what;
    }

    public long networkId;
    public int what;


    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeLong(networkId);
        dataOutput.writeInt(what);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        networkId = input.readLong();
        what = input.readInt();
    }

    @Override
    public int getSize() {
        return (Integer.SIZE+Long.SIZE)/8;
    }
}
