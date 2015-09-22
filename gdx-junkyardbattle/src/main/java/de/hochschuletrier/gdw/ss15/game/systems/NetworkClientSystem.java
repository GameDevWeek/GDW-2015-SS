package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketDisconnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.DisconnectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class NetworkClientSystem extends EntitySystem implements SocketDisconnectListener {


    ClientConnection connection = Main.getInstance().getClientConnection();

    public void socketDisconnected()
    {

    }



    public NetworkClientSystem(int priority) {
        super(priority);
    }



    @Override
    public void update(float deltaTime)
    {
        //TODO check all input components

        Clientsocket socket = connection.getSocket();
        if(socket != null)
        {
            while(socket.isPacketAvaliable())
            {
                ReceivedPacket(socket.getReceivedPacket());
            }
        }
    }

    private void ReceivedPacket(Packet pack)
    {
        System.out.println("received packet");
    }


    public void dispose(){
    }

}

