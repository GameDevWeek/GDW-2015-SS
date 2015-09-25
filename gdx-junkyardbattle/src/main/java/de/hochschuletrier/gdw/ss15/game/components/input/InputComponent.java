package de.hochschuletrier.gdw.ss15.game.components.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputComponent extends Component implements Pool.Poolable {

    public float horizontal; //Richtung horizontal, Range [-1, 1]: -1 links 0 neutral 1 rechts
    public float vertical; //Richtung vertical, Range [-1, 1]: -1 hoch 0 neutral 1 runter

    public boolean shoot; //Schusstaste (linke Maustaste)
    public boolean gather; //Gathertaste (rechte Maustaste)
    public boolean escape; //Escapetaste (Menu, Pause, whatever ...)

    //Mausposition für evtl Fadenkreuz
    public int posX;
    public int posY;

    public double rightStickAngle;
    public boolean isController = false; //ob Controller aktiv ist

    @Override
    public void reset()
    {
        shoot = gather = isController = false;
        posX = posY = 0;
        horizontal = vertical = 0.0f;
        rightStickAngle = 0.0;
    }
}
