package de.hochschuletrier.gdw.ss15.game.network.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;


public class HealthPacket extends Packet
{
	public int health;
    public long id;
    
	public HealthPacket() {
		super(PacketIds.Health.getValue());
		this.health = health;
        this.id = 0;
	}

	@Override
	protected void pack(DataOutputStream dataOutput) throws IOException {
		dataOutput.writeFloat(health);
        dataOutput.writeLong(id);
	}

	@Override
	protected void unpack(DataInputStream input) throws IOException {
		health = input.readInt();
        id = input.readLong();
	}

	@Override
	public int getSize() {
		return ((Integer.SIZE+Long.SIZE)/8);
	}
    
}