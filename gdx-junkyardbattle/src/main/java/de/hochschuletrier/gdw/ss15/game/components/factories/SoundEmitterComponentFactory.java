package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SoundEmitterComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;

public class SoundEmitterComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "SoundEmitter";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        SoundEmitterComponent component = engine.createComponent(SoundEmitterComponent.class);
        entity.add(component);
    }
}