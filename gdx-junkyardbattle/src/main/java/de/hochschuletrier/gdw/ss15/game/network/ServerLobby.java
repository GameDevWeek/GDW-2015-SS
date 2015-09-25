package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.ChangeNamePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.MenuePlayerChangedPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lukas on 24.09.15.
 */
public class ServerLobby
{

    String Mapname;
    private int MaximumPlayers = 8;
    private float SecondsToStart = 20;
    MyTimer timer = new MyTimer(true);

    public LinkedList<LobyClient> connectedClients = new LinkedList<>();


    public ServerLobby()
    {

    }

    public void init()
    {

    }

    public void remove()
    {

    }

    public void SendStartGame()
    {
        SendPackettoAll(new SimplePacket(SimplePacket.SimplePacketId.StartGame.getValue(), 0));
    }

    public boolean update(float deltatime)
    {
        Iterator<LobyClient> it = connectedClients.iterator();
        while(it.hasNext())
        {
            LobyClient client = it.next();
            if(!client.socket.isConnected())
            {
                it.remove();
            }
            else
            {
                while (client.socket.isPacketAvaliable())
                {
                    ReceivePacket(client);
                }
            }
        }

        timer.Update();
        if(timer.get_CounterSeconds()>SecondsToStart)
        {
            if(connectedClients.size()==0)
            {
                timer.StartCounterS(SecondsToStart);
                SendPackettoAll(new SimplePacket(SimplePacket.SimplePacketId.TimeMenuePacket.getValue(),(long)SecondsToStart));
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    public void ReceivePacket(LobyClient client)
    {
        Packet pack = client.socket.getReceivedPacket();
        if(pack.getPacketId() == PacketIds.Simple.getValue())
        {
            SimplePacket spacket = (SimplePacket) pack;
            if(spacket.m_SimplePacketId == SimplePacket.SimplePacketId.ChangeTeamPacket.getValue())
            {
                client.Team1 = !client.Team1;
                SendChangePlayerStatusToAll(client);
            }
        }
        if(pack.getPacketId() == PacketIds.ChangeName.getValue())
        {
            System.out.println("name changed");
            ChangeNamePacket nPack = (ChangeNamePacket)pack;
            client.name = nPack.name;
            SendChangePlayerStatusToAll(client);


            SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.TimeMenuePacket.getValue(),(long)(SecondsToStart-timer.get_CounterSeconds()));
            client.socket.sendPacket(sPack, true);
        }
    }

    public boolean InserNewPlayer(Serverclientsocket sock) {
        if (connectedClients.size() >= MaximumPlayers) {
            return false;
        }
        LobyClient client = new LobyClient(sock);
        SendAllTonewPlyer(client);
        connectedClients.push(client);
        SendChangePlayerStatusToAll(client);
        SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.TimeMenuePacket.getValue(),(long)SecondsToStart);
        SendPacketClientEvent.emit(sPack,true);

        return true;
    }

    public void SendChangePlayerStatusToAll(LobyClient info)
    {
        SendPackettoAll(info.getPacket());
    }

    public void SendPackettoAll(Packet pack)
    {
        int i=0;
        for(LobyClient client : connectedClients) {
            client.socket.sendPacketSave(pack,i++==0);
        }
    }

    public void SendAllTonewPlyer(LobyClient client)
    {
        for(LobyClient info : connectedClients) {
            MenuePlayerChangedPacket pack = info.getPacket();
            client.socket.sendPacketSave(pack,true);
        }
    }


}