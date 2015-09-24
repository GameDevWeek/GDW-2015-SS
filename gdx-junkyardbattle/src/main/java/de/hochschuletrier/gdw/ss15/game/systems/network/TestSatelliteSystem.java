package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.MapSpecialEntities.CreatorInfo;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SpawnSatelliteComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
import de.hochschuletrier.gdw.ss15.game.MapSpecialEntities;
import de.hochschuletrier.gdw.ss15.game.MapLoader;

public class TestSatelliteSystem extends IteratingSystem {
	ServerGame serverGame;
	MyTimer timer = new MyTimer(true);
	float x;
    float y;
    int shards;
	private ComponentMapper<InventoryComponent> inventory;
	private ComponentMapper<PositionComponent> position;
	private ComponentMapper<PositionSynchComponent> positionSynch;
	boolean satellite = false;
	public TestSatelliteSystem(ServerGame serverGame) {
		super(Family.all(SpawnSatelliteComponent.class).get());
		
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
		if(timer.get_CounterSeconds()>10 && satellite == false)
        {
		    System.out.println("Satellite spawned");
		    satellite = true;
            serverGame.createEntity("SatelliteSiteServer", x, y);
            System.out.println(x+" , "+ y);
            shards = 2;
        }
		
		
	}
	



	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		
		
		PositionComponent posc = ComponentMappers.position.get(entity);
        InventoryComponent inventory = ComponentMappers.inventory.get(entity);
        PositionSynchComponent pos = ComponentMappers.positionSynch.get(entity);

          //int shards =inventory.getMetalShards();
          
          x= posc.x;
          y = posc.y;
          
          //System.out.println(x+" , "+y);
        	
        	if(shards>1)
        	{
        		entity.removeAll();
        		satellite = false;
        		timer.ResetTimer();
        		shards = 2;
        		System.out.println("Ich bin hier");
        		
        	}
 
	}
 
}