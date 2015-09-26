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
    public float rotation;
    public float playerrotation;
    public final Vector2 playerpos = new Vector2();
    public int power;

    public float traveltime = 0f;

    @Override
    public void reset()
    {
        playerID = -1;
        traveltime = 0f;
        power = 0;
        playerrotation = 0;
        rotation = 0;
        playerpos.set(Vector2.Zero);
    }
}
