package de.hochschuletrier.gdw.ss15.game.rendering;

import com.badlogic.gdx.math.Interpolation;

public final class ZoomingModes {

    public enum modes {
        pow2, pow3, pow4, pow5, sine, exp5, 
        exp10, circle, elastic, swing, bounce, linear
    };
    
    public static float interpolate(modes mode, float srcZoom, float dstZoom, float zoomProgress){
        
        if(zoomProgress <= 0f)
            return srcZoom;
        else if(zoomProgress >= 1f)
            return dstZoom;
        
        float retValue = 0f;
        
        switch(mode){
        case pow2:
            retValue = Interpolation.pow2.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case pow3:
            retValue = Interpolation.pow3.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case pow4:
            retValue = Interpolation.pow4.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case pow5:
            retValue = Interpolation.pow5.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case exp5:
            retValue = Interpolation.exp5.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case exp10:
            retValue = Interpolation.exp10.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case bounce:
            retValue = Interpolation.bounce.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case circle:
            retValue = Interpolation.circle.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case elastic:
            retValue = Interpolation.elastic.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case sine:
            retValue = Interpolation.sine.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case swing:
            retValue = Interpolation.swing.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        case linear:
            retValue = Interpolation.linear.apply(srcZoom, dstZoom, zoomProgress);
            break;
            
        }
        
        return retValue;
        
    }        
}