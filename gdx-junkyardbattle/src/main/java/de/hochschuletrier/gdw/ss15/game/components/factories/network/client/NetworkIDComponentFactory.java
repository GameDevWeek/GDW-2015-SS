package de.hochschuletrier.gdw.ss15.game.components.factories.network.client;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.network.client.NetworkIDComponent;

/**
 * Created by hherm on 22/09/2015.
 */
public class NetworkIDComponentFactory extends ComponentFactory<EntityFactoryParam> {
    @Override
    public String getType() {
        return "NetworkID";
    }

    @Override
    public void run(Entity entity, SafeProperties safeProperties, SafeProperties safeProperties1, EntityFactoryParam entityFactoryParam) {
        NetworkIDComponent component = engine.createComponent(NetworkIDComponent.class);
        component.reset();
        entity.add(component);
    }
}
