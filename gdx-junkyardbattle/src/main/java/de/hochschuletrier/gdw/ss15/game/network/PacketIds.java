package de.hochschuletrier.gdw.ss15.game.network;

/**
 * Created by lukas on 22.09.15.
 */

import de.hochschuletrier.gdw.ss15.game.network.Packets.*;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.PacketFactory;

public enum PacketIds
{
    Movement((short)0),
    Simple((short)1),
    InitEntity((short)2),
    Health((short)3),
    EntityUpdate((short)5),
    Fire((short)6),
    MenuInfo((short)7),
    Gather((short)8),
    ChangeName((short)9),
    Damage((short)10),
    SpawnBullet((short)11),
    Highscore((short)12),
    ReceiveShootClient((short)15);



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
        PacketFactory.registerPacket(EntityUpdate.getValue(), EntityUpdatePacket.class);
        PacketFactory.registerPacket(MenuInfo.getValue(), MenuePlayerChangedPacket.class);
        PacketFactory.registerPacket(ChangeName.getValue(), ChangeNamePacket.class);
        PacketFactory.registerPacket(Health.getValue(), HealthPacket.class); 
        PacketFactory.registerPacket(Fire.getValue(), FirePacket.class);
        PacketFactory.registerPacket(Gather.getValue(), GatherPacket.class);
        PacketFactory.registerPacket(Damage.getValue(), GatherPacket.class);
        PacketFactory.registerPacket(SpawnBullet.getValue(), SpawnBulletPacket.class);
        PacketFactory.registerPacket(Highscore.getValue(), HighscorePacket.class);
        PacketFactory.registerPacket(ReceiveShootClient.getValue(), ReciveShotPacketClient.class);
    }
}
