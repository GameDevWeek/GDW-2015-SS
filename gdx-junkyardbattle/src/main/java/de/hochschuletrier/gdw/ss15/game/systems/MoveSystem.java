package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 22.09.2015.
 */
public class MoveSystem extends EntitySystem {

    private boolean leftPressed, rightPressed, upPressed, downPressed;
    private ImmutableArray<Entity> playerEntities;
    private Family playerFamily;
    private ComponentMapper<MoveComponent> move;
    private ComponentMapper<PlayerComponent> player;
    private ComponentMapper<InputComponent> input;
    private ComponentMapper<PhysixBodyComponent> physixBody;
    private Vector2 vectorToAdd;

    public MoveSystem(Engine engine)
    {
        move = ComponentMappers.move;
        player = ComponentMappers.player;
        input = ComponentMappers.input;
        physixBody = ComponentMappers.physixBody;
        playerFamily = Family.all(PhysixBodyComponent.class, PlayerComponent.class, MoveComponent.class, InputComponent.class).get();
        playerEntities = engine.getEntitiesFor(playerFamily);
        vectorToAdd = Vector2.Zero;
    }

    public void update(float deltaTime)
    {
        for (Entity e: playerEntities)
        {
            updateMoveVector(e);
        }
    }

    private void updateMoveVector(Entity e)
    {
        vectorToAdd.setZero();
        if (input.get(e).left) vectorToAdd.add(-1, 0);
        if (input.get(e).right) vectorToAdd.add(1, 0);
        if (input.get(e).up) vectorToAdd.add(0, 1);
        if (input.get(e).down) vectorToAdd.add(0, -1);
        vectorToAdd.nor();
        vectorToAdd.scl(move.get(e).speed);

        move.get(e).velocity.set(vectorToAdd);
        Vector2 v2 = new Vector2(100,100);
        physixBody.get(e).setLinearVelocity(v2);
    }


}
