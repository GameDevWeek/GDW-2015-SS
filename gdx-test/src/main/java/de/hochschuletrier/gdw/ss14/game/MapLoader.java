package de.hochschuletrier.gdw.ss14.game;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;

/**
 * 
 * @author tobidot (Tobias Gepp)
 *
 */


public class MapLoader
{    
    private TiledMap tiledMap;
    /**
     * Standard Konstruktor
     */
    public MapLoader()
    {
    }
    
    public TiledMap getTiledMap()
    {
        return tiledMap;
    }

    /**
     * laedt die Map in das Spiel
     * @param game Spielstand fuer das die Entities geladen werden sollen
     * @param filename Name der Mapdatei die geladen werden soll
     */
    public void run(Game game, String filename)
    {     
        /// Datei auslesen und in tiledMap packen
        try
        {
            tiledMap = new TiledMap( filename );
        }  catch (Exception ex) 
        {
            throw new IllegalArgumentException( "Map konnte nicht geladen werden: " + filename);
        }
        
        /// Objekte aus tiledMap laden und per Entitycreator im Game erstellen 
        loadObjectsFromMap( game,tiledMap );
    }

    /** 
     * Objekte aus tiledMap laden und per Entitycreator im Game erstellen
     * @param game Spiel das gefuellt werden soll
     * @param tiledMap Map die geladen wird
     */     
    private void loadObjectsFromMap(Game game,TiledMap tiledMap)
    {
        /// fuer alles Layers 
        for (Layer layer : tiledMap.getLayers() )
        {
            if( layer.isObjectLayer() )
            {   /// Objects
                for( LayerObject obj : layer.getObjects() )
                {
                    Entity resultEnt;
                    String objectName = obj.getName();
                    float xPos = obj.getX();
                    float yPos = obj.getY();
                    resultEnt = game.createEntity(objectName, xPos, yPos);
                    
                    
                    Consumer<MapSpecialEntities.CreatorInfo> creator = MapSpecialEntities.specialEntities.get( objectName );
                    if ( creator != null )
                    {   /// eine Spezialbehandlung gefunden
                        creator.accept( new MapSpecialEntities.CreatorInfo(resultEnt,obj) );
                    }                        
                }
            } else
            {   /// Tiles
                // Objekte laden die fest in der tiledMap sind
            }
        }
    }
    
}
