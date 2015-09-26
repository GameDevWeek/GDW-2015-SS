package de.hochschuletrier.gdw.ss15.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss15.game.components.AboveAbyssComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.PhysixBodyComponentFactory;

/**
 * 
 * @author tobidot (Tobias Gepp)
 * 
 *      DOKUMENTATION
 * 
 * In dieser Klasse( MapLoader ) sollten keine Aendereungen gemacht werden 
 * Entities oder aehnliches werden in 'MapSpecialEntities' modifiziert
 * 
 * eine Klasse die den 'TileCreationListener' implmentiert
 * kann sich per 
 *      ('MapLoader').listen( ('TileCreationListener') );
 * regristieren 
 * die Methode TileCreationListener.onTileCreate() wird 
 * bei jeder erstellung (aus der Map) eines Tiles oder eines LayerObjects aufgerufen.
 *
 */


public class MapLoader
{    
    private static ArrayList<TileCreationListener> tileListeners = new ArrayList<TileCreationListener>();
    
    
    private TiledMap tiledMap;
    /**
     * Standard Konstruktor
     */
    
    public interface EntityCreator
    {
        Entity createEntity(String name, float x, float y);
    }
    
    
    public interface TileCreationListener
    {
        public void onTileCreate( MapSpecialEntities.CreatorInfo info );
    } 
    public void listen( TileCreationListener listener )
    {
        tileListeners.add( listener );
    }
    public void stopListening( TileCreationListener listener )
    {
        tileListeners.remove( listener );
    }
    
    public MapLoader()
    {
    }
    
    public TiledMap getTiledMap()
    {
        return tiledMap;
    }

    /**
     * laedt die Map in das Spiel
     * @param filename Name der Mapdatei die geladen werden soll
     */
    public void run(EntityCreator creator, String filename,PhysixSystem pSystem,EntityFactory entityFactory,AssetManagerX aMana)
    {     
        /// Datei auslesen und in tiledMap packen
        try
        {
            tiledMap = new TiledMap( filename );
            // Attach textures to the tilesets
            HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
            for (TileSet tileset : tiledMap.getTileSets()) {
                TmxImage img = tileset.getImage();
                String fn = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
                Texture tex = new Texture(fn);
                tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
                tilesetImages.put(tileset, tex);
            }
            for (TileSet tileset : tiledMap.getTileSets()) {
                tileset.setAttachment(tilesetImages.get(tileset));
            }
        }  catch (Exception ex) 
        {
            throw new IllegalArgumentException( "Map konnte nicht geladen werden: " + filename,ex);
        }
        
        /// Objekte aus tiledMap laden und per Entitycreator im Game erstellen 
        loadObjectsFromMap( pSystem,creator,tiledMap,entityFactory,aMana );
    }

