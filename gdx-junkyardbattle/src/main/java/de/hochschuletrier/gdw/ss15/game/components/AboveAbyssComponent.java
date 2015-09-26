package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;

import de.hochschuletrier.gdw.commons.utils.pool.Poolable;

public class AboveAbyssComponent extends Component implements Poolable{

    public int above;
    
    public AboveAbyssComponent() {
        this.above = 0;
    }
    
    @Override
    public void reset() {
       above = 0;
    }

}
