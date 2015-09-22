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
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PositionSynchComponent component = engine.createComponent(PositionSynchComponent.class);
        component.reset(); // sollte funktionieren
        component.clientName=properties.getString("clientname");
        //System.out.println("Gelesen: "+ component.clientName);
        entity.add(component);
    }
}
