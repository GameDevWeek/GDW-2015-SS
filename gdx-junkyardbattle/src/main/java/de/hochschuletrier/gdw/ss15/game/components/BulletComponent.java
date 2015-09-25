package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**e
 * playerID: The ID of the play who has shot the bullet
 */
public class BulletComponent extends Component implements Poolable
{
    public int playerID;
    public Vector2 startpos;

    @Override
    public void reset()
    {
        playerID = -1;
    }
}
