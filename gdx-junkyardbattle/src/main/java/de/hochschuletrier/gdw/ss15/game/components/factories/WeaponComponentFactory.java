package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;

public class WeaponComponentFactory extends ComponentFactory<EntityFactoryParam> {


    
    public String getType() {
        return "Weapon";
    }

    
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        WeaponComponent component = engine.createComponent(WeaponComponent.class);
        //component.health = Integer.parseInt(properties.getString("health"));
        entity.add(component);
    }
}
