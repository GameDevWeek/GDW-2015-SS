package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.PlayerHurtEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class PlayerHurtSystem extends EntitySystem implements PlayerHurtEvent.Listener {

    ComponentMapper<HealthComponent> health = ComponentMappers.health;
    ComponentMapper<WeaponComponent> weapon = ComponentMappers.weapon;
    public PlayerHurtSystem()
    {
        PlayerHurtEvent.register(this);
    }

    @Override
    public void onPlayerHurt(Entity shootingPlayer, Entity hurtPlayer) {
        health.get(hurtPlayer).health -= weapon.get(shootingPlayer).ShardsPerShot;

    }
}
