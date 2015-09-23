package de.hochschuletrier.gdw.ss15.game.systems.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by David Siepen on 21.09.2015.
 * <p>
 * Das InputSystem ist ein InputProcessor, der beim Spiel angemeldet wird.
 * Wenn eine Taste gedrueckt wird, wird die entsprechende methode mit dem
 * zugehoerigen keycode aufgerufen.
 */
public class InputSystem extends IteratingSystem implements InputProcessor, ControllerListener {


    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private boolean leftMBDown = false;
    private boolean rightMBDown = false;

    private int posX;
    private int posY;

    public InputSystem() {
        this(0);
    }

    public InputSystem(int priority) {
        super(Family.all(InputComponent.class, PlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(entity.getComponent(PlayerComponent.class).isLocalPlayer){
            entity.getComponent(InputComponent.class).up = up;
            entity.getComponent(InputComponent.class).down = down;
            entity.getComponent(InputComponent.class).left = left;
            entity.getComponent(InputComponent.class).right = right;

            entity.getComponent(InputComponent.class).shoot = leftMBDown;
            entity.getComponent(InputComponent.class).gather = rightMBDown;

            entity.getComponent(InputComponent.class).posX = posX;
            entity.getComponent(InputComponent.class).posY = posY;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        // keyDown = ein knopf wurde gedrückt
        switch (keycode) {
            case Input.Keys.W:
                up = true;
                break;
            case Input.Keys.S:
                down = true;
                break;
            case Input.Keys.D:
                right = true;
                break;
            case Input.Keys.A:
                left = true;
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
                up = false;
                break;
            case Input.Keys.S:
                down = false;
                break;
            case Input.Keys.D:
                right = false;
                break;
            case Input.Keys.A:
                left = false;
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
                leftMBDown=true;
                break;
            case Input.Buttons.RIGHT:
                rightMBDown = true;
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // touchUp = mouseClick
        switch (button) {
            case Input.Buttons.LEFT:
                leftMBDown = false;
                break;
            case Input.Buttons.RIGHT:
                rightMBDown = false;
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
        posX = screenX;
        posY = screenY;

        return true;
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
        Controllers.addListener(this);
        // Das InputPaket fuer den Server wird initialisiert
    }


    // Controller
    //---------------------------------------------------------------------------------------


    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void connected(Controller controller) {
        System.out.println("Controller connected");
    }

    @Override
    public void disconnected(Controller controller) {
        System.out.println("Controller disconnected");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        System.out.println(buttonCode);
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        // brauchen wir nicht!
        return false;
    }
}
