package de.hochschuletrier.gdw.ss15.game.components.factories.network.server;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

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
        component.updateDuration = properties.getFloat("updateDuration", 42);
        component.sendSave = properties.getBoolean("sendSave", false);
        component.lastSendTimer = new MyTimer(true);
        //System.out.println("updateDuration: "+  component.updateDuration);
        entity.add(component);
    }
}
