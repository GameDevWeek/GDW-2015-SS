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

        return false;
    }
}
