package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.NetworkPositionEvent;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InputMovPaket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketDisconnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.DisconnectHandler;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.hochschuletrier.gdw.ss15.events.GatherUpEvent;

import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class NetworkClientSystem extends EntitySystem {

    private MyTimer timer = new MyTimer(true);

    Game game = null;
    ClientConnection connection = Main.getInstance().getClientConnection();
    long lastNetworkTimestamp = 0;

    private static final Logger logger = LoggerFactory.getLogger(NetworkClientSystem.class);

    public void socketDisconnected()
    {

    }



    public NetworkClientSystem(Game game,int priority) {

        super(priority);
        this.game=game;
    }

    @Override
    public void update(float deltaTime)
    {
        /*
        //TODO check all input components
        timer.Update();
        if(timer.get_CounterMilliseconds() > 200){
            InputMovPaket inputPacket = new InputMovPaket(game.getInputSystem().keyDown(Input.Keys.W),
                    game.getInputSystem().keyDown(Input.Keys.S),
                    game.getInputSystem().keyDown(Input.Keys.A),
                    game.getInputSystem().keyDown(Input.Keys.D));
            connection.getSocket().sendPacketUnsave(inputPacket);
            timer.ResetTimer();
        }
        */

        Clientsocket socket = connection.getSocket();
        if(socket != null)
        {
            while(socket.isPacketAvaliable())
            {
                System.out.println("Received packet");
                ReceivedPacket(socket.getReceivedPacket());
            }
        }
    }

    private void ReceivedPacket(Packet pack)
    {
        System.out.println("received packet");
        if(pack.getPacketId()== PacketIds.InitEntity.getValue())
        {
            InitEntityPacket iPacket = (InitEntityPacket) pack;
            logger.info("Spawned entitiy with name: "+iPacket.name);
            game.createEntity(iPacket.name,0,0);
        }
        else if(pack.getPacketId() == PacketIds.Position.getValue())
        {//positino update packet
            if(pack.getTimestamp()>lastNetworkTimestamp)
            {//synccompoent
                lastNetworkTimestamp = pack.getTimestamp();
                EntityPacket ePacket = (EntityPacket) pack;


                NetworkPositionEvent.emit(null,ePacket.xPos,ePacket.yPos,ePacket.rotation,false);
            }
        }
    }


    public void dispose(){
    }


}

