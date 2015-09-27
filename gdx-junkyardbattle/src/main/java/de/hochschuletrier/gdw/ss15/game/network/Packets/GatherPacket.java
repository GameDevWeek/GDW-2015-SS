package de.hochschuletrier.gdw.ss15.game.network.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class GatherPacket extends Packet{
	
	public float channelTime = 0;

	public GatherPacket() {
		 super(PacketIds.Gather.getValue());
	}
	
	public GatherPacket(float channelTime){
		this();
		this.channelTime = channelTime;
	}
	
	public void set(float channelTime) {
	    this.channelTime = channelTime;
	}

	@Override
	protected void pack(DataOutputStream dataOutput) throws IOException {
		dataOutput.writeFloat(channelTime);
	}

	@Override
	protected void unpack(DataInputStream input) throws IOException {
		channelTime = input.readFloat();
	}

	@Override
	public int getSize() {
		return Float.SIZE / 8;
	}

}
