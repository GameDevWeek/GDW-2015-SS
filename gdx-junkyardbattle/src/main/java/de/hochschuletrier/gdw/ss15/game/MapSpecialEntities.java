package de.hochschuletrier.gdw.ss15.game;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TileSetAnimation;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;


/**
 * 
 * @author tobidot (Tobias Gepp)
 *
 */

public class MapSpecialEntities
{
    private MapSpecialEntities() {};
    /**
     * CreatorInfo wird den Consumern ueberleifert die Entities mit besonderen Attributen erstellen
     */
    public static class CreatorInfo
    {
        Entity entity;
        TileInfo asTile;
        LayerObject asObject;
        Layer layer;            /// Layer fuer Renderer
        public CreatorInfo(Entity ent,LayerObject lo,Layer layer)
        {
            entity = ent;
            asObject = lo;
            asTile = null;
            this.layer = layer;
            
        }
        public CreatorInfo(Entity ent,TileInfo ti,Layer layer)
        {
            entity = ent;
            asObject = null;
            asTile = ti;
            this.layer = layer;            
        }
    }
    public static HashMap< String,Consumer<CreatorInfo> > specialEntities;
    
    /**
     * Klasse zum laden eines 'Dummy' Objects
     */
    public static class DummyEntity implements Consumer<CreatorInfo>
    {
        public void accept(CreatorInfo info)
        {
            /*
            // eine Componente herraussuchen und mit den 'gelesenen' Werten besetzen 
            TestComponent body = ComponentMapper.test.get( info.entity );
            if ( test != null )
            {
                boolean flag = info.data.getBooleanProperty("TestFlag", false);
                body.flag = flag;
            }
            */
        }
    }
    
    /*** Rendering ****/
    public static class TextureEntity implements Consumer<CreatorInfo> {
        public void accept(CreatorInfo info) {
        }
    }
    
    static void addRenderComponents(Entity entity, TiledMap map, Layer layer, TileInfo info, float tileX, float tileY, float offsetX, float offsetY) {
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
    
    static void addRenderComponents(Entity entity, TiledMap map, Layer layer, TileInfo info, float tileX, float tileY) {
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
    static void addRenderComponents(Entity entity, TiledMap map, Layer layer, TileInfo info, float tileX, float tileY, PlayMode playMode, boolean start) {
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
        
        float stateTime = tileset.getTileX(info.localId) * animation.frameDuration;
        
        for(int i=0; i<frames; i++) {
            tileset.updateAnimation(animation.frameDuration*i);

            int sheetX = tileset.getTileX(0);
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
        
        addRenderComponents(entity, px, py, layer.getIndex(), anim, start, stateTime);
    }
    
    private static void addRenderComponents(Entity entity, float x, float y, int layer, Texture texture, TextureRegion region) {
        PositionComponent posComp = ComponentMappers.position.get(entity);
        posComp.layer = layer;
        posComp.x = x;
        posComp.y = y;
        
        TextureComponent texComp = ComponentMappers.texture.get(entity);
        texComp.texture = texture;
        texComp.srcX = region.getRegionX();
        texComp.srcX = region.getRegionY();
        texComp.width = region.getRegionWidth();
        texComp.height = region.getRegionHeight();
    }
    
    private static void addRenderComponents(Entity entity, float x, float y, int layer, 
            AnimationExtended animation, boolean start, float stateTime) {

        PositionComponent posComp = ComponentMappers.position.get(entity);
        posComp.layer = layer;
        posComp.x = x;
        posComp.y = y;
        AnimatorComponent animComp = ComponentMappers.animator.get(entity);
        animComp.animationStates.put(AnimationState.IDLE, animation);
        animComp.stateTime = stateTime;
    }
    
    /*** Rendering end ****/
    
    static
    {
        Class allClasses[] = MapSpecialEntities.class.getClasses();         /// Alle Memberklassen von 'MapSpecialEntities'
        specialEntities = new HashMap<String, Consumer<CreatorInfo>>();     
        for ( Class c : allClasses ) 
        {
            /// nur alle Klassen, die von Consumer abgeleitet sind
            if ( c.isAssignableFrom( Consumer.class ) )
            {
                try
                {   /// zu den Speziellen Entity-Creator hinzufuegen
                    specialEntities.put( c.toString(), (Consumer<CreatorInfo>)c.newInstance() );
                } catch (InstantiationException | IllegalAccessException e)
                {
                    // TODO 
                    // Fehler bein Instanzieren
                    e.printStackTrace();
                }
            }
        }
    }
}
