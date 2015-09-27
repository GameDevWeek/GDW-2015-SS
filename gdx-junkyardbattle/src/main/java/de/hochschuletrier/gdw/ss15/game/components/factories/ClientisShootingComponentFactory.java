package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.ClientIsShootingComponent;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;

/**
 * Created by lukas on 27.09.15.
 */
public class ClientisShootingComponentFactory  extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "ClientIsShooting";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        ClientIsShootingComponent component = engine.createComponent(ClientIsShootingComponent.class);
        entity.add(component);
    }

}
