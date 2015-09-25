package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactListener;

public class PickupListenerClient implements PhysixContactListener {

	@Override
	public void beginContact(PhysixContact contact) {
		contact.setEnabled(false);

	}

	@Override
	public void endContact(PhysixContact contact) {
		contact.setEnabled(false);

	}

	@Override
	public void preSolve(PhysixContact contact, Manifold oldManifold) {
		contact.setEnabled(false);

	}

	@Override
	public void postSolve(PhysixContact contact, ContactImpulse impulse) {
		contact.setEnabled(false);

	}

}
