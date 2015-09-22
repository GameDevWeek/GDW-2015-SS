package de.hochschuletrier.gdw.ss14.game;

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
        TiledMap tiledMap;
        Entity entity;
        TileInfo asTile;
        LayerObject asObject;
        Layer layer;            /// Layer fuer Renderer
        public CreatorInfo(Entity ent,TiledMap tm,LayerObject lo,Layer layer)
        {
            posX = 0;posY = 0;
            tiledMap = null;
            entity = ent;
            asObject = lo;
            asTile = null;
            this.layer = layer;
        }
        public CreatorInfo(Entity ent,int x,int y,TiledMap tm,TileInfo ti,Layer layer)
        {
            posX = x;
            posY = y;
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
