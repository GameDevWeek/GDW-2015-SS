package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;
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

    ServerGame runningGame = new ServerGame(serversocket);
    MyTimer timer = new MyTimer();

    public Server()
    {
        timer.Update();
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
            Tools.Sleep(10);
            timer.Update();
            runningGame.update((float)timer.get_FrameSeconds());
            //System.out.println("runn");
            //engine.update();
        }
    }
}
