package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.MenuePlayerChangedPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
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
    private int MaximumPlayers = 10000;
    private float SecondsToStart = 60;

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
        for(LobyClient client : connectedClients)
        {
            while(client.socket.isPacketAvaliable())
            {

            }
        }
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
    }

    public boolean InserNewPlayer(Serverclientsocket sock) {
        if (connectedClients.size() >= MaximumPlayers) {
            return false;
        }
        LobyClient client = new LobyClient(sock);
        SendAllTonewPlyer(client);
        connectedClients.push(client);
        SendChangePlayerStatusToAll(client);
        return true;
    }

    public void SendChangePlayerStatusToAll(LobyClient info)
    {
        MenuePlayerChangedPacket pack = info.getPacket();
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
