package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 22.09.2015.
 */
public class InputMoveSystem extends EntitySystem {

    private boolean leftPressed, rightPressed, upPressed, downPressed;
    private ImmutableArray<Entity> playerEntities;
    private Family playerFamily;
    private ComponentMapper<MoveComponent> move;
    private ComponentMapper<PlayerComponent> player;
    private Vector2 vectorToAdd;

    public InputMoveSystem(Engine engine)
    {
        leftPressed = rightPressed = upPressed = downPressed = false;
        playerFamily = Family.all(PlayerComponent.class, MoveComponent.class).get();
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
        if (leftPressed) vectorToAdd.add(-1, 0);
        if (rightPressed) vectorToAdd.add(1, 0);
        if (upPressed) vectorToAdd.add(0, 1);
        if (downPressed) vectorToAdd.add(0, -1);
        vectorToAdd.nor();
        vectorToAdd.scl(move.get(e).speed);
        move.get(e).velocity.set(vectorToAdd);
    }


}
