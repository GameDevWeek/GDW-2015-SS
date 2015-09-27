package de.hochschuletrier.gdw.ss15.game.components.effects;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MagneticBeamEffectComponent extends Component implements Pool.Poolable {
    public float gatherStateTime = 0.f;
    
    @Override
    public void reset() {
        gatherStateTime = 0.f;
    }

}
