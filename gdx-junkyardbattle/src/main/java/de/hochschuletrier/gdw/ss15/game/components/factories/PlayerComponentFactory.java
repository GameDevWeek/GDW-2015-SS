package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 22.09.2015.
 */
public class PlayerComponentFactory extends ComponentFactory<EntityFactoryParam> {


    @Override
    public String getType() {
        return "Player";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PlayerComponent component = engine.createComponent(PlayerComponent.class);
        component.name = properties.getString("name");
        component.isLocalPlayer = properties.getBoolean("isLocalPlayer", false);

    }
}
