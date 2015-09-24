package de.hochschuletrier.gdw.ss15.game.network.Packets;

/**
 * Created by lukas on 22.09.15.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;

public class SimplePacket extends Packet
{
    public int m_SimplePacketId=-1;
    public long m_Moredata=-1;

    public enum SimplePacketId
    {
        RemoveEntity((short)0),
        ConnectInitPacket((short)1);

        private final short m_Value;
        private SimplePacketId(short value)
        {
            m_Value=value;
        }

        public short getValue()
        {
            return m_Value;
        }

    }

    public SimplePacket(int simplepacketid,long moreData)
    {
        super(PacketIds.Simple.getValue());
        m_SimplePacketId = simplepacketid;
        m_Moredata = moreData;
    }

    public SimplePacket()
    {
        super(PacketIds.Simple.getValue());

    }
    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeInt(m_SimplePacketId);
        dataOutput.writeLong(m_Moredata);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        // TODO Auto-generated method stub
        m_SimplePacketId = input.readInt();
        m_Moredata = input.readLong();
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return (Integer.SIZE+Long.SIZE)/8;
    }

}