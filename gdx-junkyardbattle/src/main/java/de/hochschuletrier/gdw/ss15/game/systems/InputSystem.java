package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.ss15.game.components.InputComponent;
import de.hochschuletrier.gdw.ss15.events.InputEvent;

import java.util.LinkedList;

/**
 * Created by glumbatsch on 21.09.2015.
 * KameHameHAH!
 */
public class InputSystem extends IteratingSystem implements InputProcessor {

    private LinkedList<InputEvent> actions;

    public InputSystem(){
        this(0);
    }

    public InputSystem(int priority){
        super(Family.one(InputComponent.class).get(),priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.W:
                break;
            case Input.Keys.S:
                break;
            case Input.Keys.D:
                break;
            case Input.Keys.A:
                break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.W:
                break;
            case Input.Keys.S:
                break;
            case Input.Keys.D:
                break;
            case Input.Keys.A:
                break;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //brauchen wir nicht
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // winkel
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
