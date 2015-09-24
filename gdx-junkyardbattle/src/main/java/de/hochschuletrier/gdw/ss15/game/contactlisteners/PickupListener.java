package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactListener;
import de.hochschuletrier.gdw.ss15.events.PickupEvent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;

public class PickupListener implements PhysixContactListener{

	private PooledEngine engine;
	
	public PickupListener(PooledEngine engine) {
		this.engine = engine;
	}
	@Override
	public void beginContact(PhysixContact contact) {
		if (contact.getOtherComponent() != null)
			PickupEvent.emit(contact);
	}

	@Override
	public void endContact(PhysixContact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(PhysixContact contact, Manifold oldManifold) {
		contact.setEnabled(false);
		
	}

	@Override
	public void postSolve(PhysixContact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
