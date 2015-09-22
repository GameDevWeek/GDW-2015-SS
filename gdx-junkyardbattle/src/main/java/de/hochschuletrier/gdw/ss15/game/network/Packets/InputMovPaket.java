package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Martin on 22.09.2015.
 *
 */
public class InputMovPaket extends Packet {

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public InputMovPaket(){
        super(PacketIds.Input.getValue());
    }

    public InputMovPaket(boolean up, boolean down, boolean left, boolean right) {
        super(PacketIds.Input.getValue());
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        String tempString = "MovementKeys:\n";
        tempString += "up: " + this.up + "\n";
        tempString += "down: " + this.down + "\n";
        tempString += "left: " + this.left + "\n";
        tempString += "right: " + this.right + "\n";
        return tempString;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeBoolean(up);
        dataOutput.writeBoolean(down);
        dataOutput.writeBoolean(left);
        dataOutput.writeBoolean(right);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        up = input.readBoolean();
        down = input.readBoolean();
        left = input.readBoolean();
        right = input.readBoolean();
    }

    @Override
    public int getSize() {
        return 4;
    }
}
