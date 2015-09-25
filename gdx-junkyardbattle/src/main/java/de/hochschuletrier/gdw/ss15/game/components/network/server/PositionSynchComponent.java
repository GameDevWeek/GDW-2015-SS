package de.hochschuletrier.gdw.ss15.game.components.network.server;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
//import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.interfaces.Sendable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PositionSynchComponent extends Component implements Pool.Poolable {

    public static long currentID = 0;
    public long networkID = currentID++;
    public String clientName;
    public float lastX;
    public float lastY;
    public float lastVelocityX;
    public float lastVelocityY;
    public float lastRot;
    public MyTimer lastSendTimer;
    public float updateDuration;
    public boolean sendSave;

    public boolean inited;

    @Override
    public void reset() {
        networkID = currentID++;
        clientName=null;
        lastX=0;
        lastY=0;
        lastVelocityX = 0;
        lastVelocityY = 0;
        lastRot=0;
        lastSendTimer=null;
        updateDuration=42;
        sendSave=false;
        inited=false;
    }

    /*
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
    */
}
