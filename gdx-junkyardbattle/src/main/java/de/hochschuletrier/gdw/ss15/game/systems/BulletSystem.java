package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class BulletSystem extends IteratingSystem{

	public BulletSystem() {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class, BulletComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
//        System.out.println("Bullet Pos " + position);
        if(physix.getLinearVelocity().len2() <= 0.0001f)//Bullet quasi stehengeblieben
        {
        	entity.remove(BulletComponent.class);
        	entity.add(new PickableComponent());
        }
    }
}
