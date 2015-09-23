package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 23.09.2015.
 */
public class MoveComponentFactory extends ComponentFactory<EntityFactoryParam> {


    @Override
    public String getType() {
        return "Move";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        MoveComponent component = engine.createComponent(MoveComponent.class);
        component.speed = properties.getFloat("speed");
        component.velocity = new Vector2(0,0);
        entity.add(component);

    }
}

