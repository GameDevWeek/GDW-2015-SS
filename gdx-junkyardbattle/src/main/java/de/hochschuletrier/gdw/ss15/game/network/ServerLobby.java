package de.hochschuletrier.gdw.ss15.game.network;

import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.ChangeNamePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.MenuePlayerChangedPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.utils.Dataholder;
import de.hochschuletrier.gdw.ss15.game.utils.LoadedMaps;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by lukas on 24.09.15.
 */
public class ServerLobby
{

    String Mapname;
    private int MaximumPlayers = 8;
    public float SecondsToStart = 120;
    public float actualTime = 0;

    public LinkedList<LobyClient> connectedClients = new LinkedList<>();

    private int team1 = 0;
    private int team2 = 0;

    public int mapId = 1;

    public void resetCounter()
    {
        actualTime = 0;
        SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.TimeMenuePacket.getValue(),(long)(SecondsToStart-actualTime));
        SendPackettoAll(sPack);
    }

    public ServerLobby()
    {
        //WARNING!!!! hurts by reading XD
        mapId = Dataholder.MapId.get();

    }

    public boolean ChangeMap(String s)
    {

        if(!s.isEmpty())
        {
            int mId = -1;
            for (Map.Entry<String, LoadedMaps> entry : Main.maps.entrySet()) {
                entry.getValue().name.equals(s);
                mId = entry.getValue().id;
                //MaximumPlayers = entry.getValue().playerPerTeam*2;
                //System.out.println("Mapinfo: "+entry.getValue().id+", "+entry.getValue().file);
                break;
            }

            if(mId==-1)
            {
                return false;
            }
            mapId = mId;
            SimplePacket spack = new SimplePacket(SimplePacket.SimplePacketId.MenueMapChange.getValue(),mapId);
            SendPackettoAll(spack);
        }
        else
        {
            return false;
        }
        return true;
    }

    public void init()
    {

    }

    public void remove()
    {

    }

    public void SendStartGame()
    {
        SendPackettoAll(new SimplePacket(SimplePacket.SimplePacketId.StartGame.getValue(), mapId));
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

        actualTime+=deltatime;
        if(actualTime>SecondsToStart)
        {
            if(connectedClients.size()==0)
            {
                actualTime=0;
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
                if(client.Team1)
                {
                    if(team2<4)
                    {
                        team1--;
                        team2++;
                        client.Team1 = !client.Team1;
                        SendChangePlayerStatusToAll(client);
                    }
                }
                else
                {
                    if(team1<4)
                    {
                        team2--;
                        team1++;
                        client.Team1 = !client.Team1;
                        SendChangePlayerStatusToAll(client);
                    }
                }
            }
        }
        if(pack.getPacketId() == PacketIds.ChangeName.getValue())
        {
            ChangeNamePacket nPack = (ChangeNamePacket)pack;
            client.name = nPack.name;
            SendChangePlayerStatusToAll(client);
            SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.TimeMenuePacket.getValue(),(long)(SecondsToStart-actualTime));
            client.socket.sendPacket(sPack, true);
        }
    }

    public boolean InserNewPlayer(Serverclientsocket sock) {
        if (connectedClients.size() >= MaximumPlayers) {
            return false;
        }

        LobyClient client = new LobyClient(sock);

        if(team1>=4)
        {
            client.Team1 = !client.Team1;
        }

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