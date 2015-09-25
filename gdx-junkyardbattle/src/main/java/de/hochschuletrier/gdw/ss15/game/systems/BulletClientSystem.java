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

public class BulletClientSystem extends IteratingSystem{

    private static final float maxrange = 500;

    private final PooledEngine engine;

    public BulletClientSystem(PooledEngine engine) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class, BulletComponent.class).get());
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        BulletComponent bullet = ComponentMappers.bullet.get(entity);
        Vector2 dst = new Vector2(physix.getPosition().x, physix.getPosition().y);
        dst.sub(bullet.startpos);
        if(dst.len2() > maxrange*maxrange)//Bullet quasi stehengeblieben
        {
            engine.removeEntity(entity);
        }
    }
}
