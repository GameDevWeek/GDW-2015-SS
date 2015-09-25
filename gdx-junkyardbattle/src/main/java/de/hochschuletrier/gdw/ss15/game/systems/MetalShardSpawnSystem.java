package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.MetalShardSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

public class MetalShardSpawnSystem extends IteratingSystem{

	private ServerGame game;
	
	public MetalShardSpawnSystem(ServerGame game) {
		super(Family.all(PositionComponent.class, MetalShardSpawnComponent.class).get());
		this.game = game;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
	    //min. Zeit zwischen Shard Spawns
		float minTimeBetweenSpawns = 5.f;
		MyTimer timer = ComponentMappers.metalShardSpawn.get(entity).timer;
		timer.Update();
		
		//wenn die min. Zeit zwischen Spawns erreicht/?berschritten wurde
		if (timer.get_CounterSeconds() > minTimeBetweenSpawns)
		{
	        //nur spawnen, wenn der Spawn aktuell frei ist, d.h. wenn kein Objekt darauf steht/liegt
			if(ComponentMappers.metalShardSpawn.get(entity).collidingObjects <= 0)
			{
			    //timer resetten
	            timer.StartCounter();
			    game.createEntity("metalServer", ComponentMappers.position.get(entity).x, ComponentMappers.position.get(entity).y); 
			}
		}
	}
}
