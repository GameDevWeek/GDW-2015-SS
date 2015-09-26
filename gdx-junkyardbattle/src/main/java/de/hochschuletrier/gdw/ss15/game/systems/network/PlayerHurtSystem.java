package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.PlayerHurtEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.HealthPacket;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class PlayerHurtSystem extends EntitySystem implements PlayerHurtEvent.Listener {

    ComponentMapper<HealthComponent> healthCom = ComponentMappers.health;
    ComponentMapper<DamageComponent> damageCom = ComponentMappers.damage;
    
    public PlayerHurtSystem()
    {
        PlayerHurtEvent.register(this);
    }

    @Override
    public void onPlayerHurt(Entity projectile, Entity hurtPlayer) {
        if (damageCom.get(projectile).damageToPlayer) {
            healthCom.get(hurtPlayer).health -= damageCom.get(projectile).damage;
            
            HealthPacket pack = new HealthPacket();
            //pack.health
            pack.id = ComponentMappers.positionSynch.get(hurtPlayer).networkID;
            pack.health = healthCom.get(hurtPlayer).health;
            
            SendPacketServerEvent.emit(pack, true);
        }
    }
}
