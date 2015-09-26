package de.hochschuletrier.gdw.ss15.game.components.effects;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ChargeEffectComponent extends Component implements Pool.Poolable {
    public float stateTime = 0.f;
    
    @Override
    public void reset() {
        stateTime = 0.f;
    }
}
