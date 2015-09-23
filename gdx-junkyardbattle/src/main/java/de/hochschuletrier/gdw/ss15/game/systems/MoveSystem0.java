package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 * Created by Ricardo on 21.09.2015.
 */
public class MoveSystem0 extends EntitySystem {

    private ImmutableArray<Entity> moveEntities;
    private ComponentMapper<PositionComponent> pos;
    private ComponentMapper<MoveComponent> move;

    public MoveSystem0(Engine engine)
    {

        Family moveFamily = Family.all(MoveComponent.class, PositionComponent.class).get();
        moveEntities = engine.getEntitiesFor(moveFamily);

        //moveEntities.set(this.engine.getEntitiesFor(moveFamily));
        pos = ComponentMappers.position;
        move = ComponentMappers.move;

    }

    public void update(float deltaTime)
    {
        for (Entity e : moveEntities)
        {
            pos.get(e).x += move.get(e).velocity.x * deltaTime;
            pos.get(e).y += move.get(e).velocity.y * deltaTime;
        }
    }
}
