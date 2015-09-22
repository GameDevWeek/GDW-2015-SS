package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.ClientComponent;

/**
 * Created by hherm on 22/09/2015.
 */
public class ClientComponentFactory extends ComponentFactory<EntityFactoryParam> {


    @Override
    public String getType() {
        return "Client";
    }

    @Override
    public void run(Entity entity, SafeProperties safeProperties, SafeProperties safeProperties1, EntityFactoryParam entityFactoryParam) {
        ClientComponent component = engine.createComponent(ClientComponent.class);
        component.reset();
        /**
         *  Hier evtl. Serverclientsocket initialisieren
         */
    }
}
