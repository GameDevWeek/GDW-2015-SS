package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.MapLoader.TileCreationListener;
import de.hochschuletrier.gdw.ss15.game.components.SpawnComponent;

import java.util.HashMap;
import java.util.function.Consumer;


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
        public int posX;
        public int posY;
        public Entity entity;
        public TileInfo asTile;
        public LayerObject asObject;
        public Layer layer;            /// Layer fuer Renderer
        public TiledMap tiledMap;
        public CreatorInfo(Entity ent,TiledMap tm,LayerObject lo,Layer layer)
        {
            posX = 0;posY = 0;     /// x und y sind bei Objecten = 0  ->  erhalte Position ueber PositionComponent     
            tiledMap = null;
            asTile = null;
            entity = ent;
            asObject = lo;
            this.layer = layer;
            tiledMap = tm;
        }
        public CreatorInfo(int x,int y,TiledMap tm,TileInfo ti,Layer layer)
        {
            tiledMap = tm;
            posX = x;
            posY = y;
            tiledMap = tm;
            entity = null;
            asObject = null;
            asTile = ti;
            this.layer = layer;            
        }
    }
    public static HashMap< String,Consumer<CreatorInfo> > specialEntities;
    
    
    
    /**
     * Wird bei der erstellung aller Maprelevanten Elemente aufgerufen (Tiles und LayerObject)
     * @author tobidot
     */
    public static void forAllElements ( CreatorInfo info )
    {
        
    }
    
    
    
    
    public static class SpawnPoint implements Consumer<CreatorInfo>
    {
        public void accept(CreatorInfo info)
        {
            
            /// eine Componente herraussuchen 
            SpawnComponent spawn = info.entity.getComponent( SpawnComponent.class );
            
            /// fuer wen spawn der Spawnpoint?
            /*
            if ( team != null ) {
                /// erhaltenen Wert lesen 
                int nr = info.asObject.getIntProperty("TeamID", 0);
            
                /// Komponente mit diesem Wert besetzten
                /// team.flag = flag;
            /// }
             * 
             */
        }
    }
    
    
    /**
     * Klasse zum laden eines 'Dummy' Objects
     * Diese Klasse kopieren um ein Object zu veraendern
     */
    public static class DummyEntity implements Consumer<CreatorInfo>
    {
        public void accept(CreatorInfo info)
        {
            
            /// eine Componente herraussuchen 
            /// TestComponent body = ComponentMapper.test.get( info.entity );
            
            /// if ( body != null ) {
                /// erhaltenen Wert lesen 
                /// boolean flag = info.asObject.getBooleanProperty("TestFlag", false);
            
                /// Komponente mit diesem Wert besetzten
                /// body.flag = flag;
            /// }
        }
    }
    
    
    /**
     * Klasse zum laden eines 'Dummy' Tile
     * Diese Klasse kopieren um ein Object Oder ein Tile zu erstellen/veraendern
     */
    public static class DummyTile implements Consumer<CreatorInfo>
    {
        public void accept(CreatorInfo info)
        {
            
            /// eine Componente herraussuchen 
            /// TestComponent body = ComponentMapper.test.get( info.entity );
            
            /// if ( body != null ) {
                /// erhaltenen Wert lesen 
                /// boolean flag = info.asTile.getProperties().getBooleanProperty("TestFlag", false);
            
                /// Komponente mit diesem Wert besetzten
                /// body.flag = flag;
            /// }
        }
    }
    
    
    
    
    
    static
    {
        Class allClasses[] = MapSpecialEntities.class.getClasses();         /// Alle Memberklassen von 'MapSpecialEntities'
        specialEntities = new HashMap<String, Consumer<CreatorInfo>>();     
        for ( Class c : allClasses ) 
        {
            /// nur alle Klassen, die von Consumer abgeleitet sind
            if ( Consumer.class.isAssignableFrom( c ) )
            {
                try
                {   /// zu den Speziellen Entity-Creator hinzufuegen
                    specialEntities.put( c.getSimpleName(), (Consumer<CreatorInfo>)c.newInstance() );
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
