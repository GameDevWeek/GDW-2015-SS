package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by hherm on 22/09/2015.
 */
public class NetworkIDComponent extends Component implements Pool.Poolable {

    public long networkID;

    @Override
    public void reset() {
        networkID = 0;
    }
}
