package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.PlayerHurtEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.AboveAbyssComponent;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

public class BulletListener extends PhysixContactAdapter{

    private PooledEngine engine;

    public BulletListener(PooledEngine engine){
        this.engine = engine;
    }

    //Wird aufgerufen, wenn eine Bullet/Spielerschuss gegen ein Objekt trifft
    //TO DO wo wird differenziert, was getroffen wurde. Hier oder im WeaponSystem?
    @Override
    public void beginContact(PhysixContact contact) {
        //Kontakt weiterreichen an WeaponSystem
        //TO DO Entity in WeaponSystem oder hier löschen?
    	
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        if (otherComponent != null) {
            Entity otherEntity = otherComponent.getEntity();
            BulletComponent otherBulletComponent = ComponentMappers.bullet.get(otherEntity);
            if(otherBulletComponent != null)
            {
                // kollidiert mit Bullet
                // Kollision nicht beachten
                contact.setEnabled(false);
            }
            else
            {
                PlayerComponent otherPlayerComponent = ComponentMappers.player.get(otherEntity);
                if(otherPlayerComponent != null)
                {
                    // kollidiert mit anderem Spieler
                    PlayerHurtEvent.emit(contact.getMyComponent().getEntity(), otherEntity);
//                    engine.removeEntity(contact.getMyComponent().getEntity()); // uebernimmt GameLogik
                }
                if(ComponentMappers.abyss.has(otherEntity)){//Satellit und Autowracks
                    engine.removeEntity(contact.getMyComponent().getEntity());
                }
                                
            }
        }
        else
        {
            if(contact.getOtherFixture() != null){
                if(contact.getOtherFixture().getBody().getFixtureList().get(0).getUserData() instanceof AboveAbyssComponent)
                  {
                    ComponentMappers.abyss.get(contact.getMyComponent().getEntity()).above++;
                    contact.setEnabled(false);
                  }
                else
                      engine.removeEntity(contact.getMyComponent().getEntity());
                
            }
            
        }
    }
    
    @Override
    public void endContact(PhysixContact contact) {
        if(contact.getOtherFixture() != null){
            if(contact.getOtherFixture().getBody().getFixtureList().get(0).getUserData() instanceof AboveAbyssComponent)
              {
                ComponentMappers.abyss.get(contact.getMyComponent().getEntity()).above--;
                contact.setEnabled(false);
              }            
        }
    }

    @Override
    public void preSolve(PhysixContact contact, Manifold oldManifold)
    {
    	if(contact.getOtherFixture().getFilterData().categoryBits == (short)0x0001){
    		contact.setEnabled(false);
    		return;
    	}
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        if (otherComponent != null) {
            BulletComponent otherBulletComponent = ComponentMappers.bullet.get(contact.getOtherComponent().getEntity());
            if(otherBulletComponent != null)
            {
                contact.setEnabled(false);
            }
        }
    }
}
