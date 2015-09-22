package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss15.game.input.InputMovPaket;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputComponent extends Component implements Pool.Poolable {

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public int posX;
    public int posY;

    public void update(InputMovPaket inputMovPaket) {
        this.up = inputMovPaket.up;
        this.down = inputMovPaket.down;
        this.left = inputMovPaket.left;
        this.right = inputMovPaket.right;

        this.posX = inputMovPaket.posX;
        this.posY = inputMovPaket.posY;
    }

    @Override
    public void reset()
    {
        up = down = left = right = false;
        posX = posY = 0;
    }
}
