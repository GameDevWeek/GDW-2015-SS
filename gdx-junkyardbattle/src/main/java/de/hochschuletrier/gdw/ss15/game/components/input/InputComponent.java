package de.hochschuletrier.gdw.ss15.game.components.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputComponent extends Component implements Pool.Poolable {

    public float horizontal;
    public float vertical;

    public boolean shoot;
    public boolean gather;

    public int posX;
    public int posY;

    public double rightStickAngle;
    public boolean isController = false;

    @Override
    public void reset()
    {
        shoot = gather = isController = false;
        posX = posY = 0;
        horizontal = vertical = 0.0f;
        rightStickAngle = 0.0;
    }
}
