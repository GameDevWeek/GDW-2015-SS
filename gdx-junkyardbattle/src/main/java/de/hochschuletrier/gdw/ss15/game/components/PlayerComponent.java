package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Created by Ricardo on 21.09.2015.
 */

public class PlayerComponent extends Component implements Poolable
{
    public int playerID, teamID;

    @Override
    public void reset()
    {
        playerID = 0;
        teamID = 0;
    }

}
