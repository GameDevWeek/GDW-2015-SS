package de.hochschuletrier.gdw.ss14.sandbox.maptest;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.game.Game;

/**
 * 
 * @author Tobias Gepp
 *
 */


public class MapLoader
{    
    /**
     * Standard Konstruktor
     */
    public MapLoader()
    {
    }

    /**
     * laedt die Map in das Spiel
     * @param game Spielstand fuer das die Entities geladen werden sollen
     * @param filename Name der Mapdatei die geladen werden soll
     */
    public void run(Game game, String filename)
    {     
        TiledMap tiledMap;
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
                    
                    
                    // Einwaende vorhanden moeglicherweise durch Listener ersetzen 
                    switch( objectName )
                    {
                    //
                    case "Dummy":  // ein Object zum testen
                        loadEntityDummy( resultEnt,obj );
                        break;
                    default:    
                        /// nichts tun da einfache Objekte per 'createEntity' bereits vollstaendig erstellt werden
                        break;
                    }
                }
            } else
            {   /// Tiles
                // Objekte laden die fest in der tiledMap sind
            }
        }
    }
    
    /**
     * Lade die Entity 'Dummy'
     * Dummy besitzt KEINE Components 
     * @param name
     * @param x
     * @param y
     * @param obj
     * @return
     */
    private Entity loadEntityDummy( Entity ent, LayerObject obj)
    { 
        /*
        // eine Componente herraussuchen und mit den Richtigen Werten besetzen 
        TestComponent body = ComponentMapper.test.get( ent );
        if ( test != null )
        {
            boolean flag = obj.getBooleanProperty("TestFlag", false);
            body.flag = flag;
        }
        */
        return ent;
    }
}
