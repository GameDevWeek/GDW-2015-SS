package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;

/**
 * Created by oliver on 23.09.15.
 */
public class RotationSystem extends IteratingSystem {

    Camera camera;

    public RotationSystem(Camera cam) {
        super(Family.all(PlayerComponent.class, PositionComponent.class, HealthComponent.class,
                PhysixBodyComponent.class).get());
        this.camera = cam;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent plc = ComponentMappers.player.get(entity);
        HealthComponent hc = ComponentMappers.health.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        PositionComponent posc = ComponentMappers.position.get(entity);
        PhysixBodyComponent physxc = ComponentMappers.physixBody.get(entity);

        if(! plc.isLocalPlayer) return;
        if(hc.healthState != HealthComponent.HealthState.ALIVE) return;

        Vector3 mousepos = camera.unproject(new Vector3(input.posX, input.posY, 0));

        float rotation = (float)Math.atan2(mousepos.y - posc.y,mousepos.x - posc.y);
        physxc.setAngle(rotation);
    }
}
