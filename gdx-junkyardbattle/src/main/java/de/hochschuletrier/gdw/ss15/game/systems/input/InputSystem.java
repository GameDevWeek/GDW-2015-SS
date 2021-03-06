package de.hochschuletrier.gdw.ss15.game.systems.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.input.XBox360KeyMap;

/**
 * Created by David Siepen on 21.09.2015.
 * <p>
 * Das InputSystem ist ein InputProcessor, der beim Spiel angemeldet wird.
 * Wenn eine Taste gedrueckt wird, wird die entsprechende methode mit dem
 * zugehoerigen keycode aufgerufen.
 */
public class InputSystem extends IteratingSystem implements InputProcessor, ControllerListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public Camera camera;

    private boolean isListener = false;
    private boolean controllerActive;

    private final float STICKDEADZONE = 0.25f;
    private float radius = Gdx.graphics.getHeight() / 3;
    private Vector2 rightStick = new Vector2();

    private float horizontal = 0.0f;
    private float vertical = 0.0f;

    private boolean leftMBDown = false;
    private boolean rightMBDown = false;
    private boolean escape = false;

    private int posX;
    private int posY;

    private float r1Horizontal;
    private float r1Vertical;

    public InputSystem(int priority, OrthographicCamera camera) {
        super(Family.all(InputComponent.class, PlayerComponent.class).get(), priority);
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity.getComponent(PlayerComponent.class).isLocalPlayer) {
            InputComponent input = entity.getComponent(InputComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);
            Vector3 playerScreenpos = camera.project(new Vector3(position.x, position.y, 0));

            input.horizontal = horizontal;
            input.vertical = vertical;

            input.shoot = leftMBDown;
            input.gather = rightMBDown;
            input.escape = escape;

            input.rightStickAngle = rightStick.angle();
            input.isController = controllerActive;

            if(controllerActive){
                rightStick.nor().scl(radius);
                input.posX = (int)(rightStick.x + playerScreenpos.x);
                input.posY = (int)(rightStick.y + playerScreenpos.y);

            } else {
                input.posX = posX;
                input.posY = posY;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        // keyDown = ein knopf wurde gedrückt
        controllerActive = false;
        switch (keycode) {
            case Input.Keys.W:
                vertical -= 1.0f;
                break;
            case Input.Keys.S:
                vertical += 1.0f;
                break;
            case Input.Keys.D:
                horizontal += 1.0f;
                break;
            case Input.Keys.A:
                horizontal -= 1.0f;
                break;
            case Input.Keys.UP:
                vertical -= 1.0f;
                break;
            case Input.Keys.DOWN:
                vertical += 1.0f;
                break;
            case Input.Keys.RIGHT:
                horizontal += 1.0f;
                break;
            case Input.Keys.LEFT:
                horizontal -= 1.0f;
                break;
            case Input.Keys.ESCAPE:
                escape = true;
                break;
        }
        //debug();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // keyUp = ein knopf wurde losgelassen
        controllerActive = false;
        switch (keycode) {
            case Input.Keys.W:
                vertical += 1.0f;
                break;
            case Input.Keys.S:
                vertical -= 1.0f;
                break;
            case Input.Keys.D:
                horizontal -= 1.0f;
                break;
            case Input.Keys.A:
                horizontal += 1.0f;
                break;

            case Input.Keys.UP:
                vertical += 1.0f;
                break;
            case Input.Keys.DOWN:
                vertical -= 1.0f;
                break;
            case Input.Keys.RIGHT:
                horizontal -= 1.0f;
                break;
            case Input.Keys.LEFT:
                horizontal += 1.0f;
                break;
            case Input.Keys.ESCAPE:
                escape = false;
                break;
        }
        //debug();
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        controllerActive = false;
        // touchDown = mouseClick
        switch (button) {
            case Input.Buttons.LEFT:
                leftMBDown = true;
                break;
            case Input.Buttons.RIGHT:
                rightMBDown = true;
                break;
        }
        //debug();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        controllerActive = false;
        // touchUp = mouseClick
        switch (button) {
            case Input.Buttons.LEFT:
                leftMBDown = false;
                break;
            case Input.Buttons.RIGHT:
                rightMBDown = false;
                break;
        }
        //debug();
        return true;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //brauchen wir wohl
        controllerActive = false;
        posX = screenX;
        posY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        controllerActive = false;
        posX = screenX;
        posY = screenY;
        //debug();
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
        this.isListener = true;
        // Das InputPaket fuer den Server wird initialisiert
    }


    // Controller
    //-------------------------------------------------------------------------------


    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        Controllers.removeListener(this);
    }

    @Override
    public void connected(Controller controller) {
        //geht nicht
    }

    @Override
    public void disconnected(Controller controller) {
        //geht auch nicht
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        controllerActive = true;
        switch (buttonCode) {
            case XBox360KeyMap.X:
                leftMBDown = true;
                break;
            case XBox360KeyMap.B:
                rightMBDown = true;
                break;
            case XBox360KeyMap.START:
                escape = true;
                break;
            default:
                rightMBDown = leftMBDown = false;
        }
        //debug();
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        controllerActive = true;
        System.out.println(buttonCode);
        switch (buttonCode) {
            case XBox360KeyMap.X:
                leftMBDown = false;
                break;
            case XBox360KeyMap.B:
                rightMBDown = false;
                break;
            case XBox360KeyMap.START:
                escape = false;
                break;
        }
        //debug();
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        controllerActive = true;
        switch (axisCode) {
            case XBox360KeyMap.RTRIGGER: // linker Trigger
                if (value > 0)
                    leftMBDown = true;
                else
                    leftMBDown =  false;
                break;
            case XBox360KeyMap.LTRIGGER: // rechter Trigger
                if (value > 0)
                    rightMBDown = true;
                else
                    rightMBDown=  false;
                break;
            case XBox360KeyMap.L1X:
                if (value > STICKDEADZONE || value < -STICKDEADZONE)
                    vertical = value;
                else
                    vertical = 0.0f;
                break;
            case XBox360KeyMap.L1Y:
                if (value > STICKDEADZONE || value < -STICKDEADZONE)
                    horizontal = value;
                else
                    horizontal = 0.0f;
                break;
            case XBox360KeyMap.R1X:
                r1Vertical = value;
                break;
            case XBox360KeyMap.R1Y:
                r1Horizontal = value;
                break;
//            default:
//                System.out.println(axisCode);
//                break;
        }
        rightStick.set(r1Horizontal, r1Vertical);
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        /*
        controllerActive = true;
        horizontal = vertical = 0.0f;
        switch (value) {
            case north:
                vertical = -1.0f;
                horizontal = 0.0f;
                break;
            case northEast:
                vertical = -1.0f;
                horizontal = 1.0f;
                break;
            case east:
                horizontal = 1.0f;
                vertical = 0.0f;
                break;
            case southEast:
                vertical = 1.0f;
                horizontal = 1.0f;
                break;
            case south:
                horizontal = 0.0f;
                vertical = 1.0f;
                break;
            case southWest:
                vertical = 1.0f;
                horizontal = -1.0f;
                break;
            case west:
                horizontal = -1.0f;
                vertical = 0.0f;
                break;
            case northWest:
                vertical = -1.0f;
                horizontal = -1.0f;
                break;
            case center:
                vertical = 0.0f;
                horizontal = 0.0f;
        }
        logger.debug("pocCode: " + povCode + "\npovDirection: " + value);
        debug();
        */
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


    @Override
    public void update(float deltaTime) {
        // sucht nach controllern, falls keiner angemeldet ist!
        super.update(deltaTime);
        if (!this.isListener) {
            for (Controller controller : Controllers.getControllers()) {
                if (!this.isListener){
                    Controllers.addListener(this);
                 }
            }
        }

    }

    private void debug() {








    }
}
