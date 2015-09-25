package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

public class MetalShardSpawnComponent extends Component implements Pool.Poolable{

	//wie häufig soll gespawnt werden
	public MyTimer timer;
	//wieviele Objekte stehen gerade auf dem Spawn
	//nur wenn es 0 sind, darf der Spawner ein neues MetalShard spawnen
	public int collidingObjects;
	//Zeit zwischen Spawns
	public float minTimeBetweenSpawns;
	
	public MetalShardSpawnComponent() {
		timer = new MyTimer(true);
		collidingObjects = 0;
		minTimeBetweenSpawns = 5.f;
	}
	
	@Override
	public void reset() {
		timer.ResetTimer();
		collidingObjects = 0;
        minTimeBetweenSpawns = 5.f;
	}

}
