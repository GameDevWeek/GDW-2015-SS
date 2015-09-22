package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

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
        int posX;
        int posY;
        Entity entity;
        TileInfo asTile;
        LayerObject asObject;
        Layer layer;            /// Layer fuer Renderer
        TiledMap tiledMap;
        public CreatorInfo(Entity ent,TiledMap tm,LayerObject lo,Layer layer)
        {
            posX = 0;posY = 0;     /// x und y sind bei Objecten = 0  ->  erhalte Position ueber PositionComponent     
            tiledMap = null;
            asTile = null;
            entity = ent;
            asObject = lo;
            this.layer = layer;
        }
        public CreatorInfo(int x,int y,TiledMap tm,TileInfo ti,Layer layer)
        {
            posX = x;
            posY = y;
            entity = null;
            asObject = null;
            asTile = ti;
            this.layer = layer;            
        }
    }
    public static HashMap< String,Consumer<CreatorInfo> > specialEntities;
    
    
    
    
    
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
            
            /// if ( test != null ) {
                /// erhaltenen Wert lesen 
                /// boolean flag = info.data.getBooleanProperty("TestFlag", false);
            
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
            
            /// if ( test != null ) {
                /// erhaltenen Wert lesen 
                /// boolean flag = info.data.getBooleanProperty("TestFlag", false);
            
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
