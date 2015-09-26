package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;

public class PlayerBaseListener extends PhysixContactAdapter {
    
    @Override
    public void beginContact(PhysixContact contact)
    {
        if(contact.getOtherComponent() != null)
        {
            if(ComponentMappers.basePoint.get(contact.getOtherComponent().getEntity()) != null)
            {
                // "myEntity" - Player, "otherEntity" - Base
//                ComeToBaseEvent.emit(contact.getMyComponent().getEntity(), contact.getMyComponent().getEntity());
            }
        }
    }
}
