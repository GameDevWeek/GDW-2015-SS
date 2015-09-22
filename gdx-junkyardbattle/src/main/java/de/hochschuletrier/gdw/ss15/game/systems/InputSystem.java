package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.ss15.game.components.InputComponent;
import de.hochschuletrier.gdw.ss15.game.input.InputAction;

import java.util.LinkedList;

/**
 * Created by glumbatsch on 21.09.2015.
 * KameHameHAH!
 */
public class InputSystem extends IteratingSystem implements InputProcessor {

    private LinkedList<InputAction> actions;

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
            case Input.Keys.W: actions.add(new InputAction("upPressed",1)); //1 = Networksystem.getTimestamp();
                break;
            case Input.Keys.S: actions.add(new InputAction("downPressed",1));
                break;
            case Input.Keys.D: actions.add(new InputAction("rightPressed",1));
                break;
            case Input.Keys.A: actions.add(new InputAction("leftPressed",1));
                break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.W: actions.add(new InputAction("upReleased",1));
                break;
            case Input.Keys.S: actions.add(new InputAction("downReleased",1));
                break;
            case Input.Keys.D: actions.add(new InputAction("rightReleased",1));
                break;
            case Input.Keys.A: actions.add(new InputAction("leftReleased",1));
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
        switch (button){
            case Input.Buttons.LEFT: actions.add(new InputAction("leftMBPressed", 1, screenX,screenY));
                break;
            case Input.Buttons.RIGHT: actions.add(new InputAction("rightMBPressed", 1,screenX,screenY));
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button){
            case Input.Buttons.LEFT: actions.add(new InputAction("leftMBReleased", 1,screenX,screenY));
                break;
            case Input.Buttons.RIGHT: actions.add(new InputAction("rightMBReleased", 1, screenX, screenY));
                break;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //brauchen wir nicht
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        actions.add(new InputAction("mouseMoved", 1, screenX,screenY));
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
