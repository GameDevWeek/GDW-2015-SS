package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.interfaces.Sendable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PositionSynchComponent extends Component implements Pool.Poolable, Sendable {

    public static long currentID = 0;
    public long networkID = currentID++;
    public boolean was_moved;

    @Override
    public void reset() {
        networkID = currentID++;
        was_moved = false;
    }

    @Override
    public void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeLong(networkID);
        dataOutput.writeBoolean(was_moved);
    }

    @Override
    public void unpack(DataInputStream input) throws IOException {
        networkID = input.readLong();
        was_moved = input.readBoolean();
    }
}
