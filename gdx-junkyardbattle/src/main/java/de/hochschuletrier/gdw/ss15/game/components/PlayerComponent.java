package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Created by Ricardo on 21.09.2015.
 */

public class PlayerComponent extends Component implements Poolable
{
    public static int playerIDCounter = 0;
    public static int teamIDCounter = 0;
    public          int     playerID, teamID; //Wie soll Id im Netzwerk übergeben werden int oder String?
    public final    Vector2 spawningPosition = new Vector2();
    public          Entity  killer; //name of Enemy
    public          String  name; // name of player
    public          long    lastSequenceId; //network?
    public          boolean isLocalPlayer;

    @Override
    public void reset()
    {
        playerID = playerIDCounter++;
        teamID = 0;
        isLocalPlayer = false;
        lastSequenceId = 0;
        name = "";
        spawningPosition.setZero();
        killer = null;
    }

}
