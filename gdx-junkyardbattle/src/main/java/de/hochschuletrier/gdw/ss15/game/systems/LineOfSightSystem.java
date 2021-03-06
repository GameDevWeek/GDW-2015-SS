package de.hochschuletrier.gdw.ss15.game.systems;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.game.components.LineOfSightComponent;

public class LineOfSightSystem extends EntitySystem {

    // Beispiel:
    // wenn Player 1 aus Team 1 die LineOfSightComponent hat,
    // heißt das, dass Team 2 ihn sehen kann
    
    private ArrayList<Entity> team1, team2; // Listen müssen noch aus dem Netcode hinzugefügt werden
    
    private PhysixSystem physixSystem;
    
    public LineOfSightSystem(PhysixSystem physixSystem) {
        this.physixSystem = physixSystem;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if(team1 == null || team2 == null || team1.isEmpty() || team2.isEmpty()) // wenn Listen leer sind, wird nichts gemacht
            return;
        
        for(int j=0; j < team2.size(); ++j) // erster Durchlauf bei dem LineOfSight des 2. Teams
                                            // aus Sicht des ersten Mitglieds aus Team 1 gecheckt und gesetzt wird
        {
            if(checkLineOfSight(team1.get(0), team2.get(j)))
            {
                // component angefügt ==> ist sichtbar (für anderes Team)
                if(team1.get(0).getComponent(LineOfSightComponent.class) == null)
                    team1.get(0).add(new LineOfSightComponent());
                if(team2.get(j).getComponent(LineOfSightComponent.class) == null)
                    team2.get(j).add(new LineOfSightComponent());
            }
            else
            {
                if(team1.get(0).getComponent(LineOfSightComponent.class) != null)
                    team1.get(0).remove(LineOfSightComponent.class);
                if(team2.get(j).getComponent(LineOfSightComponent.class) != null)
                    team2.get(j).remove(LineOfSightComponent.class);
            }
        }

        // restliches Team wird durchlaufen - Spieler aus Team 2 die schon sichtbar sind werden übersprungen (Optimierung)
        
        for(int i=1; i < team1.size(); ++i)
        {
            for(int j=0; j < team2.size(); ++j)
            {
                if (team2.get(j).getComponent(LineOfSightComponent.class) != null)
                {
                    continue;
                }
                if(checkLineOfSight(team1.get(i), team2.get(j)))
                {
                    team1.get(i).add(new LineOfSightComponent());
                    team2.get(j).add(new LineOfSightComponent());
                }
            }
        }
    }
    
    private boolean checkLineOfSight(Entity from, Entity to)
    {
        // checkt ob von einer Entity zur anderen Entity Sichtkontakt besteht
        
        AtomicBoolean successful = new AtomicBoolean(false);
        
        RayCastCallback lineOfSightCallback = new RayCastCallback() {
            
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point,
                    Vector2 normal, float fraction) {
                if(fixture.getBody().getUserData() instanceof PhysixBodyComponent)
                {
                    PhysixBodyComponent physixBodyComponent = (PhysixBodyComponent)fixture.getBody().getUserData();
                    successful.set(physixBodyComponent.getEntity().equals(to));
                }
                else
                {
                    successful.set(false);
                }
                return fraction;
            }
        };
        
        physixSystem.getWorld().rayCast(lineOfSightCallback,
                from.getComponent(PhysixBodyComponent.class).getPosition(),
                to.getComponent(PhysixBodyComponent.class).getPosition());
        
        return successful.get();
    }
}
