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
        public int posX;
        public int posY;
        public TiledMap tiledMap;
        public Entity entity;
        public TileInfo asTile;
        public LayerObject asObject;
        public Layer layer;            /// Layer fuer Renderer
        public CreatorInfo(Entity ent,TiledMap tm,LayerObject lo,Layer layer)
        {
            posX = 0;posY = 0;
            tiledMap = null;
            entity = ent;
            asObject = lo;
            asTile = null;
            this.layer = layer;
            tiledMap = tm;
        }
        public CreatorInfo(int x,int y,TiledMap tm,TileInfo ti,Layer layer)
        {
            posX = x;
            posY = y;
            entity = null;
            asObject = null;
            asTile = ti;
            this.layer = layer;     
            tiledMap = tm;
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
            if ( Consumer.class.isAssignableFrom( c ) )
            {
                try
                {   /// zu den Speziellen Entity-Creator hinzufuegen
                    System.out.println("Creating texture entity.");
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
