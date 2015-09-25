package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.LobyClient;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.ServerLobby;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.server.ServerCloneException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


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
            }else if(info.equals("lobby")){
                String info2 = list.get(2);
                if(info2.equals("kickPlayer")){
                    logger.info("Spieler "+list.get(3)+" wird gekickt >:)");
                    kickPlayer(list.get(3));
                }else if(info2.equals("changeMap")){
                    logger.info("Map wird geaendert zu "+list.get(3));
                    changeMap(list.get(3));
                }else{
                    logger.error(info2+" falscher Parameter fuer command serverCommmand lobby");
                }
                //lobby kickplayer changemap
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

    public Serverclientsocket LastConnectedClient = null;

    LinkedList<Serverclientsocket> clientSockets = new LinkedList<>();

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
        lobby.init();
        isRunning.set(true);
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
                    startGame();
                }
            }
            else
            {
               runningGame.update((float) timer.get_FrameSeconds());
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
                        runningGame.InsertPlayerInGame(sock,"test",true);
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

    public void startGame(){
        if(runningGame!=null)
        {
            logger.info("Spiel läuft bereits");
            return;
        }

        lobby.SendStartGame();

        Tools.Sleep(1);//all player initializie game mot verg good XD

        runningGame = new ServerGame();
        runningGame.init(Main.getInstance().getAssetManager());
        runningGame.update(0);

        for(LobyClient client : lobby.connectedClients)
        {
            runningGame.InsertPlayerInGame(client.socket,client.name,client.Team1);
        }
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

    public void kickPlayer(String name){

    }

    public void changeMap(String map){

    }

}
