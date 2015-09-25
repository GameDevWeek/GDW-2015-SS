package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.ss15.events.ComeToBaseEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BasePointComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 24.09.2015.
 */
public class BringHomeSystem extends EntitySystem implements ComeToBaseEvent.listener {

    ComponentMapper<InventoryComponent> inventory = ComponentMappers.inventory;
    ComponentMapper<BasePointComponent> basePoint = ComponentMappers.basePoint;
    ComponentMapper<PlayerComponent> player = ComponentMappers.player;

    public void onComeToBase(PhysixContact contact) {
        if (player.has(contact.getOtherComponent().getEntity())) {
            Entity playerEntity = contact.getOtherComponent().getEntity();
            Entity basePointEntity = contact.getMyComponent().getEntity();

            basePoint.get(basePointEntity).points += inventory.get(playerEntity).getMetalShards();
            inventory.get(playerEntity).setMetalShards(0);
        }
    }

}
