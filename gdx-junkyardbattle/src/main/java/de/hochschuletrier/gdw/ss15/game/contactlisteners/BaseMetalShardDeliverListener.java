package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ss15.events.ComeToBaseEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;

public class BaseMetalShardDeliverListener extends PhysixContactAdapter {

    @Override
    public void beginContact(PhysixContact contact)
    {
        if(contact.getOtherComponent() != null)
        {
            if (ComponentMappers.player.has(contact.getOtherComponent().getEntity()))
            {
                System.out.println("player hit base");
                // "otherEntity" - Player, "myEntity" - Base
                ComeToBaseEvent.emit(contact.getOtherComponent().getEntity(), contact.getMyComponent().getEntity());
            }
        }
    }
}
