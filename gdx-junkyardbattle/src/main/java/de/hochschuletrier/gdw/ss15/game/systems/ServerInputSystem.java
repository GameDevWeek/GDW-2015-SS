package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss15.game.components.InputComponent;
import de.hochschuletrier.gdw.ss15.game.input.InputMovPaket;

/**
 * Created by glumbatsch on 22.09.2015.
 * KameHameHAH!
 */
public class ServerInputSystem extends IteratingSystem {

    public ServerInputSystem(){
        this(0);
    }

    public ServerInputSystem(int priority) {
        super(Family.one(InputComponent.class).get(),0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    public void onWasWeißIchEvent(Entity entity, InputMovPaket inputMovPaket){
        entity.getComponent(InputComponent.class).update(inputMovPaket);
    }
}
