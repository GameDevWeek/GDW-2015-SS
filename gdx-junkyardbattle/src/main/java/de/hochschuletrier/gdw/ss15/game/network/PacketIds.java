package de.hochschuletrier.gdw.ss15.game.network;

/**
 * Created by lukas on 22.09.15.
 */

import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.PacketFactory;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;

public enum PacketIds
{
    Position((short)0),
    Simple((short)1);

    private final short m_Value;
    private PacketIds(short value)
    {
        m_Value=value;
    }

    public short getValue()
    {
        return m_Value;
    }

    public static void RegisterPackets()
    {
        PacketFactory.registerPacket(Position.getValue(), EntityPacket.class);
        PacketFactory.registerPacket(Simple.getValue(), SimplePacket.class);
    }
}
