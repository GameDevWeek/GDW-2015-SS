package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

public class TestSatelliteSystem extends IteratingSystem {
	ServerGame serverGame;
	MyTimer timer = new MyTimer(true);
	private ComponentMapper<InventoryComponent> inventory;
	private ComponentMapper<PositionComponent> position;
	private ComponentMapper<PositionSynchComponent> positionSynch;
	boolean satellite = false;
	public TestSatelliteSystem(ServerGame serverGame) {
		super(Family.all(InventoryComponent.class).get());
		this.serverGame = serverGame;
		inventory = ComponentMappers.inventory;
		position = ComponentMappers.position;
		positionSynch = ComponentMappers.positionSynch;
		timer.StartCounterandUpdate();
		
	}
	@Override
	public void update(float deltaTime)
	{
		timer.Update();
		if(timer.get_CounterSeconds()>30 && satellite == false)
        {
			satellite = true;
			serverGame.createEntity("SatelliteSiteServer", 300, 300);
        }
		
	}
	



	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		
		
		PositionComponent posc = ComponentMappers.position.get(entity);
        InventoryComponent inventory = ComponentMappers.inventory.get(entity);
        PositionSynchComponent pos = ComponentMappers.positionSynch.get(entity);
        inventory.metalShards = 750;
        
        	
        	
        	
        	if(inventory.metalShards<1)
        	{
        		entity.removeAll();
        		satellite = false;
        	}
        

	}
}