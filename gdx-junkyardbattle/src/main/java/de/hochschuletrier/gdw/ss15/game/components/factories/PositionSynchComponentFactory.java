package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.PositionSynchComponent;

/**
 * Created by hherm on 22/09/2015.
 */
public class PositionSynchComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "PositionSynch";
    }

    @Override
    public void run(Entity entity, SafeProperties safeProperties, SafeProperties safeProperties1, EntityFactoryParam entityFactoryParam) {
        PositionSynchComponent component = engine.createComponent(PositionSynchComponent.class);
        component.reset(); // sollte funktionieren
    }
}
