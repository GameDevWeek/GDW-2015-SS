package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.*;
import de.hochschuletrier.gdw.ss15.game.components.InputComponent;
import de.hochschuletrier.gdw.ss15.game.input.InputMovPaket;

import java.util.LinkedList;

/**
 * Created by glumbatsch on 21.09.2015.
 * KameHameHAH!
 */
public class InputSystem extends IteratingSystem implements InputProcessor {

    public InputMovPaket inputPaket;

    public InputSystem() {
        this(0);
    }

    public InputSystem(int priority) {
        super(Family.one(InputComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public boolean keyDown(int keycode) {
        // keyDown = ein knopf wurde gedrückt
        switch (keycode) {
            case Input.Keys.W:
                inputPaket.up = true;
                break;
            case Input.Keys.S:
                inputPaket.down = true;
                break;
            case Input.Keys.D:
                inputPaket.right = true;
                break;
            case Input.Keys.A:
                inputPaket.left = true;
                break;
        }
        System.out.println(inputPaket);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // keyUp = ein knopf wurde losgelassen
        switch (keycode) {
            case Input.Keys.W:
                inputPaket.up = false;
                break;
            case Input.Keys.S:
                inputPaket.down = false;
                break;
            case Input.Keys.D:
                inputPaket.right = false;
                break;
            case Input.Keys.A:
                inputPaket.left = false;
                break;
        }
        System.out.println(inputPaket);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // touchDown = mouseClick
        switch (button){
            case Input.Buttons.LEFT:
                new ShootDownEvent(screenX,screenY,99);
                break;
            case Input.Buttons.RIGHT:
                new GatherDownEvent(screenX,screenY,99);
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // touchUp = mouseClick
        switch (button){
            case Input.Buttons.LEFT:
                new ShootUpEvent(screenX,screenY,99);
                break;
            case Input.Buttons.RIGHT:
                new GatherUpEvent(screenX,screenY,99);
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
        // winkel
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        Main.getInstance().inputMultiplexer.addProcessor(this);
        inputPaket = new InputMovPaket();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

}
