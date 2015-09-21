package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Created by lukas on 21.09.15.
 */
public class NetworkServerSystem extends EntitySystem implements EntityListener {

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void entityRemoved(Entity entity) {
        //entities.remove(entity);
    }


    @Override
    public void entityAdded(Entity entity) {
        //entities.add(entity);
        //resort = true;
    }
}
