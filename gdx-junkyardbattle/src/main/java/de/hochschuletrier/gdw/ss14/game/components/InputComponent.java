package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss14.game.input.InputAction;

import java.util.LinkedList;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputComponent extends Component implements Pool.Poolable {

    public LinkedList<InputAction> actions;

    @Override
    public void reset() {
        actions.clear();
    }
}
