package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactListener;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;

public class PickupListener implements PhysixContactListener{

	private PooledEngine engine;
	
	public PickupListener(PooledEngine engine) {
		this.engine = engine;
	}
	@Override
	public void beginContact(PhysixContact contact) {
		InventoryComponent inv = contact.getOtherComponent().getEntity().getComponent(InventoryComponent.class);
		if(inv != null)
		{

			//Pickup nur aufheben, wenn die maximale Shards Anzahl nicht überschritten wird
			if(inv.setMetalShards(inv.getMetalShards()+1))
			{
				//Pickup Sounds etc. hier hinzufügen
				engine.removeEntity(contact.getMyComponent().getEntity());
			}
			else//Kollision zwischen Player und Pickup ausgeschaltet-Player können durchlaufen
				contact.setEnabled(false);
		}
	}

	@Override
	public void endContact(PhysixContact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(PhysixContact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(PhysixContact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
