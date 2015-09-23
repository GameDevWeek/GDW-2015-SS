package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketDisconnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import de.hochschuletrier.gdw.ss15.states.GameplayState;
import de.hochschuletrier.gdw.ss15.states.MainMenuState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.ConnectionEventListener;
import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class ClientConnection implements SendPacketClientEvent.Listener,
                                         SocketConnectListener,
                                         SocketDisconnectListener,
                                         SocketListener {
    private Clientsocket clientSocket = null;

    public ClientConnection()
    {
    }

    public void init()
    {
        Main.getInstance().console.register(clientCommand);
    }

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);
    ConsoleCmd clientCommand = new ConsoleCmd("client", 0, "Connected und disconncted fom server", 1) {
        @Override
        public void execute(List<String> list){
            String info = list.get(1);
            if(info.equals("connect"))
            {
                if(list.size()>2){//zusätzlich ip
                    logger.info("Try to connection to "+list.get(2));
                    connect(list.get(2),12345);
                }
                else {logger.info("Try to connection to localhost");
                   connect("localhost", 12345);
                }
            }
            else if(info.equals("disconnect"))
            {
                disconnect();
            }
            else
            {
                logger.error(info+" falsches parameter für command client");
            }
        }
    };

    public void update()
    {
        if(clientSocket!=null) {
            //clientSocket.justCallDisconnectHandler();
            clientSocket.callListeners();
            if(!clientSocket.isConnected() && !clientSocket.isByConnect())
            {
                logger.warn("Lost connection to server");
                clientSocket.close();
                clientSocket=null;
            }
        }
    }

    public Clientsocket getSocket()
    {
        return clientSocket;
    }

    public void connect(String ip,int port)
    {
        if(clientSocket!=null && clientSocket.isConnected())
        {
            logger.warn("Client bereits verbunden");
            return;
        }
        if(clientSocket!=null)
        {
            clientSocket.close();
            clientSocket=null;
        }
        clientSocket = new Clientsocket(ip,port,true);
        // Listener registrieren
        clientSocket.registerConnectListner(this);
        clientSocket.registerDisconnectListener(this);
        clientSocket.registerListener(this);

        clientSocket.connect();
        SendPacketClientEvent.registerListener(this);
    }

    public void disconnect()
    {
        if(clientSocket==null)
        {
            logger.error("Clint war nicht verbunden");
        }
        else
        {
            clientSocket.close();
            clientSocket=null;
            logger.info("Connection beendet");
        }
    }

    /*
    public void loginFinished(ConnectStatus status)
    {
        if(status == ConnectStatus.Succes)
        {
            logger.info("Login am server erfollgreich");
        }
        else
        {
            logger.error("Login gescheitert wegen: "+status);
        }
    }*/

    public void onSendSClientPacket(Packet pack,boolean save)
    {
        //System.out.print("Packet send");
        if(clientSocket!=null && clientSocket.isConnected())
        {
            if(save == true)
            {
                clientSocket.sendPacketSave(pack);
            }
            else
            {
                clientSocket.sendPacketUnsave(pack);
            }
        }
    }

    @Override
    public void loginFinished(ConnectStatus status) {

    }

    @Override
    public void socketDisconnected() {
        Main.getInstance().changeState(new MainMenuState(Main.getInstance().getAssetManager()));
    }

    @Override
    public void receivedPacket(Packet packet, boolean receivedSave) {
        DoNotTouchPacketEvent.emit(packet);
    }
}
