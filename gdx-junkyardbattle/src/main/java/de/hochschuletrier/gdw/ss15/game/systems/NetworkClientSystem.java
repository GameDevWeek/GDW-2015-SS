package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Clientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by lukas on 21.09.15.
 */
public class NetworkClientSystem extends EntitySystem implements SocketConnectListener {


    public void loginFinished(ConnectStatus status)
    {
    }


    public NetworkClientSystem(int priority) {
        super(priority);
    }



    @Override
    public void update(float deltaTime){
    }

    public void dispose(){
    }

}

