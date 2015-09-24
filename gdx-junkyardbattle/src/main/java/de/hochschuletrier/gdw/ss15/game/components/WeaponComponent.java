package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponComponent extends Component implements Pool.Poolable {

    public final float  maximumFireTime = 3f;
    public final float  maximumScattering = (float)Math.PI * 0.5f;
    public final int    maximumShardsPerShot = 5;

    public float fireChannelTime = 0.0f;
    public float harvestChannelTime = 0.0f;

    @Override
    public void reset() {

    }
}
