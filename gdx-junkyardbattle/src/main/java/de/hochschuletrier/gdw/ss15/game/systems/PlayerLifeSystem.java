package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class PlayerLifeSystem extends IteratingSystem {

    public PlayerLifeSystem(Family family) {
        super(Family.all(HealthComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent hCom = ComponentMappers.health.get(entity);
        InventoryComponent inventory = ComponentMappers.inventory.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        if(! ComponentMappers.player.has(entity)) return;
        if (hCom.healthState == HealthComponent.HealthState.ALIVE) {
            if (hCom.health <= 0) {
                hCom.healthState = HealthComponent.HealthState.DYING;
                hCom.dyingTimer -= deltaTime;
            }
            if (hCom.spawningTimer > 0)
            {
                hCom.spawningTimer -= deltaTime;
            }
        }
        if (hCom.healthState == HealthComponent.HealthState.DYING)
        {
            if (hCom.dyingTimer <= 0)
            {
                hCom.healthState = HealthComponent.HealthState.DEAD;
                inventory.setMetalShards(0);
                hCom.deathTimer -= deltaTime;
                hCom.dyingTimer = GameConstants.DYING_TIMER;

            }

        }
        if (hCom.healthState == HealthComponent.HealthState.DEAD)
        {
            if (hCom.deathTimer <= 0)
            {
                hCom.health = GameConstants.START_HEALTH;
                hCom.deathTimer = GameConstants.DEATH_TIMER;
                hCom.healthState = HealthComponent.HealthState.ALIVE;
                hCom.spawningTimer = GameConstants.SPAWNING_DURATION;
                position.x = GameConstants.START_X_POSITION;
                position.y = GameConstants.START_Y_POSITION;
            }
        }

    }
}
