package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class BulletSystem extends IteratingSystem{

    private final PooledEngine engine;
    private final ServerGame serverGame;

    public BulletSystem(PooledEngine engine, ServerGame serverGame) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class, BulletComponent.class).get());
        this.engine = engine;
        this.serverGame = serverGame;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        if(physix.getLinearVelocity().len2() <= 1f)//Bullet quasi stehengeblieben
        {
            serverGame.createEntity("metalServer", physix.getPosition().x, physix.getPosition().y);
            engine.removeEntity(entity);
        }
    }
}
