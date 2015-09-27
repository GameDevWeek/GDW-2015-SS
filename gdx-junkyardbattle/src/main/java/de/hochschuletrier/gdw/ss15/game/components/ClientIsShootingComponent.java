package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by lukas on 27.09.15.
 */
public class ClientIsShootingComponent extends Component implements Pool.Poolable{

   public boolean shooted;
   public boolean onGather;
   public float Gathertime;

    @Override
    public void reset() {
        Gathertime = 0;
        onGather = false;
        shooted = false;
        Gathertime = 0;
    }
}
