package de.hochschuletrier.gdw.ss15.game.network;

/**
 * Created by lukas on 22.09.15.
 */

import de.hochschuletrier.gdw.ss15.game.network.Packets.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.PacketFactory;

public enum PacketIds
{
    Movement((short)0),
    Simple((short)1),
    InitEntity((short)2),
    Input((short)3),
    InputEvent((short)4),
    EntityUpdate((short)5),
    Health((short)6),
    Damage((short)7);


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
        PacketFactory.registerPacket(Movement.getValue(), MovementPacket.class);
        PacketFactory.registerPacket(Simple.getValue(), SimplePacket.class);
        PacketFactory.registerPacket(InitEntity.getValue(), InitEntityPacket.class);
        PacketFactory.registerPacket(Input.getValue(), Inputpacket.class);
        PacketFactory.registerPacket(InputEvent.getValue(), InputEventPacket.class);
        PacketFactory.registerPacket(EntityUpdate.getValue(), EntityUpdatePacket.class);
        PacketFactory.registerPacket(Health.getValue(), HealthPacket.class);
        
    }
}
