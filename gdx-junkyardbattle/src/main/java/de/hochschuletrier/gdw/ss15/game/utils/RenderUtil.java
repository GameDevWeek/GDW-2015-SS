package de.hochschuletrier.gdw.ss15.game.utils;

import com.badlogic.gdx.graphics.Color;

public class RenderUtil {
    private static float[] RGBA_C = new float[4];
    
    public static Color extractColor(String colorStr) {
        if(colorStr == null)
            return new Color();
        
        RGBA_C[0] = RGBA_C[1] = RGBA_C[2] = 0.f;
        RGBA_C[3] = 1.f; 
        String[] rgbaStr = colorStr.split(",");
        
        int i = 0;
        for(String c : rgbaStr) {
            try {
                float colorComponent = Float.parseFloat(c);
                RGBA_C[i++] = colorComponent;
            } catch (NumberFormatException e) {
                return new Color();
            }
        }
        
        return new Color(RGBA_C[0], RGBA_C[1], RGBA_C[2], RGBA_C[3]);
    }
}
