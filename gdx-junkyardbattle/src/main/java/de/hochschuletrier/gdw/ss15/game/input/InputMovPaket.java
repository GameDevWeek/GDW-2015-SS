package de.hochschuletrier.gdw.ss15.game.input;

/**
 * Created by Martin on 22.09.2015.
 */
public class InputMovPaket {

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public InputMovPaket() {
        this.up = this.down = this.left = this.right = false;
    }

    @Override
    public String toString() {
        String tempString = "";
        tempString += "up: " + this.up + "\n";
        tempString += "down: " + this.down + "\n";
        tempString += "left: " + this.left + "\n";
        tempString += "right: " + this.right + "\n";
        return tempString;
    }
}
