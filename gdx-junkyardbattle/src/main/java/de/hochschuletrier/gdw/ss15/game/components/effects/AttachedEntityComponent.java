package de.hochschuletrier.gdw.ss15.game.components.effects;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class AttachedEntityComponent extends Component implements Pool.Poolable {
    public ArrayList<Entity> entities = new ArrayList<>();
    
    @Override
    public void reset() {
        entities.clear();
    }
    
}
