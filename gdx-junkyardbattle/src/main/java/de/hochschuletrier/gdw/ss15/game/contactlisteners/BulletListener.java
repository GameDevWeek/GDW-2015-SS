package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.CollisionEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;

public class BulletListener extends PhysixContactAdapter{

//	private PooledEngine engine;
//	
//	public BulletListener(PooledEngine engine){
//		this.engine = engine;
//	}
	
	//Wird aufgerufen, wenn eine Bullet/Spielerschuss gegen ein Objekt trifft
	//TO DO wo wird differenziert, was getroffen wurde. Hier oder im WeaponSystem?
    @Override
	public void beginContact(PhysixContact contact) {
		//Kontakt weiterreichen an WeaponSystem
		//TO DO Entity in WeaponSystem oder hier löschen?
        BulletComponent otherBulletComponent = ComponentMappers.bullet.get(contact.getOtherComponent().getEntity());
        if(otherBulletComponent != null)
        {
            contact.setEnabled(false);
        }
        else
        {
            CollisionEvent.emit(contact);            
        }
	}
	
	@Override
	public void preSolve(PhysixContact contact, Manifold oldManifold)
	{
	    BulletComponent otherBulletComponent = ComponentMappers.bullet.get(contact.getOtherComponent().getEntity());
	    if(otherBulletComponent != null)
	    {
	        contact.setEnabled(false);
	    }
    }
}
