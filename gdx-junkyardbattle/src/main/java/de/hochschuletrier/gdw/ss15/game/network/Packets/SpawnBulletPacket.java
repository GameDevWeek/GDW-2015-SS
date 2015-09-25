package de.hochschuletrier.gdw.ss15.game.network.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class SpawnBulletPacket extends Packet{

    public Vector2 position;
    public float rotation;
    
    public SpawnBulletPacket() {
        super(PacketIds.SpawnBullet.getValue());
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeFloat(position.x);
        dataOutput.writeFloat(position.y);
        dataOutput.writeFloat(rotation);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        position = new Vector2(0, 0);
        position.x = input.readFloat();
        position.y = input.readFloat();
        rotation = input.readFloat();
    }

    @Override
    public int getSize() {
        return 3 * Float.SIZE / 8;
    }

}
