package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.ss15.events.PickupEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 * Created by Ricardo on 24.09.2015.
 */
public class PickupSystem extends IntervalIteratingSystem implements PickupEvent.Listener {

    ComponentMapper<InventoryComponent> inventory;
    ComponentMapper<PlayerComponent> player;
    Engine engine;
    public PickupSystem(Engine engine)
    {
        super(Family.all(PlayerComponent.class).get(), 10);
        inventory = ComponentMappers.inventory;
        player = ComponentMappers.player;
        PickupEvent.register(this);
        this.engine = engine;
    }



    @Override
    public void onPickupEvent(PhysixContact physixContact) {
         if (player.has(physixContact.getOtherComponent().getEntity())) {

            Entity pickup = physixContact.getMyComponent().getEntity();
            Entity player = physixContact.getOtherComponent().getEntity();
            InventoryComponent invPickup = inventory.get(pickup);
            InventoryComponent invPlayer = inventory.get(player);

             if (invPlayer.addMetalShards(invPickup.getMetalShards()) > 0)
             {
                 //System.out.println("Pickup removed");
                 this.engine.removeEntity(pickup);

             }
             //System.out.println("InventarPlayer: " + invPlayer.getMetalShards());



        }
    }

    @Override
    protected void processEntity(Entity entity) {

    }

    @Override
    protected void updateInterval() {

    }
}
