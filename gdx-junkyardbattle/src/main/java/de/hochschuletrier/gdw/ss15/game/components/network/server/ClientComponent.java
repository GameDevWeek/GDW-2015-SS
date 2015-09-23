package de.hochschuletrier.gdw.ss15.game.components.network.server;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;

public class ClientComponent extends Component implements Pool.Poolable {

    public Serverclientsocket client;

    public void reset(){
        client = null;
    }

}
