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
import de.hochschuletrier.gdw.ss15.game.network.Packets.InputMovPaket;

/**
 * Created by David Siepen on 21.09.2015.
 *
 * Das InputSystem ist ein InputProcessor, der beim Spiel angemeldet wird.
 * Wenn eine Taste gedrueckt wird, wird die entsprechende methode mit dem
 * zugehoerigen keycode aufgerufen.
 *
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
        //System.out.println(inputPaket);
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
        //System.out.println(inputPaket);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // touchDown = mouseClick
        switch (button) {
            case Input.Buttons.LEFT:
                GatherUpEvent.emit(screenX,screenY);
                break;
            case Input.Buttons.RIGHT:
                GatherUpEvent.emit(screenX,screenY);
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // touchUp = mouseClick
        switch (button) {
            case Input.Buttons.LEFT:
                GatherUpEvent.emit(screenX,screenY);
                break;
            case Input.Buttons.RIGHT:
                GatherUpEvent.emit(screenX,screenY);
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
        // Das System wird zur Engine hinzugefuegt
        super.addedToEngine(engine);
        // Der InputProcessor wird beim Spiel angemeldet
        Main.getInstance().inputMultiplexer.addProcessor(this);
        // Das InputPaket fuer den Server wird initialisiert
        inputPaket = new InputMovPaket();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

}
