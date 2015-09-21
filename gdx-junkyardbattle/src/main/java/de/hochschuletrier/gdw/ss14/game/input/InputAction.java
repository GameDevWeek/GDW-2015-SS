package de.hochschuletrier.gdw.ss14.game.input;

/**
 * Created by Martin on 21.09.2015.
 */
public class InputAction {

    public String actionName;
    public int timeStamp;
    public int xPos;
    public int yPos;

    public InputAction(String actionName, int timeStamp, int xPos, int yPos) {
        this.actionName = actionName;
        this.timeStamp = timeStamp;
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public InputAction() {
        this.actionName = null;
        this.timeStamp = 0;
        this.xPos = 0;
        this.yPos = 0;
    }

    public InputAction(String actionName, int timeStamp) {
        this.actionName = actionName;
        this.timeStamp = timeStamp;
        this.xPos = 0;
        this.yPos = 0;
    }
}
