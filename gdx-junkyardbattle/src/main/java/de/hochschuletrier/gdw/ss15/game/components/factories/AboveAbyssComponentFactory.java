package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.AboveAbyssComponent;

public class AboveAbyssComponentFactory extends ComponentFactory<EntityFactoryParam>{

    @Override
    public String getType() {
      return "AboveAbyss";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        AboveAbyssComponent comp = engine.createComponent(AboveAbyssComponent.class);
        entity.add(comp);
    }

}
