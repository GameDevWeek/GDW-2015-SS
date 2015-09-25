package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ss15.events.PickupEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;

public class MetalShardSpawnListener extends PhysixContactAdapter{
    
    @Override
    public void beginContact(PhysixContact contact) {
        //Spawn kollidiert mit einem neuem Objekt mehr
        ComponentMappers.metalShardSpawn.get(contact.getMyComponent().getEntity()).collidingObjects++;
    }

    @Override
    public void endContact(PhysixContact contact) {
        //Spwn kollidiert mit einem alten Objekt weniger
        ComponentMappers.metalShardSpawn.get(contact.getMyComponent().getEntity()).collidingObjects--;
    }

}