    /**
     * Ein Shape zur Physik hinzufuegen
     * @param rect  zu erstellendes Rechteck
     * @param tileWidth breite eines tile
     * @param tileHeight hoehe eines tile
     */
    private void addShape(PhysixSystem pSystem,Rectangle rect, int tileWidth, int tileHeight, boolean blockShoot) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        // TODO
        // noch spezialisieren auf Flags ( block pathing, block sight, block shooting  )
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, pSystem).position(x, y).fixedRotation(false);
        Body body = pSystem.getWorld().createBody(bodyDef);
        if(!blockShoot){
            //body.createFixture(new PhysixFixtureDef(pSystem).density(1).friction(0.5f).category(PhysixBodyComponentFactory.ABGRUND).mask((short) ~PhysixBodyComponentFactory.BULLET).shapeBox(width, height));
            body.createFixture(new PhysixFixtureDef(pSystem).density(1).friction(0.5f).shapeBox(width, height));
            for (int i = 0; i < body.getFixtureList().size; i++) {
                body.getFixtureList().get(i).setUserData(new AboveAbyssComponent());
            }
            
            //System.out.println(body.getClass());
        }
        else
            body.createFixture(new PhysixFixtureDef(pSystem).density(1).friction(0.5f).shapeBox(width, height));
    }
    
    /** 
     * Objekte aus tiledMap laden und per Entitycreator im Game erstellen
     * @param tiledMap Map die geladen wird
     */     
    private void loadObjectsFromMap(PhysixSystem pSystem,EntityCreator entCreator,TiledMap tiledMap,EntityFactory entityFactory,AssetManagerX aMana)
    {
        
        
        int mapWidth = tiledMap.getWidth();
        int mapHeight = tiledMap.getHeight();
        int tileWidth = tiledMap.getTileWidth();
        int tileHeight = tiledMap.getTileHeight();
        
        /// Santomagic
        if ( pSystem != null )  /// Auf dem Server werden die PhysixsSystem nihct erstellt
        {
            // hier wegen Pathing und visual absprechen @render @physix @asset
            RectangleGenerator generator = new RectangleGenerator();
            generator.generate( tiledMap,
                    (Layer layer, TileInfo info) -> {return info.getBooleanProperty("BlockPath", false) && info.getBooleanProperty("BlockShoot", false); },
                    (Rectangle rect) -> addShape(pSystem,rect, tileWidth, tileHeight, true) );
            

            // hier wegen Pathing und visual absprechen @render @physix @asset
            generator.generate( tiledMap,
                    (Layer layer, TileInfo info) -> {return info.getBooleanProperty("BlockPath", false) && !info.getBooleanProperty("BlockShoot", false); },
                    (Rectangle rect) -> addShape(pSystem,rect, tileWidth, tileHeight, false) );
        }
        
        /// fuer alles Layers 
        for (Layer layer : tiledMap.getLayers() )
        {
            if( layer.isObjectLayer() )
            {   /// Objects
                for( LayerObject obj : layer.getObjects() )
                {
                    
                    Entity resultEnt;
                    String objectName = obj.getName();
                    float xPos = obj.getX() + (obj.getWidth() / 2);// / 2-tileWidth/2;
                    float yPos = obj.getY() + (obj.getHeight() / 2);// / 2-tileHeight/2;
                    resultEnt = entCreator.createEntity(objectName, xPos, yPos);

                    MapSpecialEntities.CreatorInfo info = new MapSpecialEntities.CreatorInfo(entCreator,entityFactory,resultEnt,tiledMap,obj,layer,aMana);
                    
                    MapSpecialEntities.forAllElements( info );
                    
                    /// koente hier ueberfluessig sein  @author tobidot
                    for( TileCreationListener l :tileListeners )  {
                        l.onTileCreate(info);
                    }
                    
                    Consumer<MapSpecialEntities.CreatorInfo> creator = MapSpecialEntities.specialEntities.get( objectName );
                    if ( creator != null )
                    {   /// eine Spezialbehandlung gefunden
                        
                        creator.accept( info );
                    }                        
                }
            } else
            {   /// Tiles
                // Objekte laden die fest in der tiledMap sind
                TileInfo[][] tiles = layer.getTiles();
                for( int x = 0; x < mapWidth; x++ )
                {
                    for( int y = 0; y < mapHeight; y++ )
                    {
                        TileInfo tileInfo = tiles[x][y];
                        
                        MapSpecialEntities.CreatorInfo info = new MapSpecialEntities.CreatorInfo( entCreator,entityFactory,x,y,tiledMap, tileInfo ,layer,aMana );

                        MapSpecialEntities.forAllElements( info );
                        
                        for( TileCreationListener l :tileListeners )  {
                            l.onTileCreate(info);
                        }
                        
                        if ( tileInfo != null )
                        {
                            /// Name des Tiles bekommen
                            TileSet ts = tiledMap.findTileSet( tileInfo.globalId );
                            String objectName = ts.getName();
                            
                            
                            float xPos = x * tileWidth;
                            float yPos = y * tileHeight;
                            
                            Consumer<MapSpecialEntities.CreatorInfo> creator = MapSpecialEntities.specialEntities.get( objectName );
                            if ( creator != null )
                            {   /// eine Spezialbehandlung gefunden
                                creator.accept( info );                                
                            } 
                        }
                    }
                }
            }
        }
    }
    
}
