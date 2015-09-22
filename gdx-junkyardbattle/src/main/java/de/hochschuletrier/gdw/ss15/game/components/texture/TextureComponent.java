/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.texture;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author Julien Saevecke
 */
public class TextureComponent extends Component implements Pool.Poolable{
    
    public Texture texture = null;
    public int width = 24;
    public int height = 24;
    public int srcX = 0;
    public int srcY = 0;
    public float scaleX = 1;
    public float scaleY = 1;
    public boolean flipX = false;
    public boolean flipY = false;
    
    @Override
    public void reset() {
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
