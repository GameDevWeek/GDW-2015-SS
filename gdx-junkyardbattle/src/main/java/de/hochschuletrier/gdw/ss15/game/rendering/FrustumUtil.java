package de.hochschuletrier.gdw.ss15.game.rendering;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Frustum checks for the MainCamera.
 * 
 * @author compie
 *
 */
public class FrustumUtil {
    private static final BoundingBox bbox = new BoundingBox();

    public static boolean inFrustum(float x, float y, TextureRegion region) {
        bbox.min.set(x - region.getRegionWidth() * 0.5f, y - region.getRegionWidth() * 0.5f, 0);
        bbox.max.set(x + region.getRegionWidth() * 0.5f, y + region.getRegionWidth() * 0.5f, 0);
        
        return MainCamera.getOrthographicCamera().frustum.boundsInFrustum(bbox);
    }
    
    public static boolean inFrustum(BoundingBox box) {
        return MainCamera.getOrthographicCamera().frustum.boundsInFrustum(box);
    }
}
