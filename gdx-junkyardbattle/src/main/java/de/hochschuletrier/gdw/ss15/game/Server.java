package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                logger.info("Spiel wird gestartet.");
                startGame();
            }
            else if(info.equals("stopGame")){
                logger.info("Spiel wird gestoppt.");
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

    ServerGame runningGame = null;
    MyTimer timer = new MyTimer();

    LinkedList<Clientsocket> clientSockets = new LinkedList<>();

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
        //runningGame = new ServerGame();
       // runningGame.init(Main.getInstance().getAssetManager());
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
            Tools.Sleep(10);
            timer.Update();
            runningGame.update((float)timer.get_FrameSeconds());
            //runningGame.update(0);
            //System.out.println("runn");
            //engine.update();

            if(serversocket.isNewClientAvaliable())
            {
                if(serversocket.isNewClientAvaliable())
                {
                    Serverclientsocket sockret = serversocket.getNewClient();
                    if(runningGame != null)
                    {
                        SimplePacket packet = new SimplePacket(SimplePacket.SimplePacketId.ConnectInitPacket.getValue(),-1);
                    }


                }
            }


        }
    }

    public Serversocket getServersocket(){
        return serversocket;
    }

    public void startGame(){

    }

    public void stopGame(){

    }

    public void kickPlayer(String name){

    }

    public void changeMap(String map){

    }

}
