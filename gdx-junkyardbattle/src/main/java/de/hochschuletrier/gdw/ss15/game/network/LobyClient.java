package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;

/**
 * Created by lukas on 24.09.15.
 */
public class LobyClient
{
    public String name;
    Serverclientsocket socket;
    public boolean Team1;

    public LobyClient(Serverclientsocket sock)
    {
        socket = sock;
    }


}
