package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.BasePointComponent;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;

public class BasePointComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "BasePoint";
    }

    @Override
    public void run(Entity entity, SafeProperties meta,
            SafeProperties properties, EntityFactoryParam param)
    {
        BasePointComponent component = engine.createComponent(BasePointComponent.class);
        component.points = 0;
        entity.add(component);
//        System.out.println("BULLET");
    }

}
