package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.ss15.events.PickupEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 24.09.2015.
 */
public class PickupSystem extends EntitySystem implements PickupEvent.Listener {

    ComponentMapper<InventoryComponent> inventory;
    ComponentMapper<PlayerComponent> player;
    public PickupSystem()
    {
        inventory = ComponentMappers.inventory;
        player = ComponentMappers.player;
        PickupEvent.register(this);
    }



    @Override
    public void onPickupEvent(PhysixContact physixContact) {
        if (ComponentMappers.player.has(physixContact.getOtherComponent().getEntity())) {

            InventoryComponent invPickup = inventory.get(physixContact.getMyComponent().getEntity());
            InventoryComponent invPlayer = inventory.get(physixContact.getOtherComponent().getEntity());

            //invPlayer.set


        }
    }
}
