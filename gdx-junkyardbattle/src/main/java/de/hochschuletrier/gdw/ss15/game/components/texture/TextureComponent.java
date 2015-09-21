/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.texture;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author Julien Saevecke
 */
public class TextureComponent extends Component implements Pool.Poolable{
    
    public Texture texture;
    public int width;
    public int height;
    public int srcX;
    public int srcY;
    public float scaleX;
    public float scaleY;
    public boolean flipX;
    public boolean flipY;
    public int layer;
    
    @Override
    public void reset() {
        texture = null;
        width = 24;
        height = 24;
        srcX = 0;
        srcY = 0;
        scaleX = 1;
        scaleY = 1;
        flipX = false;
        flipY = false;
    }
}
