package de.hochschuletrier.gdw.ss15.game.network.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;


public class HealthPacket extends Packet
{
	public int health;

	public HealthPacket() {
		super(PacketIds.Health.getValue());
		this.health = health;
	}

	@Override
	protected void pack(DataOutputStream dataOutput) throws IOException {
		dataOutput.writeFloat(health);
		
	}

	@Override
	protected void unpack(DataInputStream input) throws IOException {
		health = input.readInt();
		
	}

	@Override
	public int getSize() {
		return ((Integer.SIZE)/8);
	}
    
}