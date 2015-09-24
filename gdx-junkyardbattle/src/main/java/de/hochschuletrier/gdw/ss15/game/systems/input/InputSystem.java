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

    private boolean isListener = false;
    private boolean controllerActive;

    private final float STICKDEADZONE = 0.25f;
    private final float RADIUS= 123.0f;
    private double winkel;

    private float horizontal = 0.0f;
    private float vertical = 0.0f;

    private boolean leftMBDown = false;
    private boolean rightMBDown = false;

    private int posX;
    private int posY;

    private float r1Horizontal;
    private float r1Vertical;

    public InputSystem() {
        this(0);
    }

    public InputSystem(int priority) {
        super(Family.all(InputComponent.class, PlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity.getComponent(PlayerComponent.class).isLocalPlayer) {
            InputComponent input = entity.getComponent(InputComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);

            input.horizontal = horizontal;
            input.vertical = vertical;

            input.shoot = leftMBDown;
            input.gather = rightMBDown;

            input.posX = posX;
            input.posY = posY;

            input.rightStickAngle = winkel;
            input.isController = controllerActive;
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
        //brauchen wir nicht
        return false;
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
        System.out.println("Controller connected");
    }

    @Override
    public void disconnected(Controller controller) {
        //geht auch nicht
        System.out.println("Controller disconnected");
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
            default:
                rightMBDown = leftMBDown = false;
        }
        //debug();
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        controllerActive = true;
        switch (buttonCode) {
            case XBox360KeyMap.X:
                leftMBDown = false;
                break;
            case XBox360KeyMap.B:
                rightMBDown = false;
                break;
        }
        //debug();
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        controllerActive = true;
        switch (axisCode) {
            case XBox360KeyMap.TRIGGER: //Triggertasten
                if (value < -0.1)
                    leftMBDown = true;
                else if (value > 0.1)
                    rightMBDown = true;
                else
                    leftMBDown = rightMBDown = false;
                break;
            case XBox360KeyMap.L1X:
                if (value > STICKDEADZONE || value < -STICKDEADZONE)
                    horizontal = value;
                else
                    horizontal = 0.0f;
                break;
            case XBox360KeyMap.L1Y:
                if (value > STICKDEADZONE || value < -STICKDEADZONE)
                    vertical = value;
                else
                    vertical = 0.0f;
                break;
            case XBox360KeyMap.R1X:
                   r1Horizontal = value;
                break;
            case XBox360KeyMap.R1Y:
                    r1Vertical = value;

                break;
        }
        //debug();
        winkel = Math.toDegrees(Math.atan2(r1Vertical,r1Horizontal));
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
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
        //System.out.println("pocCode: " + povCode + "\npovDirection: " + value);
        debug();
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
        System.out.println("");
        System.out.println("");
        System.out.println("Controller active: " + controllerActive);
        System.out.println("horizontal: " + horizontal);
        System.out.println("vertical: " + vertical);
        System.out.println("");
        System.out.println("shoot: " + leftMBDown);
        System.out.println("gather: " + rightMBDown);
        System.out.println("");
        System.out.println("posX: " + posX);
        System.out.println("posY: " + posY);
        System.out.println("");
        System.out.println("rechter Stick Winkel: " + winkel);
    }
}
