package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
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

import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class NetworkClientSystem extends EntitySystem implements SocketDisconnectListener {

    private MyTimer timer = new MyTimer(true);

    Game game = null;
    ClientConnection connection = Main.getInstance().getClientConnection();

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
    }


    public void dispose(){
    }

}

