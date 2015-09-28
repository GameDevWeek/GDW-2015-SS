package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Ricardo on 21.09.2015.
 */
public class MoveComponent extends Component implements Pool.Poolable{


    public Vector2 velocity;
    public float speed = 0;

    @Override
    public void reset() {
        speed = 0;
        velocity.setZero();
    }
}
