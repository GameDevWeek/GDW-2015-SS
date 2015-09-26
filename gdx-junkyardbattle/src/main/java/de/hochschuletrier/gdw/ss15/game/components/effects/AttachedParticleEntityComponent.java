package de.hochschuletrier.gdw.ss15.game.components.effects;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class AttachedParticleEntityComponent extends Component implements Pool.Poolable {
    public Entity entity;
    
    @Override
    public void reset() {
        entity = null;
    }
    
}
