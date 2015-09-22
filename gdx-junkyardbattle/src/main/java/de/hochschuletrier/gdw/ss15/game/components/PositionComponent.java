package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent extends Component implements Pool.Poolable {

    public float x;
    public float y;
    public int layer = 0;
    public float rotation;

    @Override
    public void reset() {
        rotation = x = y = layer = 0;
    }
}
