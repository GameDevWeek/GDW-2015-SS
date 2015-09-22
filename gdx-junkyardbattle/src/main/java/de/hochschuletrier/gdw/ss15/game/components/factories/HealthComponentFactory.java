package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 22.09.2015.
 */
public class HealthComponentFactory extends ComponentFactory<EntityFactoryParam> {


    @Override
    public String getType() {
        return "Health";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        HealthComponent component = engine.createComponent(HealthComponent.class);
        component.health = Integer.parseInt(properties.getString("health"));
        component.spawningDuration = Integer.parseInt(properties.getString("spawningDuration"));
    }
}
