package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.Serverclientsocket;

public class ClientComponent extends Component implements Pool.Poolable {

    public Serverclientsocket client;

    public void reset(){
        client = null;
    }

}
