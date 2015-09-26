package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class BulletSystem extends IteratingSystem{

    public static final float lifetime = 0.3f; // seconds

    private final PooledEngine engine;
    private final ServerGame serverGame;

    public BulletSystem(PooledEngine engine, ServerGame serverGame) {
        super(Family.all(BulletComponent.class).get());
        this.engine = engine;
        this.serverGame = serverGame;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        BulletComponent bullet = ComponentMappers.bullet.get(entity);
        bullet.traveltime += deltaTime;
        if(bullet.traveltime > lifetime)//Bullet quasi stehengeblieben
        {
//            System.out.println("bullet got to slow");
            if(ComponentMappers.abyss.get(entity).above <= 0)
                serverGame.createEntity("metalServer", physix.getPosition().x, physix.getPosition().y);
            engine.removeEntity(entity);
        }
    }
}
