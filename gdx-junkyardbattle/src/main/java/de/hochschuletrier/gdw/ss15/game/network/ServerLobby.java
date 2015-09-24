package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lukas on 24.09.15.
 */
public class ServerLobby
{


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


    String Mapname;


    LinkedList<LobyClient> connectedClients = new LinkedList<>();


    public ServerLobby()
    {

    }

    public void init()
    {

    }

    public void remove()
    {

    }

    public void update(float deltatime)
    {

    }

    public boolean InserNewPlayer(Serverclientsocket sock)
    {


    }
}
