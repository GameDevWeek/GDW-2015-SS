package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss15.events.InputEvent;

import java.util.LinkedList;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputComponent extends Component implements Pool.Poolable {

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    @Override
    public void reset()
    {
        up = down = left = right = false;
    }
}
