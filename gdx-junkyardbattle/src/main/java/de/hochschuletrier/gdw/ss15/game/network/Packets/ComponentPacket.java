package de.hochschuletrier.gdw.ss15.game.network.Packets;

import com.badlogic.ashley.core.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
/*import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.interfaces.Sendable;

public class ComponentPacket<T extends Component & Sendable> extends Packet{

    T component = null;

    public ComponentPacket() {
        super(PacketIds.Component.getValue());
    }

    public ComponentPacket(T component){
        super(PacketIds.Component.getValue());
        this.component = component;
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        component.pack(dataOutput);
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        component.unpack(input);
    }

    @Override
    public int getSize() {
        return 0;
    }
}
*/