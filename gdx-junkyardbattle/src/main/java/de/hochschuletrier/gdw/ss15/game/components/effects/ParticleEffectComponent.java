/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.effects;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author Julien Saevecke
 */
public class ParticleEffectComponent extends Component implements Pool.Poolable{
    
    public ParticleEffect particleEffect = null;
    public float positionOffsetX = 0;
    public float positionOffsetY = 0;
    public boolean start = true;
    public boolean isPlaying = false;
    public boolean loop = true;

    public void stop()
    {
        isPlaying = false;
    }
    
    public void changeMode(EffectMode mode)
    {
        switch(mode){
            case START:
                isPlaying = true;
                break;
            case STOP:
                isPlaying = false;
                break;
            case STARTANDRESET:
                isPlaying = true;
                for(ParticleEmitter particleEmitter : particleEffect.getEmitters()){
                    particleEmitter.durationTimer=0;
                }
                break;
            default:
                return ;
        }
    }

    public void setRotation(float rotation) {
        Array<ParticleEmitter> particleEmitter = particleEffect.getEmitters();  
        
        for (int i = 0; i < particleEmitter.size; i++) { 
            ScaledNumericValue angle = particleEmitter.get(i).getAngle();
            
            float spanHigh = angle.getHighMax() - angle.getHighMin();
            
            angle.setHigh(rotation - spanHigh / 2.f, rotation + spanHigh / 2.f);
            
            float spanLow = angle.getLowMax() - angle.getLowMin();
            
            angle.setLow(rotation - spanLow / 2.f, rotation + spanLow / 2.f);
        }
    }
    
    @Override
    public void reset() {
        positionOffsetX = 0;
        positionOffsetY = 0;
        particleEffect = null;
        start = true;
        isPlaying = false;
        loop = true;
    }
}
