package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.DeathComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;

/**
 * Handles health of entities. If the health drops to 0 the entity will die.
 * 
 * @author compie
 *
 */
public class HealthSystem extends IteratingSystem {
    private final PooledEngine engine;
    
    @SuppressWarnings("unchecked")
    public HealthSystem(PooledEngine engine) {
        super(Family.all(HealthComponent.class).get());
        
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComp = ComponentMappers.health.get(entity);
        if(healthComp.health <= 0 && !ComponentMappers.death.has(entity)) {
            DeathComponent deathComp = engine.createComponent(DeathComponent.class);
            deathComp.dyingDuration = GameConstants.DYING_TIMER;
            entity.add(deathComp);
        }
    }
}
