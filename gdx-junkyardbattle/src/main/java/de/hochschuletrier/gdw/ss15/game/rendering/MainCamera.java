package de.hochschuletrier.gdw.ss15.game.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MainCamera {
    private static final BoundedCamera camera = new BoundedCamera();

    public static final BoundedCamera get() {
        return camera;
    }
    
    public static void updateForced() {
        camera.updateForced();
    }

    public static Vector3 getPosition() {
        return camera.getPosition();
    }

    public static void setCameraPosition(float x, float y) {
        camera.setCameraPosition(x, y);
    }

    public static final void setDestination(Vector2 p) {
        camera.setDestination(p);
    }

    public static float getFactor() {
        return camera.getFactor();
    }

    public static void setCameraPosition(Vector3 pos) {
        camera.setCameraPosition(pos);
    }

    public static void setFactor(float factor) {
        camera.setFactor(factor);
    }

    public static void setZoom(float newZoom) {
        camera.setZoom(newZoom);
    }

    public static float getZoom() {
        return camera.getZoom();
    }

    public static float getLeftOffset() {
        return camera.getLeftOffset();
    }

    public static float getTopOffset() {
        return camera.getTopOffset();
    }

    public static final void bind() {
        camera.bind();
    }

    public static void update(float delta) {
        camera.update(delta);
    }

    public static void setDestination(float x, float y) {
        camera.setDestination(x, y);
    }

    public static void setBounds(float xMin, float yMin, float xMax, float yMax) {
        camera.setBounds(xMin, yMin, xMax, yMax);
    }

    public static void resize(int width, int height) {
        camera.resize(width, height);
    }

    public static OrthographicCamera getOrthographicCamera() {
        return camera.getOrthographicCamera();
    }
    
}
