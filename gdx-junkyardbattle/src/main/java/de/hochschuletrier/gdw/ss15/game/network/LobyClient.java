package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.MenuePlayerChangedPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;


public class LobyClient
{
    public String name = "no name";
    public Serverclientsocket socket;
    public boolean Team1 = true;
    private static int nextid = 0;
    public int id = nextid++;

    public LobyClient(Serverclientsocket sock)
    {
        socket = sock;
    }

    public MenuePlayerChangedPacket getPacket()
    {
        return new MenuePlayerChangedPacket(id,Team1,name);
    }
}
