package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data;

public enum PacketIds {

    Component((short) 0),
    Entity((short) 1);

    private final short m_Value;

    private PacketIds(short value) {
        m_Value = value;
    }

    public short getValue() {
        return m_Value;
    }

    public static void RegisterPackets() {
        PacketFactory.registerPacket(Component.getValue(), ComponentPacket.class);
    }
}