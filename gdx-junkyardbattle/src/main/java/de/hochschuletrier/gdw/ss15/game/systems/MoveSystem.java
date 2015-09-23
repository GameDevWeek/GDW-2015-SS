package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;

/**
 * Created by Ricardo on 22.09.2015.
 */
public class MoveSystem extends IteratingSystem {

    private boolean leftPressed, rightPressed, upPressed, downPressed;
    private ImmutableArray<Entity> playerEntities;
    private Family playerFamily;
    private ComponentMapper<MoveComponent> move;
    private ComponentMapper<PlayerComponent> player;
    private ComponentMapper<InputComponent> input;
    private ComponentMapper<PhysixBodyComponent> physixBody;
    private Vector2 vectorToAdd;

    public MoveSystem()
    {
        super(Family.all(PlayerComponent.class, MoveComponent.class, PhysixBodyComponent.class, InputComponent.class).get());
        move = ComponentMappers.move;
        player = ComponentMappers.player;
        input = ComponentMappers.input;
        physixBody = ComponentMappers.physixBody;
        //playerFamily = Family.all(PhysixBodyComponent.class, PlayerComponent.class, MoveComponent.class, InputComponent.class).get();

        vectorToAdd = Vector2.Zero;
    }


    @Override
    protected void processEntity(Entity e, float deltaTime) {

        vectorToAdd.setZero();
        vectorToAdd.add(input.get(e).horizontal, input.get(e).vertical);
        vectorToAdd.nor();
        vectorToAdd.scl(move.get(e).speed);

        physixBody.get(e).setLinearVelocity(vectorToAdd);

    }

    private void updateMoveVector(Entity e)
    {

        System.out.println("hallochen");
    }


}
