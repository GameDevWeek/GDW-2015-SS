package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import de.hochschuletrier.gdw.commons.utils.pool.Poolable;

/**
 * Created by Ricardo on 24.09.2015.
 */
public class BasePointComponent extends Component implements Poolable {

    public int teamID;
    public int points;
    @Override
    public void reset() {
        teamID = -1;
    }
}
