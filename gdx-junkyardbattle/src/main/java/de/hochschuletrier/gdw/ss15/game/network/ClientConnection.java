package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class ClientConnection implements SocketConnectListener {
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
                connect("localhost", 12345);
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
            clientSocket.callListeners();
            if(clientSocket.isConnected())
            {
                clientSocket.close();
                clientSocket=null;
            }
        }
    }

    public void connect(String ip,int port)
    {
        if(clientSocket!=null && clientSocket.isConnected())
        {
            logger.warn("Client bereits verbunden");
        }
        if(clientSocket!=null)
        {
            clientSocket.close();
        }
        clientSocket = new Clientsocket(ip,port,true);
        clientSocket.registerConnectListner(this);
        clientSocket.connect();
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
    }
}