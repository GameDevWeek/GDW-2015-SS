package de.hochschuletrier.gdw.ss15.game.network.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class SpawnBulletPacket extends Packet{

    public Vector2 position = new Vector2(0, 0);;
    public float rotation;
    public Vector2 playerPosition = new Vector2(0, 0);
    public float playerRotation = 0.f;
    
    public SpawnBulletPacket() {
        super(PacketIds.SpawnBullet.getValue());
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeFloat(position.x);
        dataOutput.writeFloat(position.y);
        dataOutput.writeFloat(rotation);
        dataOutput.writeFloat(playerPosition.x);
        dataOutput.writeFloat(playerPosition.y);
        dataOutput.writeFloat(playerRotation);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        position.x = input.readFloat();
        position.y = input.readFloat();
        rotation = input.readFloat();
        playerPosition.x = input.readFloat();
        playerPosition.y = input.readFloat();
        playerRotation = input.readFloat();
    }

    @Override
    public int getSize() {
        return (6 * Float.SIZE) / 8;
    }

}
