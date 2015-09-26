package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.ashley.core.Engine;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.SatelliteColliding;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SpawnSatelliteComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

public class TestSatelliteSystem extends IteratingSystem
{
    
	ServerGame serverGame;
	MyTimer timer = new MyTimer(true);
    
	private ComponentMapper<InventoryComponent> inventory;
	private ComponentMapper<PositionComponent> position;
	private ComponentMapper<PositionSynchComponent> positionSynch;
	private final PooledEngine engine;
	
	public static boolean satellite = false;
    float x;
    float y;
    
    
	public TestSatelliteSystem(ServerGame serverGame, PooledEngine engine)
	{
		super(Family.all(SpawnSatelliteComponent.class).get());
		
		this.engine = engine;
		this.serverGame = serverGame;
		inventory = ComponentMappers.inventory;
		position = ComponentMappers.position;
		positionSynch = ComponentMappers.positionSynch;

		timer.StartCounterandUpdate();
		
	}
	@Override
	public void update(float deltaTime)
	{
	    super.update(deltaTime);
		timer.Update();
		
		
		if(timer.get_CounterSeconds()>60 && satellite == false)
        {
		    
		    satellite = true;
		    Entity satelliteEvent = serverGame.createEntity("SatelliteSiteServer", x, y);
            SatelliteColliding.emit();
            SoundEvent.emit("sat_explode", satelliteEvent);
        }
		
		
		
	}
	

 

	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		
		PositionComponent posc = ComponentMappers.position.get(entity);
        InventoryComponent inventory = ComponentMappers.inventory.get(entity);
        PositionSynchComponent pos = ComponentMappers.positionSynch.get(entity);

          x= posc.x;
          y = posc.y;
          
        	
        	if(inventory.getMetalShards()<1)
        	{
        		engine.removeEntity(entity);
        		satellite = false;

        		timer.ResetTimer();
        		
        	}
	}

    public boolean getSatellite(){
        return this.satellite;
    }

}