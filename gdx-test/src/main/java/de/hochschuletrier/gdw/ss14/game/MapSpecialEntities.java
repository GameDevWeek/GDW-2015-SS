package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.tiled.LayerObject;

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
    public static class CreatorInfo
    {
        Entity entity;
        LayerObject data;
        public CreatorInfo(Entity ent,LayerObject lo)
        {
            entity = ent;
            data = lo;
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
