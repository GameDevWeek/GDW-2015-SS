package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Ricardo on 21.09.2015.
 */
public class MoveComponent extends Component implements Pool.Poolable{

    public float velocityX = 0;
    public float velocityY = 0;

    @Override
    public void reset() {
        velocityX = velocityY = 0;
    }
}
