package de.hochschuletrier.gdw.ss15.game.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TileSetAnimation;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.MapLoader.TileCreationListener;
import de.hochschuletrier.gdw.ss15.game.MapSpecialEntities.CreatorInfo;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;

public class TileMapCreator implements TileCreationListener {
    private PooledEngine engine;
    
    public void init(PooledEngine e) {
        engine = e;
    }
    
    private void addRenderComponents(Entity entity, TiledMap map, Layer layer, TileInfo info, float tileX, float tileY, float offsetX, float offsetY) {
        TileSet tileset = map.findTileSet(info.globalId);
        Texture texture = (Texture) tileset.getAttachment();

        int sheetX = tileset.getTileX(info.localId);
        int sheetY = tileset.getTileY(info.localId);

        int mapTileWidth = map.getTileWidth();
        int mapTileHeight = map.getTileHeight();

        float px = (tileX * mapTileWidth)  + offsetX;
        float py = (tileY * mapTileHeight) + offsetY;
        
        int coordX = (int) (sheetX * tileset.getTileWidth()); 
        coordX += tileset.getTileMargin() + sheetX * tileset.getTileSpacing();
        int coordY = ((int) sheetY * tileset.getTileHeight());
        coordY += tileset.getTileMargin() + sheetY * tileset.getTileSpacing();                
        
        TextureRegion region = new TextureRegion(texture);
        region.setRegion(coordX, coordY, tileset.getTileWidth(), tileset.getTileHeight());
        
//        String filename = "data/normal_maps/" + tileset.getImage().getSource();
//        FileHandle fh = Gdx.files.internal(filename);

//        if(fh.exists()) {
//            Texture normalMap = assetManager.getTexture(fh.nameWithoutExtension() + "_n");
//            if(normalMap != null) {
//                NormalMapComponent normalMapComponent = engine.createComponent(NormalMapComponent.class);
//                normalMapComponent.normalMap = normalMap;
//                entity.add(normalMapComponent);
//            }
//        }
        
        addRenderComponents(entity, px, py, layer.getIndex(), texture, region);
    }
    
    private void addRenderComponents(Entity entity, TiledMap map, Layer layer, TileInfo info, float tileX, float tileY) {
        TileSet tileset = map.findTileSet(info.globalId);
        
        int mapTileWidth = map.getTileWidth();
        int mapTileHeight = map.getTileHeight();
        int tileOffsetY = tileset.getTileHeight() - mapTileHeight;
      
        addRenderComponents(entity, map, layer, info, tileX, tileY, mapTileWidth*0.5f, mapTileHeight*0.5f - tileOffsetY);
    }
    
    /**
     * Extracts information from the map and tile info to add components to the the given entity.
     * Make sure the property "animationFrames" of the TileSet is set to greater than 1.
     */
    private void addRenderComponents(Entity entity, TiledMap map, Layer layer, TileInfo info, float tileX, float tileY, PlayMode playMode) {
        TileSet tileset = map.findTileSet(info.globalId);
        int frames = tileset.getIntProperty("animationFrames", 1);
        
        assert(frames > 1);

        Texture image = (Texture) tileset.getAttachment();
        
        TileSetAnimation animation = new TileSetAnimation(
                frames,
                tileset.getFloatProperty("animationDuration", 0),
                tileset.getIntProperty("animationOffset", 0));
        
        TextureRegion[] regions = new TextureRegion[frames];
        float[] frameDurations = new float[frames];
        
        int tileOffsetY = tileset.getTileHeight() - map.getTileHeight();
        
        float px = (tileX * map.getTileWidth()) + map.getTileWidth()*0.5f;
        float py = (tileY * map.getTileHeight()) - tileOffsetY + map.getTileHeight()*0.5f;
        
//        float stateTime = tileset.getTileX(info.localId) * animation.frameDuration;
        
        for(int i=0; i<frames; i++) {
            tileset.updateAnimation(animation.frameDuration*i);
            int sheetX = tileset.getTileX(info.localId);
            int sheetY = tileset.getTileY(info.localId);
            
            int coordX = (int) (sheetX * tileset.getTileWidth()); 
            coordX += tileset.getTileMargin() + sheetX * tileset.getTileSpacing();
            int coordY = (int) (sheetY * tileset.getTileHeight());
            coordY += tileset.getTileMargin() + sheetY * tileset.getTileSpacing();  
            
            regions[i] = new TextureRegion(image);
            regions[i].setRegion(coordX, coordY, tileset.getTileWidth(), tileset.getTileHeight());
            frameDurations[i] = animation.frameDuration;
        }

        tileset.updateAnimation(0f);
        AnimationExtended anim = new AnimationExtended(playMode, frameDurations, regions);
        
//        String filename = "data/normal_maps/" + tileset.getImage().getSource();
//        FileHandle fh = Gdx.files.internal(filename);
//
//        if(fh.exists()) {
//            Texture normalMap = assetManager.getTexture(fh.nameWithoutExtension() + "_n");
//            NormalMapComponent normalMapComponent = engine.createComponent(NormalMapComponent.class);
//            normalMapComponent.normalMap = normalMap;
//            entity.add(normalMapComponent);
//        }
        
        addRenderComponents(entity, px, py, layer.getIndex(), anim, 0.f);
    }
    
    private void addRenderComponents(Entity entity, float x, float y, int layer, Texture texture, TextureRegion region) {
        PositionComponent posComp = engine.createComponent(PositionComponent.class);
        posComp.layer = layer;
        posComp.x = x;
        posComp.y = y;
        
        TextureComponent texComp = engine.createComponent(TextureComponent.class);
        texComp.reset();
        texComp.texture = texture;
        texComp.srcX = region.getRegionX();
        texComp.srcY = region.getRegionY();
        texComp.width = region.getRegionWidth();
        texComp.height = region.getRegionHeight();   
        
        entity.add(posComp);
        entity.add(texComp);
        engine.addEntity(entity);
    }
    
    private void addRenderComponents(Entity entity, float x, float y, int layer, 
            AnimationExtended animation, float stateTime) {

        PositionComponent posComp = engine.createComponent(PositionComponent.class);
        posComp.layer = layer;
        posComp.x = x;
        posComp.y = y;
        
        AnimatorComponent animComp = engine.createComponent(AnimatorComponent.class);
        animComp.reset();
        animComp.animationStates.put(AnimationState.IDLE, animation);
        animComp.stateTime = stateTime;
        
        entity.add(posComp);
        entity.add(animComp);
        engine.addEntity(entity);
    }

    @Override
    public void onTileCreate(CreatorInfo info) {
        Entity entity = engine.createEntity();
         
        boolean isTile = info.asTile != null;
        if(isTile) {
            TileSet tileset = info.tiledMap.findTileSet(info.asTile.globalId);
            int frames = tileset.getIntProperty("animationFrames", 1);
            if(frames > 1) {
                addRenderComponents(entity, info.tiledMap, info.layer, info.asTile, 
                        info.posX, info.posY, PlayMode.LOOP);
            } else {
                addRenderComponents(entity, info.tiledMap, info.layer, 
                        info.asTile, info.posX, info.posY);
            }
        }
    }
}
