package de.hochschuletrier.gdw.ss15.game.components.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputComponent extends Component implements Pool.Poolable {

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public boolean shoot;
    public boolean gather;

    public int posX;
    public int posY;

    @Override
    public void reset()
    {
        up = down = left = right =shoot = gather = false;
        posX = posY = 0;
    }
}
