package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.LobyClient;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.ServerLobby;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.server.ServerCloneException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.LongBinaryOperator;


/**
 * Created by lukas on 21.09.15.
 */
public class Server implements Runnable
{
    /**
     * Server Command
     */
    ConsoleCmd serverCommand = new ConsoleCmd("serverCommand", 0, "Command for Server", 1) {
        @Override
        public void execute(List<String> list){
            String info = list.get(1);
            if(info.equals("startGame")){
                startGame();
            }
            else if(info.equals("stopGame")){
                stopGame();
            }else if(info.equals("changeMap")){
                if(lobby == null)
                {
                    logger.error("Lobby nicht aktiv");
                }
                //System.out.println("name: "+ list.get(2));
                if(list.size()>=2) {
                    if (lobby.ChangeMap(list.get(2))) {
                        logger.info("Karte wurde geändert");
                    } else {
                        logger.error("karte wurde nicht gefunden");
                    }
                }
            }
            else
            {
                logger.error(info+" falsches parameter für command serverCommand");
            }
        }
    };
    /**
     * End Command
     */

    private AtomicBoolean isRunning = new AtomicBoolean(false);
    Thread runThread;

    Serversocket serversocket = new Serversocket(12345,true);

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    ServerLobby lobby = null;
    ServerGame runningGame = null;
    MyTimer timer = new MyTimer();

    private AtomicBoolean ToStartServer = new AtomicBoolean(false);

    private AtomicBoolean loadfinisched = new AtomicBoolean(false);

    public Serverclientsocket LastConnectedClient = null;

    LinkedList<Serverclientsocket> clientSockets = new LinkedList<>();

    LinkedList<LobyClient> listToAddInGame = null;

    public Server()
    {
        timer.Update();
    }

    public boolean start()
    {
        //Command registrieren
        Main.getInstance().console.register(serverCommand);
        if(isRunning.get())
        {
            logger.error("Server läuft bereits");
            return false;
        }
        if(!serversocket.open())
        {
            logger.error("Ports konnten nicht gebunden werden. Läuft bereits ?");
            return false;
        }
        lobby = new ServerLobby();
        isRunning.set(true);

        //runningGame = new ServerGame();
        //runningGame.init();

        runThread = new Thread(this);
        runThread.start();

        return true;
    }

    public void stop()
    {
        if(isRunning.get())
        {
            isRunning.set(false);
            try {
                runThread.join();
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
            }
            //logger.info("try close serversocket");
            serversocket.close();
            if(runningGame != null)
            {
                runningGame = null;
            }
            //logger.info("closed serversocket");
        }
    }

    public void RunFromMain()
    {
        if(ToStartServer.get())
        {

            //Main.getInstance().getCurrentState().

            runningGame = new ServerGame();
            runningGame.init(lobby.mapId);
            runningGame.update(0);

            listToAddInGame = lobby.connectedClients;
            lobby.SendStartGame();

            lobby.remove();
            lobby = null;

            ToStartServer.set(false);
            loadfinisched.set(true);
        }
    }

    public void run()
    {
        while(isRunning.get())
        {
            Tools.Sleep(1);
            timer.Update();
            if(lobby!=null)
            {
                if(!lobby.update((float) timer.get_FrameSeconds()))
                {
                    startGameThreadsave();
                }
            }
            else
            {
                Iterator<LobyClient> it = listToAddInGame.iterator();
                while(it.hasNext())
                {
                    LobyClient client = it.next();
                    while(client.socket.isPacketAvaliable())
                    {
                        Packet pack = client.socket.getReceivedPacket();
                        if(pack.getPacketId() == PacketIds.Simple.getValue())
                        {
                            SimplePacket spacket = (SimplePacket) pack;
                            if(spacket.m_SimplePacketId == SimplePacket.SimplePacketId.StartGame.getValue())
                            {//client is ready to join game
                                it.remove();
                                runningGame.InsertPlayerInGame(client.socket,client.name,client.Team1);
                                break;
                            }
                        }
                    }
                }
                if(!runningGame.update((float) timer.get_FrameSeconds()))
                {
                    logger.info("Game stoped timeout");
                    stopGame();
                }
            }


            //runningGame.update(0);
            //System.out.println("runn");
            //engine.update();

            if(serversocket.isNewClientAvaliable())
            {
                if(serversocket.isNewClientAvaliable())
                {
                    Serverclientsocket sock = serversocket.getNewClient();
                    if(runningGame!=null)
                    {
                        logger.info("Insert player to game");
                        sock.sendPacket(new SimplePacket(SimplePacket.SimplePacketId.StartGame.getValue(), 0));
                        listToAddInGame.push(new LobyClient(sock));
                    }
                    else
                    {
                        logger.info("insert player to lobby");
                        if((InsertInLobby(sock))) {
                            clientSockets.push(sock);
                        }
                    }
                }
            }
        }
    }

    public boolean InsertInLobby(Serverclientsocket sock)
    {
        if(!lobby.InserNewPlayer(sock))
        {
            sock.sendPacket(new SimplePacket(SimplePacket.SimplePacketId.ConnectInitPacket.getValue(),-3));
        }
        else
        {
            sock.sendPacket(new SimplePacket(SimplePacket.SimplePacketId.ConnectInitPacket.getValue(),1));
            return true;
        }
        return false;
    }

    public Serversocket getServersocket(){
        return serversocket;
    }

    public void startGameThreadsave()
    {
        ToStartServer.set(true);
        while(loadfinisched.get() == false)
        {
            Tools.Sleep(1);
        }
    }

    public void startGame(){
        if(runningGame!=null)
        {
            logger.info("Spiel läuft bereits");
            return;
        }

        runningGame = new ServerGame();
        runningGame.init(lobby.mapId);
        runningGame.update(0);

        listToAddInGame = lobby.connectedClients;
        lobby.SendStartGame();

        lobby.remove();
        lobby = null;
    }

    public void stopGame(){
        if(runningGame == null)
        {
            logger.info("Kein laufendes spiel vorhanden");
            return;
        }
        else
        {
            runningGame.remove();
            runningGame = null;
            Tools.Sleep(500);
            lobby = new ServerLobby();
            lobby.init();
            SimplePacket pack = new SimplePacket(SimplePacket.SimplePacketId.StopGame.getValue(),0);
            for(Serverclientsocket sock : clientSockets)
            {
                sock.sendPacketSave(pack);
            }

            Iterator<Serverclientsocket> it = clientSockets.iterator();
            while(it.hasNext())
            {
                Serverclientsocket sock=it.next();
                if(!sock.isConnected())
                {
                    it.remove();
                }
                else {
                    sock.sendPacketSave(pack);
                }
            }

            Tools.Sleep(500);

            it = clientSockets.iterator();
            while(it.hasNext())
            {
                Serverclientsocket sock=it.next();
                if(InsertInLobby(sock))
                {
                    it.remove();
                }
            }
        }
    }


}
