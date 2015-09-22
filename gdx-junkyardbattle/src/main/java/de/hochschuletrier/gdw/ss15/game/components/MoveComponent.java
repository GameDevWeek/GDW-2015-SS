package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import java.lang.Math.*;

/**
 * Created by Ricardo on 21.09.2015.
 */
public class MoveComponent extends Component implements Pool.Poolable{


    public Vector2 velocity = new Vector2(0,0);
    public float speed = 0;
    public double SQRT2 = Math.sqrt(2.0);

    @Override
    public void reset() {
        speed = 0;
        velocity.setZero();
    }
}
