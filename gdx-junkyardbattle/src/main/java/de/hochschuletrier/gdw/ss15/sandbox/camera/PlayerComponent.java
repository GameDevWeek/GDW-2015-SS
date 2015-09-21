package de.hochschuletrier.gdw.ss15.sandbox.camera;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent extends Component implements Poolable {

    public int placeholder = 0;
    
    @Override
    public void reset() {
        placeholder = 0;
    }

    
    
}
