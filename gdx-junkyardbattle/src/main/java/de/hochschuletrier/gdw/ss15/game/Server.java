package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lukas on 21.09.15.
 */
public class Server implements Runnable
{
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    Thread runThread;

    Serversocket serversocket = new Serversocket(12345,true);

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    ServerGame runningGame = new ServerGame();

    public Server()
    {

    }



    public boolean start()
    {
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
        runningGame.init(Main.getInstance().getAssetManager());
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
            //logger.info("closed serversocket");
        }
    }

    public void run()
    {
        runningGame.createEntity("player", 10, 10);
        while(isRunning.get())
        {
            runningGame.update(0);
            //System.out.println("runn");
            //engine.update();
        }
    }
}
