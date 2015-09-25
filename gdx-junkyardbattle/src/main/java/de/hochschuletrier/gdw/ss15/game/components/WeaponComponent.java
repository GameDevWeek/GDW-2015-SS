package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponComponent extends Component implements Pool.Poolable {

    public static final float  maximumFireTime = 2f;
    public static final float  maximumScattering = (float)Math.PI * 0.7f;
    public static final int    ShardsPerShot = 5;
    public static final float fireCooldown = 1.0f;
    
    public boolean fireCooldownReady = false;
    public float fireChannelTime = 0.0f;
    public float harvestChannelTime = 0.0f;

    @Override
    public void reset() {

    }
}
